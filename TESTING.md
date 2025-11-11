# 🧪 TESTING GUIDE - Quiz System

## ✅ Complete Testing Checklist

---

## Phase 1: Server Testing

### Test 1.1: Compilation

- [ ] Navigate to `backend` folder
- [ ] Run: `javac server/*.java`
- [ ] **Expected:** No error messages
- [ ] **Expected:** `.class` files created in `server` folder
- [ ] **Screenshot:** Command output showing successful compilation

### Test 1.2: Server Startup

- [ ] Run: `java -cp . server.QuizServer`
- [ ] **Expected Output:**
  ```
  === Quiz Server Starting ===
  UDP Timer Server started on port 9999
  Server listening on port 8080
  Waiting for clients...
  ```
- [ ] **Screenshot:** Server console with startup messages

---

## Phase 2: Frontend Testing

### Test 2.1: Login Page

- [ ] Open `frontend/index.html` in browser
- [ ] **Expected:** Login form displays with username/password fields
- [ ] **Screenshot:** Login page

### Test 2.2: Valid Login

- [ ] Enter username: `student1`
- [ ] Enter password: `pass123`
- [ ] Click "Login" button
- [ ] **Expected:** "Login successful! Redirecting..." message
- [ ] **Expected:** Redirect to quiz.html
- [ ] **Screenshot:** Successful login message

### Test 2.3: Invalid Login

- [ ] Enter username: `wronguser`
- [ ] Enter password: `wrongpass`
- [ ] Click "Login" button
- [ ] **Expected:** "Login failed: Invalid credentials" error message
- [ ] **Expected:** Stay on login page
- [ ] **Screenshot:** Error message

### Test 2.4: Server Connection (check server console)

- [ ] After login attempt
- [ ] **Server Console Expected:**
  ```
  New client connected: /127.0.0.1
  Received: LOGIN
  User logged in: student1
  ```
- [ ] **Screenshot:** Server console showing connection

---

## Phase 3: Quiz Functionality

### Test 3.1: Quiz Start

- [ ] On quiz.html, click "Start Quiz" button
- [ ] **Expected:** Quiz questions appear (5 questions)
- [ ] **Expected:** Timer shows "05:00"
- [ ] **Screenshot:** Quiz questions displayed

### Test 3.2: Timer Countdown (Member 3: UDP)

- [ ] Wait and observe timer
- [ ] **Expected:** Timer counts down: 05:00, 04:59, 04:58...
- [ ] **Server Console Expected:** (every second)
  ```
  Timer broadcast: 299 seconds
  Timer broadcast: 298 seconds
  ```
- [ ] **Screenshot:** Timer showing countdown

### Test 3.3: Answer Selection

- [ ] Click radio buttons to select answers for each question
- [ ] **Expected:** Radio button highlights selected answer
- [ ] **Browser Console:** (F12 → Console)
  ```
  ✏️ Answer saved for question 1 : 1
  💾 Answers auto-saved
  ```
- [ ] **Screenshot:** Answered questions

### Test 3.4: Submit Quiz

- [ ] Click "Submit Answers" button
- [ ] **Expected:** Redirect to results.html
- [ ] **Server Console Expected:**
  ```
  Quiz submitted by student1_xxxxx - Score: X/5
  ```
- [ ] **Screenshot:** Server console with submission message

---

## Phase 4: Results Display

### Test 4.1: Results Page

- [ ] On results.html
- [ ] **Expected:** Score displayed (e.g., "3/5")
- [ ] **Expected:** Percentage shown (e.g., "60%")
- [ ] **Expected:** Username displayed
- [ ] **Expected:** Date/time shown
- [ ] **Screenshot:** Results page with score

### Test 4.2: Results Storage (Member 4: NIO)

- [ ] Navigate to `backend/data/results.txt`
- [ ] **Expected:** File exists
- [ ] **Expected:** Contains entry like:
  ```
  2025-11-11T15:30:45 | User: student1_xxxxx | Score: 3/5
  ```
- [ ] **Screenshot:** results.txt file content

---

