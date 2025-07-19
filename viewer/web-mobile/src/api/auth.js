const BASE_URL = import.meta.env.VITE_API_URL;

export async function signUp(data) {
    const res = await fetch(`${BASE_URL}/sign-up`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error('회원가입 실패');
}

export async function signIn(data) {
    const res = await fetch(`${BASE_URL}/sign-in`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error('로그인 실패');
    
    const result = await res.json();
    
    // JWT 토큰을 localStorage에 저장
    if (result.accessToken) {
        localStorage.setItem('authToken', result.accessToken);
    }
    
    return result;
}

export function signOut() {
    localStorage.removeItem('authToken');
}

export function isAuthenticated() {
    return localStorage.getItem('authToken') !== null;
}