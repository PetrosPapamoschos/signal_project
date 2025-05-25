package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The {@code TcpOutputStrategy} class implements the {@code OutputStrategy} interface
 * to facilitate health data output via a TCP socket.
 * <p>
 * This class starts a TCP server that listens for a client connection on a specified port.
 * Once a client connects, the output strategy uses a {@link PrintWriter} to send formatted
 * patient data over the network.
 * </p>
 *
 * <p>Example usage:
 * <pre>
 *     OutputStrategy tcpOutput = new TcpOutputStrategy(8080);
 *     tcpOutput.output(1, System.currentTimeMillis(), "HeartRate", "72 bpm");
 * </pre>
 * </p>
 *
 * @see OutputStrategy
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructs a new {@code TcpOutputStrategy} that starts a TCP server on the given port.
     * This constructor immediately starts a new thread to accept a client connection without
     * blocking the main thread.
     *
     * @param port the port number on which the TCP server will listen for incoming connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Outputs the specified patient data as a comma-separated message over the TCP socket.
     * If a client is connected and the output stream is established, the data is sent in the
     * following format: "patientId,timestamp,label,data".
     *
     * @param patientId the unique identifier for the patient
     * @param timestamp the timestamp (in milliseconds since UNIX epoch) when the data was recorded
     * @param label     the label indicating the type of data (e.g., "HeartRate", "BloodPressure")
     * @param data      the data to output, typically represented as a String
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
