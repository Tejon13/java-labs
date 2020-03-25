package es.udc.redes.webserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;

public class WebServer {

    static String dir;
    static String index;
    static boolean allow;

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }

        InputStream input = null;
        ServerSocket socketServer = null;
        int port = Integer.parseInt(argv[0]);
        Properties config = new Properties();

        try {
            input = new FileInputStream("resources/config.properties");
            config.load(input);
            dir = config.getProperty("dir");
            index = config.getProperty("index");
            allow = Boolean.valueOf(config.getProperty("allow"));
            socketServer = new ServerSocket(port);
            socketServer.setSoTimeout(300000);
            while (true) {
                Socket socketClient = socketServer.accept();
                ServerThread serverThread = new ServerThread(socketClient);
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