# Online Quiz System - Network Programming Assignment

## Group 24

---

## 📚 Project Overview

This is a web-based Online Quiz/Examination System that demonstrates Java Network Programming concepts. The system consists of a Java backend server and an HTML/CSS/JavaScript frontend.

---

## 🎯 Network Programming Concepts Implemented

| Member       | Network Concept        | Implementation                                              |
| ------------ | ---------------------- | ----------------------------------------------------------- |
| **Member 1** | TCP Socket Server      | `QuizServer.java` - Main server handling client connections |
| **Member 2** | Multi-threading        | `ClientHandler.java` - Concurrent client request handling   |
| **Member 3** | UDP Broadcasting       | `UDPTimerServer.java` - Real-time timer updates             |
| **Member 4** | NIO (Non-blocking I/O) | `NIOFileHandler.java` - Efficient file operations           |
| **Member 5** | Client-Server Protocol | `AuthenticationServer.java` - User authentication           |

---

## 📁 Project Structure

```
Group24/
├── backend/
│   ├── server/
│   │   ├── QuizServer.java          (Member 1: TCP Socket Server)
│   │   ├── ClientHandler.java       (Member 2: Multi-threading)
│   │   ├── UDPTimerServer.java      (Member 3: UDP Timer)
│   │   ├── NIOFileHandler.java      (Member 4: NIO File operations)
│   │   ├── AuthenticationServer.java (Member 5: Authentication)
│   │   └── SimpleJSON.java          (JSON parser utility)
│   └── data/
│       ├── questions.json           (Auto-generated quiz questions)
│       ├── users.txt                (Auto-generated user credentials)
│       └── results.txt              (Quiz results storage)
│
└── frontend/
    ├── index.html                   (Login page)
    ├── quiz.html                    (Quiz interface)
    ├── results.html                 (Results page)
    ├── css/
    │   └── style.css                (Styling)
    └── js/
        ├── client.js                (WebSocket connection)
        ├── storage.js               (LocalStorage helper)
        └── quiz.js                  (Quiz logic)
```

---

## 🚀 How to Run the Project

### **Step 1: Compile Java Backend**

