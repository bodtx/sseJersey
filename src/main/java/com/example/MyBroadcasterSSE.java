package com.example;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * Example de broadcast
 */
@Singleton
@Path("broadcast")
public class MyBroadcasterSSE {

    @Inject
    SerialTest serialTest;

    @PostConstruct
    void init() throws Exception {
        // new Thread(new Runnable() {
        // @Override
        // public void run() {
        // for (int i = 0; i < 100; i++) {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // // broadcastMessage(SerialTest.infoBureau);
        // }
        // }
        // }).start();
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        serialTest.broadcaster.add(eventOutput);
        return eventOutput;
    }
}
