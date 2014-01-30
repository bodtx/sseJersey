package com.example;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Component;

/**
 * Example de broadcast
 */
@Component
@Singleton
@Path("broadcast")
public class MyBroadcasterSSE {

    @Inject
    SerialTest serialTest;

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        serialTest.broadcaster.add(eventOutput);
        return eventOutput;
    }
}
