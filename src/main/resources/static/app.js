const API_BASE = '/api';
let currentAttemptId = null;
let currentCategoryId = null;
let currentCategoryName = null;

// 画面切り替え
function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.add('hidden');
    });
    document.getElementById(screenId).classList.remove('hidden');
}

// カテゴリ選択画面表示
async function showCategoryScreen() {
    showScreen('categoryScreen');
    await loadCategories();
}

// カテゴリ一覧取得
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE}/categories`);
        const categories = await response.json();

        const categoryList = document.getElementById('categoryList');
        categoryList.innerHTML = categories.map(category => `
            <div class="category-card" onclick="selectCategory(${category.id}, '${category.name}')">
                <h3>${category.name}</h3>
                <p>${category.description || ''}</p>
            </div>
        `).join('');
    } catch (error) {
        console.error('カテゴリ取得エラー:', error);
        alert('カテゴリの取得に失敗しました');
    }
}

// カテゴリ選択
function selectCategory(categoryId, categoryName) {
    currentCategoryId = categoryId;
    currentCategoryName = categoryName;
    showStartScreen();
}

// スタート画面表示
function showStartScreen() {
    showScreen('startScreen');
    document.getElementById('startInfo').innerHTML = `
        <div class="result-card">
            <h2>${currentCategoryName}</h2>
            <p>このカテゴリのクイズに挑戦します</p>
        </div>
    `;
}

// クイズ開始
async function startQuiz() {
    try {
        const response = await fetch(`${API_BASE}/attempts`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ categoryId: currentCategoryId })
        });

        const attempt = await response.json();
        currentAttemptId = attempt.id;

        await loadNextQuestion();
    } catch (error) {
        console.error('クイズ開始エラー:', error);
        alert('クイズの開始に失敗しました');
    }
}

// 次の問題取得
async function loadNextQuestion() {
    try {
        const response = await fetch(`${API_BASE}/attempts/${currentAttemptId}/question`);

        if (response.status === 204) {
            // 問題がない場合は結果画面へ
            await showResult();
            return;
        }

        const data = await response.json();
        showQuizScreen(data);
    } catch (error) {
        console.error('問題取得エラー:', error);
        alert('問題の取得に失敗しました');
    }
}

// クイズ画面表示
function showQuizScreen(data) {
    showScreen('quizScreen');

    document.getElementById('questionInfo').textContent = `問題 ID: ${data.question.id}`;
    document.getElementById('questionText').textContent = data.question.questionText;

    const choicesDiv = document.getElementById('choices');
    choicesDiv.innerHTML = data.question.choices.map(choice => `
        <button class="choice-button" onclick="submitAnswer(${data.question.id}, ${choice.id})">
            ${choice.choiceText}
        </button>
    `).join('');

    document.getElementById('answerResult').className = 'hidden';
    document.getElementById('nextButtonContainer').className = 'button-container hidden';
}

// 回答送信
async function submitAnswer(questionId, choiceId) {
    try {
        // ボタンを無効化
        document.querySelectorAll('.choice-button').forEach(btn => {
            btn.disabled = true;
        });

        const response = await fetch(`${API_BASE}/attempts/${currentAttemptId}/answer`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ questionId, choiceId })
        });

        const result = await response.json();

        // 正誤表示
        const resultDiv = document.getElementById('answerResult');
        resultDiv.className = result.isCorrect ? 'correct' : 'incorrect';
        resultDiv.textContent = result.isCorrect ? '✓ 正解！' : '✗ 不正解';

        // 選択した選択肢をハイライト
        document.querySelectorAll('.choice-button').forEach(btn => {
            if (btn.textContent.trim() === document.querySelector(`button[onclick*="${choiceId}"]`).textContent.trim()) {
                btn.classList.add(result.isCorrect ? 'correct' : 'incorrect');
            }
        });

        // 次へボタンを表示
        const nextButtonContainer = document.getElementById('nextButtonContainer');
        nextButtonContainer.className = 'button-container';

        if (result.status === 'completed') {
            nextButtonContainer.innerHTML = '<button class="next-button" onclick="showResult()">結果を見る</button>';
        } else {
            nextButtonContainer.innerHTML = '<button class="next-button" onclick="loadNextQuestion()">次の問題へ</button>';
        }

    } catch (error) {
        console.error('回答送信エラー:', error);
        alert('回答の送信に失敗しました');
    }
}

// 結果画面表示
async function showResult() {
    try {
        const response = await fetch(`${API_BASE}/attempts/${currentAttemptId}`);
        const attempt = await response.json();

        showScreen('resultScreen');

        const passRate = attempt.totalQuestions > 0
            ? Math.round((attempt.correctCount / attempt.totalQuestions) * 100)
            : 0;

        document.getElementById('resultInfo').innerHTML = `
            <div class="result-card">
                <h2>${attempt.categoryName}</h2>
                <div class="score">${attempt.correctCount} / ${attempt.totalQuestions}</div>
                <div class="status ${attempt.passed ? 'passed' : 'failed'}">
                    ${attempt.passed ? '合格！' : '不合格'}
                </div>
                <p>正答率: ${passRate}%</p>
            </div>
        `;
    } catch (error) {
        console.error('結果取得エラー:', error);
        alert('結果の取得に失敗しました');
    }
}

// 挑戦履歴画面表示
async function showHistory() {
    showScreen('historyScreen');
    await loadHistory();
}

// 挑戦履歴取得
async function loadHistory() {
    try {
        const response = await fetch(`${API_BASE}/attempts`);
        const attempts = await response.json();

        const historyList = document.getElementById('historyList');

        if (attempts.length === 0) {
            historyList.innerHTML = '<p class="loading">まだ挑戦履歴がありません</p>';
            return;
        }

        historyList.innerHTML = attempts.map(attempt => {
            const date = new Date(attempt.createdAt).toLocaleString('ja-JP');
            const score = `${attempt.correctCount || 0}/${attempt.totalQuestions || 0}`;

            return `
                <div class="history-card" onclick="showDetail(${attempt.id})">
                    <h3>${attempt.categoryName}</h3>
                    <p>日時: ${date}</p>
                    <p>スコア: ${score}
                        <span class="badge ${attempt.status}">${attempt.status === 'completed' ? '完了' : '進行中'}</span>
                        ${attempt.status === 'completed'
                    ? `<span class="badge ${attempt.passed ? 'passed' : 'failed'}">${attempt.passed ? '合格' : '不合格'}</span>`
                    : ''}
                    </p>
                </div>
            `;
        }).join('');
    } catch (error) {
        console.error('履歴取得エラー:', error);
        alert('履歴の取得に失敗しました');
    }
}

// 挑戦詳細画面表示
async function showDetail(attemptId) {
    currentAttemptId = attemptId;
    showScreen('detailScreen');
    await loadAnswerHistory();
}

// 回答詳細を現在の挑戦から表示
async function showAnswerHistory() {
    showScreen('detailScreen');
    await loadAnswerHistory();
}

// 回答履歴取得
async function loadAnswerHistory() {
    try {
        // 挑戦情報取得
        const attemptResponse = await fetch(`${API_BASE}/attempts/${currentAttemptId}`);
        const attempt = await attemptResponse.json();

        // 回答履歴取得
        const answersResponse = await fetch(`${API_BASE}/attempts/${currentAttemptId}/answers`);
        const answers = await answersResponse.json();

        const date = new Date(attempt.createdAt).toLocaleString('ja-JP');
        const score = `${attempt.correctCount || 0}/${attempt.totalQuestions || 0}`;

        document.getElementById('detailInfo').innerHTML = `
            <div class="result-card">
                <h2>${attempt.categoryName}</h2>
                <p>日時: ${date}</p>
                <p>スコア: ${score}
                    <span class="badge ${attempt.status}">${attempt.status === 'completed' ? '完了' : '進行中'}</span>
                    ${attempt.status === 'completed'
                ? `<span class="badge ${attempt.passed ? 'passed' : 'failed'}">${attempt.passed ? '合格' : '不合格'}</span>`
                : ''}
                </p>
            </div>
        `;

        const answerHistoryList = document.getElementById('answerHistoryList');

        if (answers.length === 0) {
            answerHistoryList.innerHTML = '<p class="loading">まだ回答がありません</p>';
            return;
        }

        answerHistoryList.innerHTML = answers.map((answer, index) => `
            <div class="answer-item ${answer.isCorrect ? 'correct' : 'incorrect'}">
                <div class="question">
                    <strong>問題 ${index + 1}:</strong> ${answer.questionText}
                </div>
                <div class="choice">
                    あなたの回答: ${answer.choiceText}
                    ${answer.isCorrect ? '✓ 正解' : '✗ 不正解'}
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('回答履歴取得エラー:', error);
        alert('回答履歴の取得に失敗しました');
    }
}

// 初期化
window.addEventListener('DOMContentLoaded', () => {
    showCategoryScreen();
});