## Phase 5: Multi-threading Test (Member 2)

### Test 5.1: Multiple Simultaneous Users

- [ ] Open 3 different browser windows/tabs
- [ ] Login with different users:
  - Window 1: student1 / pass123
  - Window 2: student2 / pass123
  - Window 3: student3 / pass123
- [ ] **Server Console Expected:**
  ```
  New client connected: /127.0.0.1
  User logged in: student1
  New client connected: /127.0.0.1
  User logged in: student2
  New client connected: /127.0.0.1
  User logged in: student3
  ```
- [ ] **Screenshot:** Server console showing multiple connections

### Test 5.2: Concurrent Quiz Taking

- [ ] All 3 users start quiz simultaneously
- [ ] All 3 users submit answers
- [ ] **Expected:** All get their results without errors
- [ ] **Screenshot:** 3 browser windows with different scores

---

## Phase 6: Data Files Test (Member 4: NIO)

### Test 6.1: Questions File

- [ ] Check `backend/data/questions.json`
- [ ] **Expected:** File exists with 5 questions in JSON format
- [ ] **Screenshot:** questions.json content

### Test 6.2: Users File

- [ ] Check `backend/data/users.txt`
- [ ] **Expected:** File exists with format:
  ```
  student1:pass123
  student2:pass123
  ...
  ```
- [ ] **Screenshot:** users.txt content

### Test 6.3: File Auto-Creation

- [ ] Delete `backend/data` folder
- [ ] Restart server
- [ ] **Expected:** Server recreates `data` folder
- [ ] **Expected:** Server recreates default questions and users
- [ ] **Screenshot:** Server console showing file creation

---

## Phase 7: LocalStorage Test (Frontend)

### Test 7.1: Session Storage

- [ ] Login successfully
- [ ] Open browser DevTools (F12) → Application → Local Storage
- [ ] **Expected Keys:**
  - `userId`
  - `username`
  - `loginTime`
- [ ] **Screenshot:** LocalStorage with session data

### Test 7.2: Quiz Cache

- [ ] Start quiz
- [ ] Check LocalStorage
- [ ] **Expected:** `currentQuiz` key with quiz data
- [ ] **Screenshot:** LocalStorage with cached quiz

### Test 7.3: Auto-save Answers

- [ ] Answer some questions
- [ ] Check LocalStorage
- [ ] **Expected:** `savedAnswers` key with answers array
- [ ] **Screenshot:** LocalStorage with saved answers

---

## Phase 8: Network Concepts Verification

### Member 1: TCP Socket Server ✅

- **File:** `QuizServer.java`
- **Test:** Server accepts client connections
- **Verify:**
  ```
  ServerSocket serverSocket = new ServerSocket(PORT)  // Line 19
  Socket clientSocket = serverSocket.accept()          // Line 25
  ```
- [ ] **Screenshot:** Server console showing "Server listening on port 8080"

### Member 2: Multi-threading ✅

- **File:** `ClientHandler.java`
- **Test:** Multiple clients handled simultaneously
- **Verify:**
  ```
  ExecutorService threadPool = Executors.newFixedThreadPool(10)  // Line 11
  threadPool.execute(handler)                                     // Line 29
  ```
- [ ] **Screenshot:** Server console showing 3+ concurrent clients

### Member 3: UDP Broadcasting ✅

- **File:** `UDPTimerServer.java`
- **Test:** Timer updates broadcast every second
- **Verify:**
  ```
  DatagramSocket socket = new DatagramSocket()        // Line 13
  socket.send(packet)                                 // Line 52
  ```
- [ ] **Screenshot:** Server console showing timer broadcasts

### Member 4: NIO File Operations ✅

- **File:** `NIOFileHandler.java`
- **Test:** Files read/written using NIO
- **Verify:**
  ```
  Files.readString(path, StandardCharsets.UTF_8)      // Line 19
  Files.writeString(path, result, ...)                // Line 31
  ```
- [ ] **Screenshot:** Data files (questions.json, results.txt)

### Member 5: Authentication Protocol ✅

