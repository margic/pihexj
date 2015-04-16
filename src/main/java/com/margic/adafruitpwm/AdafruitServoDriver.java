package com.margic.adafruitpwm;

import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.support.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by paulcrofts on 3/15/15.
 * Servo driver implementation for Adafruit-PCA9685 pwm board
 * See datasheet http://www.adafruit.com/datasheets/PCA9685.pdf
 */
public class AdafruitServoDriver implements ServoDriver {

    private static final Logger log = LoggerFactory.getLogger(AdafruitServoDriver.class);

    private static final String DRIVER_NAME = "Adafruit-PCA9685";

    public static final double CLOCK_FREQUENCY = 25 * 1000000;
    public static final double RESOLUTION = 4096;

    private boolean updateSequencial;


    private int mode1Value;
    private int mode2Value;

    private PCA9685Device[] devices;
    private int frequency;
    /*
      * cache is use when writing multiple servos to cache any missing
      * servos from the supplied list. The PCA9685 allows writing sequential
      * registers. This avoids a lot of i2c overhead. To take advantage of
      * sequential writes the cache can cache state of previous writes to registers
     */
    private int[] servoCache; // the servo pulse position cache

    // TODO add some sort of lock on this object while reading to avoid updates while doing serial writes consider cloning before flush
    private SortedSet<ServoUpdateEvent> eventBuffer;

    /**
     * Creates a new driver with an array of devices to allow multiples of NUM_CHANNELS
     * The servo driver can work in two different modes. update sequences of servos or update
     * single servos at a time. These are currently exclusive modes. It is inefficient to switch
     * between modes at run time do the the setting required on the mode one register.
     *
     * @param devices
     * @param updateSequencial
     */
    @Inject
    public AdafruitServoDriver(PCA9685Device[] devices, boolean updateSequencial) {
        log.debug("Creating new servo driver {}", getDriverName());
        this.devices = devices;
        this.servoCache = new int[devices.length * PCA9685Device.NUMBER_CHANNELS];
        eventBuffer = new TreeSet<>();
        this.updateSequencial = updateSequencial;
    }

    @Override
    public String getDriverName() {
        return DRIVER_NAME;
    }

    /**
     * Init the device with the default settings in case existing settings are still in memory
     *
     * @throws IOException
     */
    @Override
    public void init() throws IOException {
        log.info("Initializing the device to preferred start up state"); // essentially the power on default
        for (PCA9685Device device : devices) {
            log.info("Device: {}", device.getDeviceName());
            log.debug("Setting all registers off using ALL registers ");
            device.writeRegister(PCA9685Device.ALL_LED_ON_L, (byte) 0x00);
            device.writeRegister(PCA9685Device.ALL_LED_ON_H, (byte) 0x00);
            device.writeRegister(PCA9685Device.ALL_LED_OFF_L, (byte) 0x00);
            device.writeRegister(PCA9685Device.ALL_LED_OFF_H, (byte) 0x00);

            log.debug("Set mode 2 with only OUTDRV bit high: {}", PCA9685Device.OUTDRV);
            device.writeRegister(PCA9685Device.MODE2, (byte) PCA9685Device.OUTDRV);
            mode2Value = PCA9685Device.OUTDRV;


            if(updateSequencial) {
                log.debug("Servo driver mod is sequencial, enable the auto increment flag on mode 1 register");
                mode1Value |= PCA9685Device.MODE1_AUTO_INCREMENT;
                device.writeRegister(PCA9685Device.MODE1, (byte) mode1Value);
            }
        }
    }

    /**
     * The hardware forces a minimum value that can be loaded into the PRE_SCALE register
     * at ‘3’. The PRE_SCALE register defines the frequency at which the outputs modulate. The
     * prescale value is determined with the formula shown in Equation 1:
     * (1) prescaleVal = round(oscClock/(4096 * updateRate)) - 1
     * where the update rate is the output modulation frequency required. For example, for an
     * output frequency of 200 Hz with an oscillator clock frequency of 25 MHz:
     * (2) prescaleVal = round(25000000 / (4096 * 200)) - 1 = 30
     * The PRE_SCALE register can only be set when the SLEEP bit of MODE1 register is set to
     * logic 1.
     *
     * @param frequency the frequency in hertz
     */
    @Override
    public void setPulseFrequency(int frequency) throws IOException {
        for (PCA9685Device device : devices) {
            log.info("Device: {} - Setting pwm frequency to {} hz", getDriverName(), frequency);
            this.frequency = frequency;
            int prescale = getPreScale(frequency);

            log.debug("Reading value of Mode 1 register");
            int oldMode1 = device.readRegister(PCA9685Device.MODE1);
            log.debug("Mode 1 register: {}", Integer.toHexString(oldMode1));

            int newMode1 = (oldMode1 & 0x7F) | PCA9685Device.MODE1_SLEEP;
            log.debug("Setting sleep bit on Mode 1 register: {}", Integer.toHexString(newMode1));
            device.writeRegister(PCA9685Device.MODE1, (byte) newMode1);

            log.debug("Writing prescale register with: {}", Integer.toHexString(prescale));
            device.writeRegister(PCA9685Device.PRESCALE, (byte) prescale);

            newMode1 = oldMode1 & ~PCA9685Device.MODE1_SLEEP;
            log.debug("Writing the old value back to mode1 register with sleep off to start osc again: {}", Integer.toHexString(newMode1));
            device.writeRegister(PCA9685Device.MODE1, (byte) (newMode1));
            mode1Value = newMode1;
            // wait for oscillator to restart
            sleep(50);
            newMode1 = oldMode1 | PCA9685Device.MODE1_RESTART;
            log.debug("Setting restart bit: {}", Integer.toHexString(newMode1));
            device.writeRegister(PCA9685Device.MODE1, (byte) newMode1);
        }
    }

