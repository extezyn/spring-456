const INACTIVITY_TIMEOUT = 3 * 60 * 1000; // 3 минуты бездействия
const TOKEN_REFRESH_INTERVAL = 2.5 * 60 * 1000; // Обновляем токен каждые 2.5 минуты

let inactivityTimer;
let refreshTokenInterval;

// Инициализация отслеживания активности
function initActivityTracking() {
    // Сбрасываем таймер при любом действии пользователя
    const events = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'];
    events.forEach(event => {
        document.addEventListener(event, resetInactivityTimer, true);
    });
    
    resetInactivityTimer();
    startTokenRefresh();
}

// Сброс таймера бездействия
function resetInactivityTimer() {
    clearTimeout(inactivityTimer);
    inactivityTimer = setTimeout(() => {
        logout('Сессия завершена из-за бездействия (3 минуты)');
    }, INACTIVITY_TIMEOUT);
}

// Автоматическое обновление токена
function startTokenRefresh() {
    clearInterval(refreshTokenInterval);
    refreshTokenInterval = setInterval(async () => {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
            logout('Токен обновления отсутствует');
            return;
        }
        
        try {
            const resp = await fetch('/api/v1/auth/refresh', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken })
            });
            
            if (resp.ok) {
                const data = await resp.json();
                localStorage.setItem('jwt', data.token);
                localStorage.setItem('refreshToken', data.refreshToken);
                console.log('Токен обновлён');
            } else {
                logout('Ошибка обновления токена');
            }
        } catch (e) {
            console.error('Ошибка обновления токена:', e);
        }
    }, TOKEN_REFRESH_INTERVAL);
}

// Получение токена для запросов
function getAuthToken() {
    return localStorage.getItem('jwt');
}

// Проверка авторизации
function checkAuth() {
    const token = getAuthToken();
    if (!token) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// Выход
function logout(message) {
    clearTimeout(inactivityTimer);
    clearInterval(refreshTokenInterval);
    localStorage.removeItem('jwt');
    localStorage.removeItem('refreshToken');
    if (message) {
        alert(message);
    }
    window.location.href = '/login.html';
}

// Выполнение авторизованного запроса
async function authFetch(url, options = {}) {
    const token = getAuthToken();
    if (!token) {
        logout('Требуется авторизация');
        return null;
    }
    
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };
    
    try {
        const resp = await fetch(url, { ...options, headers });
        
        if (resp.status === 401) {
            // Пробуем обновить токен
            const refreshToken = localStorage.getItem('refreshToken');
            if (refreshToken) {
                const refreshResp = await fetch('/api/v1/auth/refresh', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ refreshToken })
                });
                
                if (refreshResp.ok) {
                    const data = await refreshResp.json();
                    localStorage.setItem('jwt', data.token);
                    localStorage.setItem('refreshToken', data.refreshToken);
                    // Повторяем исходный запрос
                    headers['Authorization'] = `Bearer ${data.token}`;
                    return await fetch(url, { ...options, headers });
                }
            }
            logout('Сессия истекла');
            return null;
        }
        
        return resp;
    } catch (e) {
        console.error('Ошибка запроса:', e);
        return null;
    }
}