Open PowerShell in VS Code (Ctrl + `) and run:

```powershell
# Navigate to backend folder
cd backend

# Create bin directory if it doesn't exist
mkdir -Force server

# Compile all Java files
javac server/*.java
```

If successful, you'll see `.class` files created in the `backend/server` folder.

### **Step 2: Start the Server**

```powershell
# Run from backend directory
cd backend

# Start the quiz server
java server.QuizServer
```

You should see output like:

```
=== Quiz Server Starting ===
UDP Timer Server started on port 9999
Server listening on port 8080
Waiting for clients...
```

**Keep this terminal window open!** The server must be running for clients to connect.

### **Step 3: Open Frontend in Browser**

**Option A: Direct File Opening**

1. Navigate to `frontend` folder in File Explorer
2. Double-click `index.html` to open in your default browser

**Option B: Using VS Code Live Server (Recommended)**

1. Install "Live Server" extension in VS Code if not already installed
2. Right-click on `frontend/index.html`
3. Select "Open with Live Server"

### **Step 4: Test the Application**

1. **Login Page:**

   - Username: `student1` (or student2, student3, student4, student5)
   - Password: `pass123`
   - Click "Login"

2. **Quiz Page:**

   - Click "Start Quiz" button
   - Answer the 5 network programming questions
   - Watch the timer countdown (5 minutes)
   - Click "Submit Answers" when done

3. **Results Page:**
   - View your score and percentage
   - Click "Take Quiz Again" or "Logout"

---

## 🧪 Testing Multiple Clients

To test multi-threading (Member 2's concept):

1. Keep the server running
2. Open multiple browser windows/tabs
3. Login with different usernames (student1, student2, etc.)
4. Start quiz in multiple windows simultaneously
5. Check server console - you'll see multiple clients being handled concurrently

---

## 🔧 Troubleshooting

### **Problem: Server won't start - "Address already in use"**

**Solution:**

```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace <PID> with actual process ID)
taskkill /PID <PID> /F

# Then restart the server
```

### **Problem: Frontend can't connect to server**

**Solution:**

1. Verify server is running (check terminal)
2. Make sure server shows "Server listening on port 8080"
3. Open browser console (F12) and check for connection errors
4. Try using `http://localhost` instead of file:// protocol

### **Problem: Questions don't load**

**Solution:**

- The server automatically creates `backend/data/questions.json` on first run
- If missing, check file permissions in `backend/data` folder
- Check server console for error messages

### **Problem: Timer not updating**

**Solution:**

- Timer updates are broadcast via UDP (Member 3's concept)
- Make sure you clicked "Start Quiz" button
- Check browser console for errors

### **Problem: Java compilation errors**

**Solution:**

```powershell
# Make sure you're in the backend directory
cd backend

# Compile with verbose output to see errors
javac -verbose server/*.java

# Check Java version (requires Java 11+)
java -version
```

---

## 📊 Features Demonstrated

### **Network Programming Concepts:**

- ✅ TCP Socket connections (Member 1)
- ✅ Multi-threaded client handling (Member 2)
- ✅ UDP broadcasting for timer (Member 3)
- ✅ NIO for file operations (Member 4)
- ✅ Authentication protocol (Member 5)

### **Application Features:**

- User authentication and session management
- Real-time quiz timer with countdown
- Multiple choice questions
- Instant score calculation
- Results persistence using NIO
- LocalStorage for client-side caching
- Auto-save answers feature
- Responsive web design

---

## 👥 Default User Accounts

| Username | Password |
| -------- | -------- |
| student1 | pass123  |
| student2 | pass123  |
| student3 | pass123  |
| student4 | pass123  |
| student5 | pass123  |
| admin    | admin123 |

---

## 📝 Quiz Questions

The system includes 5 default questions about network programming:

1. What does TCP stand for?
2. Which port does HTTP use by default?
3. What is the main advantage of UDP over TCP?
4. Which Java class is used for socket programming?
5. What does NIO stand for in Java?

---

## 🔍 How to Verify Each Network Concept

### **Member 1: TCP Socket Server**

- File: `QuizServer.java` lines 12-28
- Test: Server starts and listens on port 8080
- Verify: Check server console shows "Server listening on port 8080"

### **Member 2: Multi-threading**

- File: `ClientHandler.java` (entire file)
- Test: Multiple clients connect simultaneously
- Verify: Open 3+ browser tabs, login with different users, check server console shows multiple "New client connected" messages

### **Member 3: UDP Broadcasting**

- File: `UDPTimerServer.java` lines 42-57
- Test: Start quiz and watch timer countdown
- Verify: Server console shows "Timer broadcast: X seconds" messages

### **Member 4: NIO File Operations**

- File: `NIOFileHandler.java` lines 12-35
- Test: Quiz loads questions, results are saved
- Verify: Check `backend/data/results.txt` file after submitting quiz

### **Member 5: Client-Server Authentication**

- File: `AuthenticationServer.java` lines 36-39
- Test: Login with correct/incorrect credentials
- Verify: Success with valid credentials, failure with invalid ones

---

## 📦 Data Storage

### **Server-Side (Backend)**

- **Questions:** `backend/data/questions.json` (JSON format)
- **Users:** `backend/data/users.txt` (plain text, format: username:password)
- **Results:** `backend/data/results.txt` (timestamped results log)

### **Client-Side (LocalStorage)**

- User session (userId, username, loginTime)
- Cached quiz questions
- Auto-saved answers
- Last quiz result

---

## 🎓 Learning Outcomes

This project demonstrates:

1. **Socket Programming:** TCP client-server communication
2. **Concurrency:** Multi-threaded server handling multiple clients
3. **Network Protocols:** Both TCP (reliable) and UDP (fast broadcast)
4. **File I/O:** Modern Java NIO for efficient file operations
5. **Authentication:** Basic client-server security protocol
6. **Web Technologies:** Integration of Java backend with HTML/CSS/JS frontend

---

## 🐛 Known Limitations

1. **WebSocket vs Raw Sockets:** Frontend uses WebSocket API (browser limitation). In a pure Java client, you'd use raw Socket connections.
2. **UDP Reception:** Web browsers can't directly receive UDP packets. In production, you'd use a Java Swing/JavaFX client.
3. **No Database:** Uses file storage for simplicity. Production systems would use a database.
4. **Basic Security:** Passwords stored in plain text (demo only). Production would use hashing.

---

## 📸 Screenshots for Report

### **Recommended Screenshots:**

1. Server console showing startup and client connections
2. Login page with credentials
3. Quiz page with questions displayed
4. Timer countdown in action
5. Results page showing score
6. Multiple browser windows (demonstrating multi-threading)
7. Backend data files (questions.json, results.txt)
8. Server console showing UDP timer broadcasts

---

## 🆘 Need Help?

### **Common Commands:**

**Compile:**

```powershell
cd backend
javac server/*.java
```

**Run Server:**

```powershell
cd backend
java server.QuizServer
```

**Check Ports:**

```powershell
netstat -ano | findstr :8080
```

**View Server Logs:**

- Just watch the terminal where server is running
- All events are logged to console

---

## ✅ Pre-Submission Checklist

Before submitting your assignment:

- [ ] Server compiles without errors
- [ ] Server starts and shows "listening on port 8080"
- [ ] Can login with student credentials
- [ ] Quiz questions load properly
- [ ] Timer counts down correctly
- [ ] Can select and submit answers
- [ ] Results page displays score
- [ ] Multiple clients can connect (test multi-threading)
- [ ] Files created in `backend/data/` folder
- [ ] Screenshots taken for report
- [ ] Each member's contribution documented

---

## 📄 Report Sections (Assignment Requirement)

Your report should include:

1. **Project Title:** Online Quiz/Examination System
2. **Group Members and Individual Contributions:**
   - Member 1: TCP Socket Server implementation
   - Member 2: Multi-threading for concurrent clients
   - Member 3: UDP timer broadcasting
   - Member 4: NIO file operations
   - Member 5: Authentication protocol
3. **System Overview:** Web-based quiz system with Java backend
4. **Network Programming Concepts Used:** (See table above)
5. **Screenshots of Outputs:** (See Screenshots section above)
6. **Challenges Faced and Solutions:** (Document any issues you encountered)
7. **Conclusion:** Summary of learning outcomes

---

## 🎉 Success Criteria

Your project is working correctly if:
✅ Server accepts multiple simultaneous connections
✅ Users can login and take quiz
✅ Timer broadcasts updates every second
✅ Quiz data loads from files
✅ Results save to files
✅ Authentication works correctly

---


