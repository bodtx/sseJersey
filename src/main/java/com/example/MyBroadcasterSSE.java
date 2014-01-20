package com.example;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * Example de broadcast
 */
@Singleton
@Path("broadcast")
public class MyBroadcasterSSE {
	private SseBroadcaster broadcaster = new SseBroadcaster();
	
	@PostConstruct
	void init () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    broadcastMessage(new MyJaxbBean("Alexis",i));
				}
            }
        }).start();
    }
	
	
	private  void broadcastMessage(MyJaxbBean message) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .data(MyJaxbBean.class, message)
            .build();
 
        broadcaster.broadcast(event);
 
        System.out.println( "Message was '" + message + "' broadcast.");
    }
	
	@GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        this.broadcaster.add(eventOutput);
        return eventOutput;
    }
}

