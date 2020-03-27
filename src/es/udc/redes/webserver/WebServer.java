package es.udc.redes.webserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;

/**
 * This class manage the server's configuration.
 *
 * @author Íker
 */
public class WebServer {

    static int PORT;
    static String DIRECTORY;
    static String DIRECTORY_INDEX;
    static boolean ALLOW;

    /**
     * Initializes de server and the socket.
     *
     * @param argv - arguments [no arguments needed]
     */
    public static void main(String argv[]) {

        InputStream input = null;
        ServerSocket socketServer = null;
        Properties config = new Properties();

        try {
            input = new FileInputStream("resources/config.properties");
            config.load(input);
            PORT = Integer.parseInt(config.getProperty("PORT"));
            DIRECTORY = config.getProperty("DIRECTORY");
            DIRECTORY_INDEX = config.getProperty("DIRECTORY_INDEX");
            ALLOW = Boolean.valueOf(config.getProperty("ALLOW"));

            socketServer = new ServerSocket(PORT);
            socketServer.setSoTimeout(300000);
            while (true) {
                Socket socketClient = socketServer.accept();
                ServerThread serverThread = new ServerThread(socketClient, ALLOW);
                serverThread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
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