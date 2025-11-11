let currentQuiz = [];
let userAnswers = [];
let timerInterval = null;

// Initialize quiz page
window.addEventListener("DOMContentLoaded", function () {
  if (window.location.pathname.includes("quiz.html")) {
    const session = getUserSession();
    document.getElementById("username-display").textContent =
      "👤 Welcome, " + session.username;
  }
});

function startQuiz() {
  console.log("🚀 Starting quiz...");

  // Request quiz from server
  client.requestQuiz();

  // Start 5-minute timer (300 seconds)
  client.startTimer(300);

  // Hide start button
  document.getElementById("quiz-container").style.display = "none";
}

function displayQuiz(questions) {
  console.log("📋 Displaying quiz with", questions.length, "questions");

  currentQuiz = questions;
  userAnswers = new Array(questions.length).fill(-1);

  const questionsDiv = document.getElementById("questions-list");
  questionsDiv.innerHTML = "";

  questions.forEach((q, index) => {
    const questionDiv = document.createElement("div");
    questionDiv.className = "question-item";

    let optionsHTML = "";
    q.options.forEach((option, optIndex) => {
      optionsHTML += `
                <label class="option-label">
                    <input type="radio" 
                           name="question${index}" 
                           value="${optIndex}"
                           onchange="saveAnswer(${index}, ${optIndex})">
                    ${option}
                </label>
            `;
    });

    questionDiv.innerHTML = `
            <div class="question-text">
                <strong>Question ${index + 1}:</strong> ${q.question}
            </div>
            <div class="options">
                ${optionsHTML}
            </div>
        `;

    questionsDiv.appendChild(questionDiv);
  });

  document.getElementById("quiz-questions").style.display = "block";

  // Scroll to questions
  window.scrollTo(0, 0);
}

function saveAnswer(questionIndex, answerIndex) {
  userAnswers[questionIndex] = answerIndex;
  autoSaveAnswers(userAnswers);
  console.log(
    "✏️ Answer saved for question",
    questionIndex + 1,
    ":",
    answerIndex
  );
}

function submitQuiz() {
  // Check if all questions are answered
  const unanswered = userAnswers.filter((a) => a === -1).length;

  if (unanswered > 0) {
    if (
      !confirm(`You have ${unanswered} unanswered question(s). Submit anyway?`)
    ) {
      return;
    }
  }

  console.log("📤 Submitting quiz...");

  // Send answers to server
  client.submitAnswers(userAnswers);

  // Stop timer
  if (timerInterval) {
    clearInterval(timerInterval);
  }
}

function updateTimer(timeRemaining) {
  const minutes = Math.floor(timeRemaining / 60);
  const seconds = timeRemaining % 60;

  const timeStr = `${minutes.toString().padStart(2, "0")}:${seconds
    .toString()
    .padStart(2, "0")}`;
  const timeElem = document.getElementById("time-remaining");

  if (timeElem) {
    timeElem.textContent = timeStr;

    // Change color when time is running out
    if (timeRemaining <= 60) {
      timeElem.style.color = "#d9534f";
    } else if (timeRemaining <= 120) {
      timeElem.style.color = "#f0ad4e";
    }
  }

  // Auto-submit when time runs out
  if (timeRemaining === 0) {
    alert("⏰ Time is up! Submitting your answers...");
    submitQuiz();
  }

  // Warning at 1 minute
  if (timeRemaining === 60) {
    alert("⚠️ Only 1 minute remaining!");
  }
}

function logout() {
  if (confirm("Are you sure you want to logout?")) {
    clearStorage();
    window.location.href = "index.html";
  }
}
