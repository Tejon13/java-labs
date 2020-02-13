package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * Multithread TCP echo server.
 */
public class MultiTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }

        ServerSocket socketServer = null;

        int port = Integer.parseInt(argv[0]);

        try {
            // Create a server socket
            socketServer = new ServerSocket(port);
            // Set a timeout of 300 secs
            socketServer.setSoTimeout(300000);

            while (true) {
                // Wait for connections
                Socket socketClient = socketServer.accept();
                // Create a ServerThread object, with the new connection as parameter
                ServerThread serverThread = new ServerThread(socketClient);
                // Initiate thread using the start() method
                serverThread.start();
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
//Close the socket
            try {
                if (socketServer != null) {
                    socketServer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}