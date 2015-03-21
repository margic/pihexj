<h1>PiHexJ</h1>

<p>
PiHexJ is a Java project I created for a hex robot I am building using RC Servos and a chasis from LynxMotion.
 LynxMotion actually provide complete controllers but where's the fun in that.
</p>

<p>
My robot is going to use a Raspberry Pi as a controller and a pair of Adafruit 16-Channel 12-bit PWM/Servo Driver http://www.adafruit.com/product/815.
The Servo Driver is based on a PCA9685 from NXP Semiconductors.
</p>

<p>
I am providing a Java driver for the PCA9685. The driver uses the I2C bus on the Raspberry Pi via the Pi4J library https://github.com/Pi4J.
I am developing in Intellij using maven and junit. Once stable I may separate the driver into it's own repository. It really depends on feedback.
</p>

<p>
Please check out my blog posts on the progress of the robot. It will give you a details of the steps and where I am in the development as the code
reflects the current state of development and is changing frequently.
</p>

<p>
Check out the blog --> http://pihex.blogspot.com/
</p>