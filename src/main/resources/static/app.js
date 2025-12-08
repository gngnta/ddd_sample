const out = (id, data) => {
    const el = document.getElementById(id);
    el.textContent = typeof data === 'string' ? data : JSON.stringify(data, null, 2);
};

const base = () => {
    const b = document.getElementById('baseUrl').value.trim();
    return b.endsWith('/') ? b.slice(0, -1) : b;
};

const request = async (path, options = {}, target) => {
    try {
        const res = await fetch(base() + path, {
            headers: { 'Content-Type': 'application/json' },
            ...options,
        });
        if (res.status === 204) {
            out(target, '204 No Content');
            return null;
        }
        const data = await res.json();
        out(target, data);
        return data;
    } catch (e) {
        out(target, `Error: ${e}`);
        return null;
    }
};

window.loadCategories = async function () {
    await request('/api/categories', {}, 'categoriesOut');
};

window.startAttempt = async function () {
    const categoryId = Number(document.getElementById('startCategoryId').value);
    await request('/api/attempts/start', {
        method: 'POST',
        body: JSON.stringify({ category_id: categoryId }),
    }, 'startOut');
};

window.nextQuestion = async function () {
    const attemptId = Number(document.getElementById('attemptIdNext').value);
    await request(`/api/attempts/${attemptId}/question`, {}, 'nextOut');
};

window.submitAnswer = async function () {
    const attemptId = Number(document.getElementById('attemptIdAnswer').value);
    const questionId = Number(document.getElementById('questionIdAnswer').value);
    const choiceId = Number(document.getElementById('choiceIdAnswer').value);
    await request(`/api/attempts/${attemptId}/answer`, {
        method: 'POST',
        body: JSON.stringify({ question_id: questionId, choice_id: choiceId }),
    }, 'answerOut');
};

window.getResult = async function () {
    const attemptId = Number(document.getElementById('attemptIdResult').value);
    await request(`/api/attempts/${attemptId}/result`, {}, 'resultOut');
};

window.listAttempts = async function () {
    await request('/api/attempts', {}, 'listOut');
};

window.listAnswers = async function () {
    const attemptId = Number(document.getElementById('attemptIdAnswers').value);
    await request(`/api/attempts/${attemptId}/answers`, {}, 'answersOut');
};