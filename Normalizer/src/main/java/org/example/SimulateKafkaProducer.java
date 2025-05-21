package org.example;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class SimulateKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 1000; i++) {
            String[] request = RequestGenerator.generateRandomRequest().split(" ",2);
            ProducerRecord<String, String> record = new ProducerRecord<>("input-topic", request[0], request[1]);
            producer.send(record);
//            System.out.println("Sent: " + request[0] + " " + request[1]);
        }

        producer.close();
    }
}

