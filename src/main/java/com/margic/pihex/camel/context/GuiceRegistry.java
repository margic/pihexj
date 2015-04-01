package com.margic.pihex.camel.context;

import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import org.apache.camel.impl.JndiRegistry;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by paulcrofts on 3/27/15.
 */
public class GuiceRegistry extends JndiRegistry {

    private static final Logger log = LoggerFactory.getLogger(GuiceRegistry.class);

    @Inject
    public GuiceRegistry(Injector injector, Configuration config) {
        super();
        bindGuiceObjects(injector, config);
    }

    /**
     * method to scan injector bindings to register objects
     * annotated with BindCamelRegistry or names
     */
    private void bindGuiceObjects(Injector injector, Configuration config) {
        // bind the config it will make testing way easier
        if(config != null){
            bind("configuration", config);
        }
        if (injector != null) {
            log.debug("Binding guice objects to registry");
            Map<Key<?>, Binding<?>> bindings = injector.getBindings();
            for (Key key : bindings.keySet()) {
                if (key.getAnnotationType() == Named.class) {
                    log.debug("Found binding annotated with named. Should be bound to camel registry.");
                    Object object = injector.getInstance(key);
                    Named named = (Named) key.getAnnotation();
                    String name = named.value();
                    bind(name, object);
                }else {
                    javax.inject.Named annotation = (javax.inject.Named)key.getTypeLiteral().getRawType().getAnnotation(javax.inject.Named.class);
                    if(annotation != null) {
                        log.debug("Found object bound with annotation BindCamelRegistry");
                        String name = annotation.value();
                        if (name != null) {
                            Object object = injector.getInstance(key);
                            bind(name, object);
                        }
                    }
                }
            }
        }
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

}
