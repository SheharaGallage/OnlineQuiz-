// Save user session to localStorage
function saveUserSession(userId, username) {
  localStorage.setItem("userId", userId);
  localStorage.setItem("username", username);
  localStorage.setItem("loginTime", new Date().toISOString());
  console.log("💾 Session saved:", username);
}

// Get user session from localStorage
function getUserSession() {
  return {
    userId: localStorage.getItem("userId"),
    username: localStorage.getItem("username"),
    loginTime: localStorage.getItem("loginTime"),
  };
}

// Check if user is logged in
function isLoggedIn() {
  const session = getUserSession();
  return session.userId !== null && session.username !== null;
}

// Cache quiz questions
function cacheQuiz(questions) {
  localStorage.setItem("currentQuiz", JSON.stringify(questions));
  console.log("💾 Quiz cached:", questions.length, "questions");
}

// Get cached quiz
function getCachedQuiz() {
  const quiz = localStorage.getItem("currentQuiz");
  return quiz ? JSON.parse(quiz) : null;
}

// Auto-save answers (for recovery if connection drops)
function autoSaveAnswers(answers) {
  localStorage.setItem("savedAnswers", JSON.stringify(answers));
  console.log("💾 Answers auto-saved");
}

// Get saved answers
function getSavedAnswers() {
  const answers = localStorage.getItem("savedAnswers");
  return answers ? JSON.parse(answers) : {};
}

// Save quiz results
function saveResults(score, total, percentage) {
  const result = {
    score: score,
    total: total,
    percentage: percentage,
    timestamp: new Date().toISOString(),
  };
  localStorage.setItem("lastResult", JSON.stringify(result));
  console.log("💾 Results saved:", result);
}

// Get last result
function getLastResult() {
  const result = localStorage.getItem("lastResult");
  return result ? JSON.parse(result) : null;
}

// Clear all storage (logout)
function clearStorage() {
  localStorage.clear();
  console.log("🗑️ Storage cleared");
}

// Check authentication on page load
window.addEventListener("DOMContentLoaded", function () {
  const currentPage = window.location.pathname;

  if (
    currentPage.includes("quiz.html") ||
    currentPage.includes("results.html")
  ) {
    if (!isLoggedIn()) {
      console.log("⚠️ Not logged in, redirecting to login page");
      window.location.href = "index.html";
    }
  }
});
