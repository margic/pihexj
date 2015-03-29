package com.margic.camel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
    }

    @BindCamelRegistry(ref = "myBean")
    static class TestClass{

    }
}
