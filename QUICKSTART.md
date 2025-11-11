# 🚀 QUICK START GUIDE

## For Windows Users - Super Easy!

### Method 1: Using Batch Files (Easiest)

1. **Compile the code:**

   - Double-click `compile.bat`
   - Wait for "Compilation completed successfully!"

2. **Start the server:**

   - Double-click `run-server.bat`
   - Wait for "Server listening on port 8080"

3. **Open the quiz:**

   - Go to `frontend` folder
   - Double-click `index.html`

4. **Login:**
   - Username: `student1`
   - Password: `pass123`

**That's it! You're done! 🎉**

---

### Method 2: Using PowerShell/Terminal

```powershell
# Step 1: Compile
cd backend
javac server/*.java

# Step 2: Run server
java server.QuizServer
```

Then open `frontend/index.html` in your browser.

---

## 📝 Testing Checklist

- [ ] Server shows "Server listening on port 8080" ✅
- [ ] Can login with student1/pass123 ✅
- [ ] Quiz loads 5 questions ✅
- [ ] Timer shows 05:00 and counts down ✅
- [ ] Can select answers ✅
- [ ] Can submit quiz ✅
- [ ] Results page shows score ✅

---

## 🆘 Problems?

### Server won't start?

```powershell
# Kill any process using port 8080
netstat -ano | findstr :8080
taskkill /PID <process_id> /F
```

### Frontend can't connect?

- Make sure server is running
- Try opening in different browser
- Check browser console (F12) for errors

### Compilation errors?

- Make sure you have Java JDK 11 or higher
- Check: `java -version`

---

## 👥 Test Accounts

| Username | Password |
| -------- | -------- |
| student1 | pass123  |
| student2 | pass123  |
| student3 | pass123  |
| student4 | pass123  |
| student5 | pass123  |

---

## 📊 Network Concepts Demonstrated

✅ **TCP Socket Server** - Port 8080 for client connections  
✅ **Multi-threading** - Multiple students can take quiz simultaneously  
✅ **UDP Broadcasting** - Timer updates broadcast every second  
✅ **NIO File I/O** - Efficient loading/saving of quiz data  
✅ **Authentication** - User login and session management

---

**For detailed instructions, see README.md**
