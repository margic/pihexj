package com.margic.camel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.margic.pihex.camel.context.GuiceRegistry;
import org.junit.Test;

import javax.inject.Named;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by paulcrofts on 3/29/15.
 * This test test both ways to bind to the camel registry,
 * annotate the binding binding with annotatedWith and annotate the class with @Named
 */
public class GuiceRegistryTest {

    @Named("myBean")
    static class TestClass{
        // this is just an empty class bound to name myBean for test
    }

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
