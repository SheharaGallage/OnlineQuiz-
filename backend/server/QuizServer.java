package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class QuizServer {
    private static final int PORT = 8080;
    private static Map<String, ClientHandler> connectedClients = new ConcurrentHashMap<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static UDPTimerServer timerServer;

    public static void main(String[] args) {
        System.out.println("=== Quiz Server Starting ===");

        // Start UDP timer broadcast (Member 3)
        timerServer = new UDPTimerServer();
        new Thread(timerServer).start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            System.out.println("Waiting for clients...\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Member 2: Multi-threading - handle each client in separate thread
                ClientHandler handler = new ClientHandler(clientSocket, timerServer);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addClient(String userId, ClientHandler handler) {
        connectedClients.put(userId, handler);
    }

    public static void removeClient(String userId) {
        connectedClients.remove(userId);
    }
}
