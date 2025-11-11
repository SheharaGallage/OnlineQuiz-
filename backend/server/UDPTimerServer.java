package server;

import java.net.*;
import java.io.IOException;

public class UDPTimerServer implements Runnable {
    private static final int UDP_PORT = 9999;
    private DatagramSocket socket;
    private boolean isRunning = false;
    private int timeRemaining = 0;

    public UDPTimerServer() {
        try {
            socket = new DatagramSocket();
            System.out.println("UDP Timer Server started on port " + UDP_PORT);
        } catch (Exception e) {
            System.err.println("UDP Timer error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            try {
                if (timeRemaining > 0) {
                    broadcastTime();
                    timeRemaining--;
                    Thread.sleep(1000); // Wait 1 second
                } else {
                    Thread.sleep(100); // Idle wait
                }
            } catch (InterruptedException e) {
                System.err.println("Timer interrupted: " + e.getMessage());
            }
        }
    }

    public void startTimer(int duration) {
        this.timeRemaining = duration;
        System.out.println("Timer started: " + duration + " seconds");
    }

    private void broadcastTime() {
        try {
            String timerData = "{\"type\":\"TIMER_UPDATE\",\"timeRemaining\":" + timeRemaining + "}";

            byte[] buffer = timerData.getBytes();

            // Broadcast to localhost
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, UDP_PORT);
            socket.send(packet);

            System.out.println("Timer broadcast: " + timeRemaining + " seconds");
        } catch (IOException e) {
            System.err.println("Broadcast error: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        if (socket != null) {
            socket.close();
        }
    }
}







