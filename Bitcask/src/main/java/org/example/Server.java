package org.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Server {
    private static void handleClient(Socket socket, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<byte[]>> futures = new ArrayList<>();
        List<byte[]> results = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            int k = 1;

            while ((message = reader.readLine()) != null) {
                if (message.isEmpty()) continue;

                if (message.startsWith("w ") || message.startsWith("r ")) {
                    Future<byte[]> future = executor.submit(new Worker(message));
                    futures.add(future);

                    if (k % 300 == 0) {
                        Future<byte[]> controlFuture = executor.submit(new Worker("c"));
                        futures.add(controlFuture);
                    }

                    k++;
                } else {
                    System.err.println("Unrecognized message format: " + message);
                }
            }

            for (Future<byte[]> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (byte[] result : results) {
                if (result != null) {
                    System.out.println(new String(result));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try { socket.close(); } catch (Exception ignored) {}
        }
    }


    public static void main(String args[]) throws IOException {
        Properties config = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/system.properties");
        config.load(fis);

        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        String logsPath = config.getProperty("server.logs");
        String hintPath = config.getProperty("server.hints");
        int numThreads = Integer.parseInt(config.getProperty("server.threads"));

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Bitcask Server started. Listening on port " + serverPort);
            new Worker(0, logsPath, hintPath);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket, numThreads)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
