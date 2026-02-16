package models;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter printWriter;
    private String name;
    private static int n = 1;

    public Client(Socket socket, PrintWriter printWriter) {
        this.socket = socket;
        this.printWriter = printWriter;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return printWriter;
    }

    public String getName() {
        return "Client" + n++;
    }
}
