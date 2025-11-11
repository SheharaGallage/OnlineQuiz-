class QuizClient {
  constructor() {
    this.socket = null;
    this.connected = false;
    this.messageQueue = [];
    this.connect();
  }

  connect() {
    try {
      // Connect to TCP server on port 8080
      this.socket = new WebSocket("ws://localhost:8080");

      this.socket.onopen = () => {
        console.log("✅ Connected to server");
        this.connected = true;

        // Send queued messages
        while (this.messageQueue.length > 0) {
          const msg = this.messageQueue.shift();
          this.socket.send(msg);
        }
      };

      this.socket.onmessage = (event) => {
        this.handleMessage(event.data);
      };

      this.socket.onerror = (error) => {
        console.error("❌ WebSocket error:", error);
        if (typeof showMessage === "function") {
          showMessage(
            "Cannot connect to server. Please ensure the server is running on port 8080.",
            "error"
          );
        }
      };

      this.socket.onclose = () => {
        console.log("🔌 Disconnected from server");
        this.connected = false;
      };
    } catch (error) {
      console.error("Connection error:", error);
    }
  }

  send(data) {
    const message = JSON.stringify(data);

    if (this.connected && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
      console.log("📤 Sent:", data.type);
    } else {
      console.log("⏳ Queued message:", data.type);
      this.messageQueue.push(message);
    }
  }

  login(username, password) {
    const message = {
      type: "LOGIN",
      username: username,
      password: password,
    };
    this.send(message);
  }

  requestQuiz() {
    const session = getUserSession();
    const message = {
      type: "GET_QUIZ",
      userId: session.userId,
    };
    this.send(message);
  }

  submitAnswers(answers) {
    const session = getUserSession();
    const message = {
      type: "SUBMIT_ANSWERS",
      userId: session.userId,
      answers: answers,
    };
    this.send(message);
  }

  startTimer(duration) {
    const message = {
      type: "START_TIMER",
      duration: duration,
    };
    this.send(message);
  }

  handleMessage(data) {
    try {
      const message = JSON.parse(data);
      console.log("📥 Received:", message.type);

      switch (message.type) {
        case "LOGIN_SUCCESS":
          this.handleLoginSuccess(message);
          break;
        case "LOGIN_FAILED":
          this.handleLoginFailed(message);
          break;
        case "QUIZ_DATA":
          this.handleQuizData(message);
          break;
        case "TIMER_UPDATE":
          this.handleTimerUpdate(message);
          break;
        case "RESULTS":
          this.handleResults(message);
          break;
        case "ERROR":
          this.handleError(message);
          break;
      }
    } catch (error) {
      console.error("Error handling message:", error);
    }
  }

  handleLoginSuccess(message) {
    saveUserSession(message.userId, message.username);
    if (typeof showMessage === "function") {
      showMessage("Login successful! Redirecting...", "success");
    }
    setTimeout(() => {
      window.location.href = "quiz.html";
    }, 1000);
  }

  handleLoginFailed(message) {
    if (typeof showMessage === "function") {
      showMessage("Login failed: " + message.message, "error");
    }
  }

  handleQuizData(message) {
    cacheQuiz(message.questions);
    if (typeof displayQuiz === "function") {
      displayQuiz(message.questions);
    }
  }

  handleTimerUpdate(message) {
    if (typeof updateTimer === "function") {
      updateTimer(message.timeRemaining);
    }
  }

  handleResults(message) {
    saveResults(message.score, message.total, message.percentage);
    window.location.href = "results.html";
  }

  handleError(message) {
    if (typeof showMessage === "function") {
      showMessage("Error: " + message.message, "error");
    }
    alert("Error: " + message.message);
  }
}

// Create global client instance
const client = new QuizClient();