    public int getPulseFrequency() {
        if (frequency == 0) {
            return DEFAULT_PWM_FREQUENCY;
        }
        return frequency;
    }


    public int getPreScale(int frequency) throws IOException {
        log.debug("Get prescale value for frequency {}", frequency);
        double correctedFrequency = frequency * 0.9;  // Correct for overshoot in the frequency setting (see issue #11).
        double prescaleval = CLOCK_FREQUENCY;
        prescaleval /= RESOLUTION;
        prescaleval /= correctedFrequency;
        prescaleval -= 1.0;
        log.debug("Estimated pre-scale {}", prescaleval);
        prescaleval = Math.round(prescaleval + 0.5);

        if (prescaleval > 254) {
            throw new IOException("Specified frequency " + frequency + " results in prescale value " + prescaleval + " that exceed limit 254");
        }
        int prescale = (int) prescaleval;
        log.debug("Final pre-scale {}", prescale);
        return prescale;
    }

    /**
     * buffer updates in event buffer
     * not servos not updated until flush called
     *
     * @param servoUpdate
     * @throws IOException
     */
    @Override
    public void updateServo(ServoUpdateEvent servoUpdate) throws IOException {
        eventBuffer.add(servoUpdate);
    }

    @Override
    public int flush() throws IOException {
        int updateCount = 0;
        if(updateSequencial) {
            updateCount += updateAllServos();
        }else {
            for (ServoUpdateEvent event : eventBuffer) {
                updateCount += updateSingleServo(event);
            }
        }
        eventBuffer.clear();
        return updateCount;
    }

    private int updateSingleServo(ServoUpdateEvent servoUpdate) throws IOException {
        // update cache first
        if (isMoved(servoUpdate)) {
            // only send update to servo if its position has actually moved.
            cacheServoPosition(servoUpdate);
            int servoChannel = servoUpdate.getChannel();
            int pulseLength = servoUpdate.getPulseLength();

            // calc num counts for ms
            int count = getCountForPulseLength(pulseLength);

            log.debug("Updating servo position: {}, count: {}", servoUpdate.toString(), count);

            byte[] offBytes = ByteUtils.get2ByteInt(count);
            PCA9685Device device = getDevice(servoChannel);
            device.writeRegister(getRegisterForChannel(servoChannel, Register.ON_LOW), (byte) 0x00);
            device.writeRegister(getRegisterForChannel(servoChannel, Register.ON_HIGH), (byte) 0x00);
            device.writeRegister(getRegisterForChannel(servoChannel, Register.OFF_LOW), offBytes[ByteUtils.LOW_BYTE]);
            device.writeRegister(getRegisterForChannel(servoChannel, Register.OFF_HIGH), offBytes[ByteUtils.HIGH_BYTE]);
            return 1;
        }else{
            // didn't update any
            return 0;
        }
    }

    /**
     * This method updates multiple servos in one byte stream taking advantge of
     * PCA9685 auto register increment to send bytes in one stream saving time
     * Bytes must be for contiguous registers
     * @return
     */
    private int updateAllServos() throws IOException {

        int updateCount = 0;

        while(!eventBuffer.isEmpty()) {
            // get the device for the first update
            ServoUpdateEvent nextEvent = eventBuffer.first();
            int firstChannel = nextEvent.getChannel();
            int deviceOffset = getDeviceOffset(firstChannel);
            // if this servo is on the second device it's devicechannel will be channel - 1 x number_channels
            // which device
            PCA9685Device device = getDevice(firstChannel);
            updateCount += updateDevice(device, deviceOffset);
        }
        return updateCount;
    }

