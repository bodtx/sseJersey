package com.example;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        super.registerClasses(SseFeature.class, MyBroadcasterSSE.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(SerialTest.class).to(SerialTest.class);
            }
        });
    }
}
