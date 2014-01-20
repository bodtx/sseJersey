package com.example;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
    	super.registerClasses(SseFeature.class,MyResource.class,MyResourceSSE.class, MyBroadcasterSSE.class);
//        packages("com.example");
    }
}