    private int updateDevice(PCA9685Device device, int deviceOffset) throws IOException{
        int updateCount = 0;

        List<RegisterValue> registerValues = new ArrayList<>();
        //byte[] bytes = getRegisterBuffer();
//        // todo don't forget to set register 0 and 1
//        bytes[0] = (byte)mode1Value;
//        bytes[1] = (byte)mode2Value;
//        bytes[0] |= PCA9685Device.MODE1_AUTO_INCREMENT;
//        mode1Value = bytes[0];
        // prepare registers

        // get the first servo channel
        int firstEventChannel = eventBuffer.first().getChannel();
        // get the register address of this first channel need this to address the update I2C bus
        int firstRegisterAddress = getRegisterForChannel(firstEventChannel, Register.ON_LOW);
        int deviceChannel = firstEventChannel - deviceOffset;

        //loop through registers represented by i
        while(deviceChannel < PCA9685Device.NUMBER_CHANNELS && isMoreEventsForDevice(deviceOffset)) {
            RegisterValue registerValue = new RegisterValue();

            // set first two bytes to zero. Still setting on bytes to zero
            // set low then high
            registerValue.setOnLowByte((byte) 0x00);
            registerValue.setOnHightByte((byte) 0x00);

            ServoUpdateEvent event = null;
            if (!eventBuffer.isEmpty()) {
                event = eventBuffer.first();
                int eventChannel = event.getChannel() - deviceOffset;
                if (eventChannel == deviceChannel) {
                    // update event in buffer is for this channel
                    if (log.isTraceEnabled()) {
                        log.trace("Updating servo: {} count: {}", deviceChannel, event.getPulseLength());
                    }
                    cacheServoPosition(event);
                    int count = getCountForPulseLength(event.getPulseLength());
                    byte[] posistionBytes = ByteUtils.get2ByteInt(count);
                    registerValue.setOffLowByte(posistionBytes[ByteUtils.LOW_BYTE]);
                    registerValue.setOffHighByte(posistionBytes[ByteUtils.HIGH_BYTE]);
                    eventBuffer.remove(event);
                } else {
                    // update event is not for this channel check the cache for this channel or fill with zero
                    int cachedPosition = getCachedServoPosition(deviceChannel);
                    if (log.isTraceEnabled()) {
                        log.trace("Updating servo: {} with cached count: {}", deviceChannel, cachedPosition);
                    }
                    int count = getCountForPulseLength(cachedPosition);
                    byte[] positionBytes = ByteUtils.get2ByteInt(count);
                    registerValue.setOffLowByte(positionBytes[ByteUtils.LOW_BYTE]);
                    registerValue.setOffHighByte(positionBytes[ByteUtils.HIGH_BYTE]);
                }
                registerValues.add(registerValue);
                deviceChannel++;
                updateCount++;
            }
        }
        // build the array of bytes for the write.

        byte[] bytes = new byte[registerValues.size() * 4];
        int index = -1;
        for(RegisterValue value: registerValues){
            bytes[++index] = value.getOnLowByte();
            bytes[++index] = value.getOnHightByte();
            bytes[++index] = value.getOffLowByte();
            bytes[++index] = value.getOffHighByte();
        }

        // write bytes starting with start register then rest of bytes
        device.writeRegisters(firstRegisterAddress, bytes);
        return updateCount;
    }

