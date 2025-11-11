package server;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String userId;
    private UDPTimerServer timerServer;
    private NIOFileHandler fileHandler;
    private AuthenticationServer authServer;

    public ClientHandler(Socket socket, UDPTimerServer timerServer) {
        this.socket = socket;
        this.timerServer = timerServer;
        this.fileHandler = new NIOFileHandler();
        this.authServer = new AuthenticationServer();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Handle WebSocket handshake
            if (!handleWebSocketHandshake()) {
                return;
            }

            String message;
            while ((message = readWebSocketMessage()) != null) {
                if (message.isEmpty())
                    continue;
                handleMessage(message);
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private boolean handleWebSocketHandshake() throws IOException {
        String line;
        String key = null;

        // Read HTTP headers
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Sec-WebSocket-Key:")) {
                key = line.substring("Sec-WebSocket-Key:".length()).trim();
            }
        }

        if (key == null) {
            System.out.println("Not a WebSocket connection, closing");
            return false;
        }

        // Send WebSocket handshake response
        String accept = generateWebSocketAccept(key);
        out.print("HTTP/1.1 101 Switching Protocols\r\n");
        out.print("Upgrade: websocket\r\n");
        out.print("Connection: Upgrade\r\n");
        out.print("Sec-WebSocket-Accept: " + accept + "\r\n");
        out.print("\r\n");
        out.flush();

        System.out.println("WebSocket handshake completed");
        return true;
    }

    private String generateWebSocketAccept(String key) {
        try {
            String magic = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            String combined = key + magic;
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(combined.getBytes("UTF-8"));
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String readWebSocketMessage() throws IOException {
        InputStream is = socket.getInputStream();

        // Read first byte (FIN, RSV, opcode)
        int firstByte = is.read();
        if (firstByte == -1)
            return null;

        boolean fin = (firstByte & 0x80) != 0;
        int opcode = firstByte & 0x0F;

        // Opcode 8 = close, return null
        if (opcode == 8)
            return null;
        // Opcode 9 = ping, skip
        if (opcode == 9)
            return "";

        // Read second byte (MASK, payload length)
        int secondByte = is.read();
        if (secondByte == -1)
            return null;

        boolean masked = (secondByte & 0x80) != 0;
        long payloadLength = secondByte & 0x7F;

        // Extended payload length
        if (payloadLength == 126) {
            payloadLength = (is.read() << 8) | is.read();
        } else if (payloadLength == 127) {
            payloadLength = 0;
            for (int i = 0; i < 8; i++) {
                payloadLength = (payloadLength << 8) | is.read();
            }
        }

        // Read masking key
        byte[] maskingKey = new byte[4];
        if (masked) {
            is.read(maskingKey);
        }

        // Read payload
        byte[] payload = new byte[(int) payloadLength];
        is.read(payload);

        // Unmask payload
        if (masked) {
            for (int i = 0; i < payload.length; i++) {
                payload[i] = (byte) (payload[i] ^ maskingKey[i % 4]);
            }
        }

        return new String(payload, "UTF-8");
    }

    private void sendWebSocketMessage(String message) {
        try {
            OutputStream os = socket.getOutputStream();
            byte[] payload = message.getBytes("UTF-8");

            // First byte: FIN=1, opcode=1 (text)
            os.write(0x81);

            // Second byte: MASK=0, payload length
            if (payload.length <= 125) {
                os.write(payload.length);
            } else if (payload.length <= 65535) {
                os.write(126);
                os.write((payload.length >> 8) & 0xFF);
                os.write(payload.length & 0xFF);
            } else {
                os.write(127);
                for (int i = 7; i >= 0; i--) {
                    os.write((int) ((payload.length >> (8 * i)) & 0xFF));
                }
            }

            // Write payload
            os.write(payload);
            os.flush();
        } catch (IOException e) {
            System.err.println("Error sending WebSocket message: " + e.getMessage());
        }
    }

    private void handleMessage(String message) {
        try {
            SimpleJSON.JSONObject json = SimpleJSON.parseObject(message);
            String type = json.getString("type");

            System.out.println("Received: " + type);

            switch (type) {
                case "LOGIN":
                    handleLogin(json);
                    break;
                case "GET_QUIZ":
                    handleGetQuiz(json);
                    break;
                case "SUBMIT_ANSWERS":
                    handleSubmitAnswers(json);
                    break;
                case "START_TIMER":
                    handleStartTimer(json);
                    break;
            }
        } catch (Exception e) {
            sendError("Error processing request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogin(SimpleJSON.JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");

        // Member 5: Authentication
        if (authServer.authenticate(username, password)) {
            userId = username + "_" + System.currentTimeMillis();
            QuizServer.addClient(userId, this);

            String response = "{\"type\":\"LOGIN_SUCCESS\",\"userId\":\"" + userId +
                    "\",\"username\":\"" + username + "\"}";
            sendMessage(response);

            System.out.println("User logged in: " + username);
        } else {
            String response = "{\"type\":\"LOGIN_FAILED\",\"message\":\"Invalid credentials\"}";
            sendMessage(response);
        }
    }

    private void handleGetQuiz(SimpleJSON.JSONObject data) {
        try {
            // Member 4: Load quiz using NIO
            String quizData = fileHandler.loadQuizQuestions();

            String response = "{\"type\":\"QUIZ_DATA\",\"questions\":" + quizData + "}";
            sendMessage(response);

            System.out.println("Quiz sent to user: " + userId);
        } catch (IOException e) {
            sendError("Error loading quiz: " + e.getMessage());
        }
    }

    private void handleSubmitAnswers(SimpleJSON.JSONObject data) {
        try {
            String answersStr = data.getString("answers");
            String[] answersParts = answersStr.replace("[", "").replace("]", "").split(",");
            int[] answers = new int[answersParts.length];
            for (int i = 0; i < answersParts.length; i++) {
                answers[i] = Integer.parseInt(answersParts[i].trim());
            }

            // Calculate score
            String quizData = fileHandler.loadQuizQuestions();
            String[] questions = parseQuestions(quizData);

            int score = 0;
            for (int i = 0; i < answers.length && i < questions.length; i++) {
                int correctAnswer = getCorrectAnswer(questions[i]);
                if (answers[i] == correctAnswer) {
                    score++;
                }
            }

            // Save result using NIO
            fileHandler.saveQuizResult(userId, score, questions.length);

            // Send result back
            double percentage = (score * 100.0 / questions.length);
            String response = "{\"type\":\"RESULTS\",\"score\":" + score +
                    ",\"total\":" + questions.length +
                    ",\"percentage\":" + percentage + "}";
            sendMessage(response);

            System.out.println("Quiz submitted by " + userId + " - Score: " + score + "/" + questions.length);
        } catch (Exception e) {
            sendError("Error processing answers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String[] parseQuestions(String jsonArray) {
        jsonArray = jsonArray.trim();
        if (jsonArray.startsWith("[")) {
            jsonArray = jsonArray.substring(1);
        }
        if (jsonArray.endsWith("]")) {
            jsonArray = jsonArray.substring(0, jsonArray.length() - 1);
        }

        java.util.List<String> questions = new java.util.ArrayList<>();
        int braceCount = 0;
        StringBuilder current = new StringBuilder();

        for (char c : jsonArray.toCharArray()) {
            if (c == '{')
                braceCount++;
            if (c == '}')
                braceCount--;

            current.append(c);

            if (braceCount == 0 && current.length() > 0) {
                String q = current.toString().trim();
                if (q.length() > 0 && !q.equals(",")) {
                    questions.add(q.replace(",", ""));
                }
                current = new StringBuilder();
            }
        }

        return questions.toArray(new String[0]);
    }

    private int getCorrectAnswer(String questionJson) {
        int startIdx = questionJson.indexOf("\"correctAnswer\":");
        if (startIdx == -1)
            return 0;

        startIdx += "\"correctAnswer\":".length();
        int endIdx = questionJson.indexOf(",", startIdx);
        if (endIdx == -1) {
            endIdx = questionJson.indexOf("}", startIdx);
        }

        String answerStr = questionJson.substring(startIdx, endIdx).trim();
        return Integer.parseInt(answerStr);
    }

    private void handleStartTimer(SimpleJSON.JSONObject data) {
        int duration = Integer.parseInt(data.getString("duration"));
        timerServer.startTimer(duration);
    }

    public void sendMessage(String message) {
        sendWebSocketMessage(message);
    }

    private void sendError(String error) {
        String response = "{\"type\":\"ERROR\",\"message\":\"" + error + "\"}";
        sendMessage(response);
    }

    private void cleanup() {
        try {
            if (userId != null) {
                QuizServer.removeClient(userId);
            }
            socket.close();
            System.out.println("Client disconnected: " + userId);
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}
