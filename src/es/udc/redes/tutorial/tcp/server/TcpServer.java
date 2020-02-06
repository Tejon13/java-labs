package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * Monothread TCP echo server.
 */
public class TcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }
        try {
            // Create a server socket
            
            // Set a timeout of 300 secs
            
            while (true) {
                // Wait for connections
                
                // Set the input channel
                
                // Set the output channel
                
                // Receive the client message
                
                // Send response to the client

                // Close the streams
            }
        // Uncomment next catch clause after implementing the logic            
        //} catch (SocketTimeoutException e) {
        //    System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
//Close the socket
        }
    }
}
