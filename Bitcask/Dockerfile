# Use an official OpenJDK image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the server JAR and the input graph file into the container
COPY target/Bitcask-1.0-SNAPSHOT.jar bitcask.jar
COPY src/main/resources/system.properties .

# Set the command to run the server JAR with the input graph file
CMD ["java", "-jar", "bitcask.jar"]
