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
    return res.json();
}