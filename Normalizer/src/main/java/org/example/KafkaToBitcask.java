package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class KafkaToBitcask {

    public static void main(String[] args) {
        // Kafka Streams Configuration
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "bitcask-producer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Define Stream Processing
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("input-topic");

        stream.foreach((key, value) -> {
            try {
                Long id = Long.parseLong(key);

                // Send to Bitcask server via TCP
                sendToBitcask(id, value, "localhost", 5000);  // change host/port as needed
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    public static void sendToBitcask(Long key, String value, String host, int port) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)){

            value = value.replace(',',';');
            String request = "w" + " " + key + " " + value + "--no-reply";
            writer.println(request);

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
