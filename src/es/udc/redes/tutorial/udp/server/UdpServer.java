package es.udc.redes.tutorial.udp.server;

import java.io.IOException;
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

        DatagramSocket datasocket = null;

        int port = Integer.parseInt(argv[0]);

        try {
            // Create a server socket
            datasocket = new DatagramSocket(port);
            // Set max. timeout to 300 secs
            datasocket.setSoTimeout(300000);

            byte[] paquete = new byte[1024];

            while (true) {
                // Prepare datagram for reception
                DatagramPacket rdatapacket = new DatagramPacket(paquete, paquete.length);
                // Receive the message
                datasocket.receive(rdatapacket);
                // Prepare datagram to send response
                DatagramPacket sdatapacket = new DatagramPacket(paquete, paquete.length, rdatapacket.getAddress(), rdatapacket.getPort());
                // Send response
                datasocket.send(sdatapacket);
            }

        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
// Close the socket
            if (datasocket != null) {
                datasocket.close();
            }
        }
    }
}