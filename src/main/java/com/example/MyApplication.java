package com.example;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

//@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        register(SseFeature.class);
        register(RequestContextFilter.class);
        // register(MyBroadcasterSSE.class);
        packages("com.example");
        // super.registerClasses(SseFeature.class, MyBroadcasterSSE.class);
        // register(new AbstractBinder() {
        // @Override
        // protected void configure() {
        // bind(SerialTest.class).to(SerialTest.class);
        // }
        // });
    }
}
