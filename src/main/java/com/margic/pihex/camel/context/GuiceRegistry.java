package com.margic.pihex.camel.context;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.margic.pihex.camel.BindCamelRegistry;
import org.apache.camel.impl.JndiRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by paulcrofts on 3/27/15.
 */
public class GuiceRegistry extends JndiRegistry {

    private static final Logger log = LoggerFactory.getLogger(GuiceRegistry.class);
    private Injector injector;

    @Inject
    public GuiceRegistry(Injector injector) {
        super();
        this.injector = injector;
        bindGuiceObjects();
    }

    /**
     * method to scan injector bindings to register objects
     * annotated with BindCamelRegistry or names
     */
    private void bindGuiceObjects() {
        if (injector != null) {
            log.debug("Binding guice objects to registry");
            Map<Key<?>, Binding<?>> bindings = injector.getBindings();
            for (Key key : bindings.keySet()) {
                if (key.getAnnotationType() == Named.class || key.getAnnotationType() == BindCamelRegistry.class) {
                    log.debug("Found binding that should be bound to camel registry.");
                    Object object = injector.getInstance(key);
                    if (key.getAnnotation() instanceof Named) {
                        Named named = (Named) key.getAnnotation();
                        String name = named.value();
                        bind(name, object);
                    }
                }
            }
        }
    }


    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    /**
     * override create context for jndi registry to set the
     * initial contextfactory to the camel factory
     * @return
     * @throws NamingException
     */
    @Override
    protected Context createContext() throws NamingException {
        Hashtable<Object, Object> properties = new Hashtable<Object, Object>(System.getProperties());
        properties.put("java.naming.factory.initial", "org.apache.camel.util.jndi.CamelInitialContextFactory");
        return new InitialContext(properties);
    }

    //    @Override
//    public Object lookupByName(String name) {
//        Object answer = super.lookupByName(name);
//        if(answer == null){
//            log.debug("Looking up object in injector binding");
//
//
//        }
//        return answer;
//    }
}
