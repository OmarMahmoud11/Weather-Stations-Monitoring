package org.example;
import java.util.Random;

public class RequestGenerator {
    private static final Random random = new Random();
    private static long serialNumber = 1;

    public static String generateRandomRequest() {
        // Randomly decide between write (w) and read (r)
//        boolean isWrite = random.nextBoolean();
        boolean isWrite = true;
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

            String jsonMessage = String.format(
                    "{\"s_no\": %d, \"battery_status\": \"%s\", \"status_timestamp\": %d, \"weather\": {\"humidity\": %d, \"temperature\": %d, \"wind_speed\": %d}}",
                    serialNumber++, batteryStatus, timestamp, humidity, temperature, windSpeed
            );


            return key + " " + jsonMessage;
        } else {
            return "r " + key;
        }
    }
}
