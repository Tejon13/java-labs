package es.udc.redes.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ServerThread extends Thread {

    private Code code;
    private Date date;
    private File file = null;
    private final Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        PrintWriter writer = null;
        BufferedOutputStream dataOut = null;
        String fileRequest = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            dataOut = new BufferedOutputStream(socket.getOutputStream());

            String msg = reader.readLine();
            if (msg != null) {
                StringTokenizer parse = new StringTokenizer(msg);
                String method = parse.nextToken().toUpperCase();
                fileRequest = parse.nextToken().toLowerCase();

                if (!method.equals("GET") && !method.equals("HEAD")) {
                    code = Code.NOT_IMPLEMENTED;
                    printHeader(writer, dataOut, "", fileRequest);
                }
                if (method.equals("GET")) {
                    printHeader(writer, dataOut, "GET", fileRequest);
                }
                if (method.equals("HEAD")) {
                    printHeader(writer, dataOut, "HEAD", fileRequest);
                }
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (dataOut != null) {
                    dataOut.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void printHeader(PrintWriter printer, OutputStream dataOut, String metodo, String content) throws IOException {
        if (code == Code.NOT_IMPLEMENTED) {
            file = new File("resources/codes/501.html");
        } else {
            switch (content) {
                case "/fic.png":
                    code = Code.OK;
                    file = new File("resources/archives/fic.png");
                    break;
                case "/index.html":
                    code = Code.OK;
                    file = new File("resources/archives/index.html");
                    break;
                case "/license.txt":
                    code = Code.OK;
                    file = new File("resources/archives/LICENSE.txt");
                    break;
                case "/saludo.html":
                    code = Code.OK;
                    file = new File("resources/archives/saludo.html");
                    break;
                case "/udc.gif":
                    code = Code.OK;
                    file = new File("resources/archives/udc.gif");
                    break;
                default:
                    code = Code.NOT_FOUND;
                    file = new File("resources/codes/404.html");
                    break;
            }
        }

        date = new Date();
        int length = (int) file.length();
        byte[] data = readerData(file, length);

        printer.println("HTTP/1.0 " + code.getEstado());
        printer.println("Date: " + date);
        printer.println("Server: ");
        printer.println("Last-Modified: " + formatDate((int) file.lastModified()));
        printer.println("Content-Length: " + file.length());
        printer.println("Content-Type: " + getType(file));
        printer.println("");
        printer.flush();

        if (code == Code.OK) {
            switch (metodo) {
                case "GET":
                    dataOut.write(data, 0, length);
                    break;
                case "HEAD":
                    break;
            }
        } else {
            dataOut.write(data, 0, length);
        }
        dataOut.flush();
    }

    private String formatDate(int seconds) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        return simpleDate.format(seconds);
    }

    private byte[] readerData(File file, int lentgh) throws IOException {
        FileInputStream input = null;
        byte[] data = new byte[lentgh];

        try {
            input = new FileInputStream(file);
            input.read(data);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return data;
    }

    private String getType(File fileRequest) {
        String requested = fileRequest.getName();
        String type = "application/octet-stream";

        if (requested.endsWith(".htm") || requested.endsWith(".html")) {
            type = "text/html";
        }
        if (requested.endsWith(".log") || requested.endsWith(".txt")) {
            type = "text/plain";
        }
        if (requested.endsWith(".gif")) {
            type = "image/gif";
        }
        if (requested.endsWith(".png")) {
            type = "image/png";
        }
        return type;
    }

}