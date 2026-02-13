package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят%n", port);
            e.printStackTrace();
        }
    }

    private static void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n", socket);
        try (socket; Scanner reader = getReader(socket); PrintWriter writer = getWriter(socket)) {
            sendResponse("Привет " + socket, writer);
            while (true) {
                String message = reader.nextLine();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    break;
                }
                sendResponse(message.toUpperCase(), writer);
            }
        } catch (NoSuchElementException ex) {
            System.out.println("Клиент закрыл соединение!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Клиент отключен: %s%n", socket);
    }

    private static PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        return new PrintWriter(os);
    }

    private static Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(stream);
        return new Scanner(isr);

    }

    private static boolean isQuitMsg(String message) {
        return "bye".equals(message.toLowerCase());
    }

    private static boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    private static void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }


}