    private boolean isMoreEventsForDevice(int deviceOffset){
        boolean answer = false;
        if(!eventBuffer.isEmpty()){
            int deviceChannel = eventBuffer.first().getChannel() - deviceOffset;
            if(deviceChannel < PCA9685Device.NUMBER_CHANNELS){
                answer = true;
            }
        }
        return answer;
    }

//    private int updateAllRegistersDevice(PCA9685Device device, int deviceOffset) throws IOException{
//        int updateCount = 0;
//
//        // which device
//        byte[] bytes = getRegisterBuffer();
//        // todo don't forget to set register 0 and 1
//        bytes[0] = (byte)mode1Value;
//        bytes[1] = (byte)mode2Value;
//        bytes[0] |= PCA9685Device.MODE1_AUTO_INCREMENT;
//        mode1Value = bytes[0];
//        // prepare registers
//        for(int i = 6; i < 69; i+=4){
//            int deviceServoChannel = (i - 6) / 4;
//            // set first two bytes to zero. Still setting on bytes to zero
//            bytes[i] = (byte)0x00;
//            bytes[i + 1] = (byte)0x00;
//            ServoUpdateEvent event = null;
//            if (!eventBuffer.isEmpty()) {
//                event = eventBuffer.first();
//                int eventChannel = event.getChannel() - deviceOffset;
//                if (eventChannel == deviceServoChannel) {
//                    // update event in buffer is for this channel
//                    if(log.isTraceEnabled()) {
//                        log.trace("Updating servo: {} count: {}", deviceServoChannel, event.getPulseLength());
//                    }
//                    cacheServoPosition(event);
//                    int count = getCountForPulseLength(event.getPulseLength());
//                    byte[] posistionBytes = ByteUtils.get2ByteInt(count);
//                    bytes[i + 2] = posistionBytes[ByteUtils.LOW_BYTE];
//                    bytes[i + 3] = posistionBytes[ByteUtils.HIGH_BYTE];
//                    eventBuffer.remove(event);
//                    updateCount++;
//                }
//            }else{
//                // update event is not for this channel check the cache for this channel or fill with zero
//                int cachedPosition = getCachedServoPosition(deviceServoChannel);
//                if(log.isTraceEnabled()) {
//                    log.trace("Updating servo: {} with cached count: {}", deviceServoChannel, cachedPosition);
//                }
//                int count = getCountForPulseLength(cachedPosition);
//                byte[] positionBytes = ByteUtils.get2ByteInt(count);
//                bytes[i + 2] = positionBytes[ByteUtils.LOW_BYTE];
//                bytes[i + 3] = positionBytes[ByteUtils.HIGH_BYTE];
//            }
//        }
//        // write bytes starting with start register then rest of bytes
//        device.writeRegisters(0, bytes);
//        return updateCount;
//    }

    public int getDeviceOffset(int servoChannel){
        // if this servo is on the second device it's devicechannel will be channel - 1 x number_channels
        return (servoChannel/PCA9685Device.NUMBER_CHANNELS) * PCA9685Device.NUMBER_CHANNELS;
    }

    private int getCountForPulseLength(int pulseLength){
        return (int) Math.round(pulseLength * RESOLUTION / ((double) 1 / (double) getPulseFrequency()) / (double) 1000000);
    }


    private byte[] getRegisterBuffer(){
        byte[] bytes = new byte[70];

        bytes[0] = (byte)mode1Value;
        bytes[1] = (byte)mode2Value;  // todo this value is not getting replaced
        // Subaddresses are programmable through the I2C-bus. Default power-up values are E2h, E4h, E8h,
        bytes[2] = (byte)0xE2;
        bytes[3] = (byte)0xE4;
        bytes[4] = (byte)0xE8;
        bytes[5] = (byte)0xE0;

        return bytes;
    }

    /**
     * returns the device that handles this channel
     *
     * @param channel
     * @return
     */
    private PCA9685Device getDevice(int channel) {
        int devNum = channel / PCA9685Device.NUMBER_CHANNELS;
        return devices[devNum];
    }

    private boolean isMoved(ServoUpdateEvent servoUpdate) {
        int cachedPosition = getCachedServoPosition(servoUpdate.getChannel());
        return cachedPosition != servoUpdate.getPulseLength();
    }

    private void cacheServoPosition(ServoUpdateEvent servoUpdate) {
        int servoChannel = servoUpdate.getChannel();
        servoCache[servoChannel] = servoUpdate.getPulseLength();
    }

    private int getCachedServoPosition(int channel){
        return servoCache[channel];
    }

    /**
     * Returns one of the 4 channel0 registers addresses based on servo channel
     *
     * @param channel
     * @param register
     * @return
     */
    public int getRegisterForChannel(int channel, Register register) {
        int registerAddress = (4 * channel) + (register.value + PCA9685Device.LED0_ON_LOW); // LED0_ON_HIGH is first of sequence of registers;
        return registerAddress;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("sleep interrupted.", e);
        }
    }

    enum Register {
        ON_LOW(0),
        ON_HIGH(1),
        OFF_LOW(2),
        OFF_HIGH(3);
        private final int value;
        Register(int value) {
            this.value = value;
        }
        int value() {
            return value;
        }
    }

    class RegisterValue{
        private byte onLowByte;
        private byte onHightByte;
        private byte offLowByte;
        private byte offHighByte;

        public void setOnLowByte(byte onLowByte) {
            this.onLowByte = onLowByte;
        }

        public void setOnHightByte(byte onHightByte) {
            this.onHightByte = onHightByte;
        }

        public void setOffLowByte(byte offLowByte) {
            this.offLowByte = offLowByte;
        }

        public void setOffHighByte(byte offHighByte) {
            this.offHighByte = offHighByte;
        }

        public byte getOnLowByte() {
            return onLowByte;
        }

        public byte getOnHightByte() {
            return onHightByte;
        }

        public byte getOffLowByte() {
            return offLowByte;
        }

        public byte getOffHighByte() {
            return offHighByte;
        }
    }
}
