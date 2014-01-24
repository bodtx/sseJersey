package com.example;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.jvnet.hk2.annotations.Service;

/**
 * @author CER3100441
 * 
 */
@Singleton
@Service
public class SerialTest implements SerialPortEventListener {
    SerialPort serialPort;
    SseBroadcaster broadcaster = new SseBroadcaster();
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {// "/dev/tty.usbserial-A9007UX1", // Mac OS X
    // "/dev/ttyUSB0", // Linux
    "COM4", // Windows
    };

    public SerialTest() {
        initialize();
    }

    public final static InfoBureau infoBureau = new InfoBureau();
    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                Thread.sleep(100);
                String inputLine = input.readLine();
                if (inputLine.contains("OK")) {
                    infoBureau.setHumidity(Float.parseFloat(inputLine.split(",")[2].trim()));
                    infoBureau.setTemperature(Float.parseFloat(inputLine.split(",")[3].trim()));
                    broadcastMessage(infoBureau);

                }
                // FileUtils.writeStringToFile(fileCsv, new Date().getTime() + "," + inputLine + "\n", true);
                // System.out.println(infoBureau.getHumidity() + "/" + infoBureau.getTemperature());
            } catch (Exception e) {
                System.err.println("Erreur " + e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public void broadcastMessage(InfoBureau message) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.mediaType(MediaType.APPLICATION_JSON_TYPE).data(InfoBureau.class, message).build();

        broadcaster.broadcast(event);

        // System.out.println("Message was '" + message + "' broadcast.");
    }

    // public static void main(String[] args) throws Exception {
    //
    // Thread t = new Thread() {
    // public void run() {
    // // the following line will keep this app alive for 1000 seconds,
    // // waiting for events to occur and responding to them (printing incoming messages to console).
    // // try {
    // SerialTest main = new SerialTest();
    // main.initialize();
    // // Thread.sleep(1000000);
    // // } catch (InterruptedException ie) {
    // // }
    // }
    // };
    // t.start();
    // System.out.println("Started");
    // }
}