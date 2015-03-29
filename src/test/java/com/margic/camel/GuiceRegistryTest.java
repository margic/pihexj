package com.margic.camel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.margic.pihex.camel.BindCamelRegistry;
import com.margic.pihex.camel.context.GuiceRegistry;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/29/15.
 */
public class GuiceRegistryTest {

    @Test
    public void testBindCamelRegistryAnnotation(){
        // need injector
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(TestClass.class);
            }
        });

        GuiceRegistry registry = new GuiceRegistry(injector);
        Object object = registry.lookupByName("myBean");
        assertNotNull(object);
        TestClass testClass = registry.lookup("myBean", TestClass.class);
        assertNotNull(testClass);
    }

    @BindCamelRegistry(ref = "myBean")
    static class TestClass{

    }

    @Test
    public void testBindCamelRegistryNamedAnnotation(){
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).annotatedWith(Names.named("testName")).toInstance("My String Value");
            }
        });

        GuiceRegistry registry = new GuiceRegistry(injector);
        String testString = (String)registry.lookup("testName");
        assertEquals("My String Value", testString);

        testString = registry.lookup("testName", String.class);
        assertEquals("My String Value", testString);
    }
}
