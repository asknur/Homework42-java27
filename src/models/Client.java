package models;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter printWriter;

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


}
