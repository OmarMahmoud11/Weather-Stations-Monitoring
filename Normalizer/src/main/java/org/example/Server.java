package org.example;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            for (int i = 1; i <= 1000; i++) {
                String request = RequestGenerator.generateRandomRequest();  // Simulated data
                System.out.println(request);
                writer.println(request);
            }

            System.out.println("All requests sent from converter.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
