package es.udc.redes.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * This class manage the server's Threads.
 *
 * @author Íker
 */
public class ServerThread extends Thread {

    private Code code;
    private Date date;
    private File file = null;
    private final Socket socket;
    private final Boolean permiso;

    public ServerThread(Socket s, Boolean a) {
        this.socket = s;
        this.permiso = a;
    }

    /**
     * Executes the echo server connection.
     */
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
                    printHeader(writer, dataOut, method, fileRequest);
                }
                if (method.equals("HEAD")) {
                    printHeader(writer, dataOut, method, fileRequest);
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

    /**
     * Prints all HTTP Headers.
     *
     * @param printer - object that prints the Header
     * @param dataOut - object that sends the file to the server
     * @param metodo - HTTP method
     * @param content - file requested by the server
     * @throws IOException
     */
    private void printHeader(PrintWriter printer, OutputStream dataOut, String metodo, String content) throws IOException {
        File requested = new File("resources/archives" + content);

        if (code == Code.NOT_IMPLEMENTED) {
            file = new File("resources/codes/501.html");
        } else {
            if (requested.exists()) {
                code = Code.OK;
                file = new File("resources/archives" + content);
            } else {
                code = Code.NOT_FOUND;
                file = new File("resources/codes/404.html");
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

        activity(metodo, content, length);
    }

    /**
     * Records server's activity in it's corresponding .log.
     *
     * @param metodo - HTTP method
     * @param content - file requested by the server
     * @param length - file's length
     */
    private void activity(String metodo, String content, int length) {
        File access = new File("resources/access/access.log");
        File errors = new File("resources/errors/errors.log");
        PrintWriter registrar;

        try {
            switch (code.getEstado().charAt(0)) {
                case '2':
                    registrar = new PrintWriter(new FileOutputStream(access, true), true);

                    registrar.println("------ A C C E S S ------");
                    registrar.println("Petición de acceso: " + metodo + " " + content);
                    registrar.println("IP cliente: " + socket.getInetAddress().toString());
                    registrar.println("Fecha y hora de petición: " + date);
                    registrar.println("Código de estado: " + code);
                    registrar.println("Tamaño: " + length);
                    registrar.println("");
                    registrar.println("-------------------------");
                    registrar.println("");
                    break;
                case '3':
                    registrar = new PrintWriter(new FileOutputStream(access, true), true);

                    registrar.println("------ A C C E S S ------");
                    registrar.println("Petición de acceso: " + metodo + " " + content);
                    registrar.println("IP cliente: " + socket.getInetAddress().toString());
                    registrar.println("Fecha y hora de petición: " + date);
                    registrar.println("Código de estado: " + code);
                    registrar.println("Tamaño: " + length);
                    registrar.println("");
                    registrar.println("-------------------------");
                    registrar.println("");
                    break;
                case '4':
                    registrar = new PrintWriter(new FileOutputStream(errors, true), true);

                    registrar.println("------- E R R O R -------");
                    registrar.println("Petición errónea: " + metodo + " " + content);
                    registrar.println("IP cliente: " + socket.getInetAddress().toString());
                    registrar.println("Fecha y hora de error: " + date);
                    registrar.println("Código de error: " + code);
                    registrar.println("");
                    registrar.println("-------------------------");
                    registrar.println("");
                    break;
                case '5':
                    registrar = new PrintWriter(new FileOutputStream(errors, true), true);

                    registrar.println("------- E R R O R -------");
                    registrar.println("Petición errónea: " + metodo + " " + content);
                    registrar.println("IP cliente: " + socket.getInetAddress().toString());
                    registrar.println("Fecha y hora de error: " + date);
                    registrar.println("Código de error: " + code);
                    registrar.println("");
                    registrar.println("-------------------------");
                    registrar.println("");
                    break;
            }
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Gives appropriate format to the object new Date().
     *
     * @param seconds - time to format
     * @return simpleDate.format(seconds) - formatted time
     */
    private String formatDate(int seconds) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        return simpleDate.format(seconds);
    }

    /**
     * Reads data from a file to send later to the server.
     *
     * @param file - file to read
     * @param lentgh - file's lentgh
     * @return data - array with all file's data
     * @throws IOException
     */
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

    /**
     * Type's getter which server's requested for.
     *
     * @param fileRequest - file to get the type
     * @return type - String with file's type
     */
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