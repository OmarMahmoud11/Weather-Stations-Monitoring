package org.example;
import java.util.Random;

public class RequestGenerator {
    private static final Random random = new Random();
    private static long serialNumber = 1;

    public static String generateRandomRequest() {
        // Randomly decide between write (w) and read (r)
        boolean isWrite = random.nextBoolean();
        long key = random.nextInt(100) + 1; // Random key between 1 and 100

        if (isWrite) {
            String batteryStatus = switch (random.nextInt(3)) {
                case 0 -> "low";
                case 1 -> "medium";
                default -> "high";
            };

            long timestamp = System.currentTimeMillis() / 1000; // current Unix time
            int humidity = random.nextInt(101); // 0–100%
            int temperature = random.nextInt(150); // e.g., 0–149°F
            int windSpeed = random.nextInt(50); // 0–49 km/h

            String jsonMessage = "{\n" +
                    "    \"s_no\": " + serialNumber++ + ",\n" +
                    "    \"battery_status\": \"" + batteryStatus + "\",\n" +
                    "    \"status_timestamp\": " + timestamp + ",\n" +
                    "    \"weather\": {\n" +
                    "        \"humidity\": " + humidity + ",\n" +
                    "        \"temperature\": " + temperature + ",\n" +
                    "        \"wind_speed\": " + windSpeed + "\n" +
                    "    }\n" +
                    "}";

            return "w " + key + " " + jsonMessage;
        } else {
            return "r " + key;
        }
    }
}