- **File:** `AuthenticationServer.java`
- **Test:** User login authentication
- **Verify:**
  ```
  public boolean authenticate(String username, String password)  // Line 36
  return storedPassword != null && storedPassword.equals(...)    // Line 38
  ```
- [ ] **Screenshot:** Successful and failed login attempts

---

## Phase 9: Error Handling

### Test 9.1: Server Down

- [ ] Stop server (Ctrl+C)
- [ ] Try to login from browser
- [ ] **Expected:** "Cannot connect to server" error
- [ ] **Screenshot:** Connection error message

### Test 9.2: Partial Answers

- [ ] Start quiz
- [ ] Answer only 2 out of 5 questions
- [ ] Click Submit
- [ ] **Expected:** Confirmation dialog "You have 3 unanswered question(s)"
- [ ] **Screenshot:** Confirmation dialog

### Test 9.3: Time Expiration

- [ ] Start quiz
- [ ] Wait for timer to reach 00:00 (or modify timer to 10 seconds for quick test)
- [ ] **Expected:** Alert "Time is up! Submitting your answers..."
- [ ] **Expected:** Auto-submit and redirect to results
- [ ] **Screenshot:** Time up alert

---

## Phase 10: Performance Testing

### Test 10.1: Rapid Requests

- [ ] Login and start quiz quickly
- [ ] Submit immediately
- [ ] Repeat 5 times
- [ ] **Expected:** No errors, all requests handled
- [ ] **Screenshot:** Server console showing all requests

### Test 10.2: Large File I/O (Member 4)

- [ ] Add 100 questions to questions.json
- [ ] Request quiz
- [ ] **Expected:** All questions load successfully
- [ ] **Screenshot:** Quiz with many questions

---

## 📊 Testing Summary Report

| Test Category    | Total Tests | Passed  | Failed  |
| ---------------- | ----------- | ------- | ------- |
| Server Setup     | 2           | [ ]     | [ ]     |
| Frontend UI      | 4           | [ ]     | [ ]     |
| Quiz Functions   | 4           | [ ]     | [ ]     |
| Results          | 2           | [ ]     | [ ]     |
| Multi-threading  | 2           | [ ]     | [ ]     |
| Data Files       | 3           | [ ]     | [ ]     |
| LocalStorage     | 3           | [ ]     | [ ]     |
| Network Concepts | 5           | [ ]     | [ ]     |
| Error Handling   | 3           | [ ]     | [ ]     |
| Performance      | 2           | [ ]     | [ ]     |
| **TOTAL**        | **30**      | **[ ]** | **[ ]** |

---

## 📸 Required Screenshots for Report

1. ✅ Server startup console
2. ✅ Login page
3. ✅ Successful login
4. ✅ Quiz questions display
5. ✅ Timer countdown
6. ✅ Results page with score
7. ✅ Multiple browser windows (multi-threading demo)
8. ✅ Server console with multiple client connections
9. ✅ Server console with timer broadcasts
10. ✅ Data files (questions.json, users.txt, results.txt)
11. ✅ Browser LocalStorage
12. ✅ Code snippets showing network concepts

---

## 🎯 Success Criteria

Your project passes if:

- ✅ All 5 network concepts are demonstrated
- ✅ Server handles multiple clients simultaneously
- ✅ Timer updates broadcast correctly
- ✅ Files are read/written using NIO
- ✅ Authentication works correctly
- ✅ Quiz completes end-to-end without errors
- ✅ Results are stored persistently

---

## 🐛 Common Issues & Solutions

| Issue                 | Solution                                                     |
| --------------------- | ------------------------------------------------------------ |
| Port 8080 in use      | `netstat -ano \| findstr :8080` then `taskkill /PID <id> /F` |
| Class not found       | Use `java -cp . server.QuizServer`                           |
| Connection refused    | Make sure server is running first                            |
| Timer not updating    | Check browser console for WebSocket errors                   |
| Questions not loading | Check `backend/data/questions.json` exists                   |

---

**Test thoroughly before submission! Document any issues encountered for the "Challenges and Solutions" section of your report.**
