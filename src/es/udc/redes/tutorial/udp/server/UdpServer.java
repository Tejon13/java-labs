package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements an UDP Echo Server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        try {
            // Create a server socket
            
            // Set max. timeout to 300 secs
            while (true) {
                // Prepare datagram for reception
                
                // Receive the message
                
                // Prepare datagram to send response
                
                // Send response
                
            }
          
        // Uncomment next catch clause after implementing the logic
        //} catch (SocketTimeoutException e) {
        //    System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
// Close the socket
        }
    }
}
