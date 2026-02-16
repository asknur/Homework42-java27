package server;

import models.Client;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler {
    private static final Set<Client> clients = ConcurrentHashMap.newKeySet();

    public static void handle(Socket socket) {
        System.out.printf("Подключен клиент: %s%n", socket);
        Client client = null;

        try (socket;
             Scanner reader = getReader(socket);
             PrintWriter writer = getWriter(socket)) {
            sendResponse("Привет " + socket, writer);
            client = new Client(socket, writer);
            clients.add(client);

            while (true) {
                String message = reader.nextLine();
                System.out.printf("%s: %s%n", socket, message);
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    System.out.printf("Пользователь %s отключился%n", socket.getPort());
                    break;
                }
                broadcast(message, client);
            }

        } catch (NoSuchElementException ex) {
            System.out.println("Клиент закрыл соединение!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                clients.remove(client);
            }
        }
        System.out.printf("Клиент отключен: %s%n", socket);
    }

    private static PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        return new PrintWriter(os);
    }

    private static Scanner getReader(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
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

    private static void broadcast(String message, Client sender) {
        String messageWithName = sender.getName().strip() + ": " + message;

        for (Client client : clients) {
            if (client != sender) {
                PrintWriter writer = client.getWriter();
                writer.println(messageWithName);
                writer.flush();

            }
        }
    }

}
