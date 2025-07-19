const BASE_URL = import.meta.env.VITE_API_URL;

function getAuthHeaders() {
    const token = localStorage.getItem('authToken');
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

export async function getUserProfile() {
    const res = await fetch(`${BASE_URL}/api/user/profile`, {
        method: 'GET',
        headers: getAuthHeaders(),
    });

    if (!res.ok) throw new Error('프로필 조회 실패');
    return res.json();
}

export async function getUserPoints() {
    const res = await fetch(`${BASE_URL}/api/user/points`, {
        method: 'GET',
        headers: getAuthHeaders(),
    });

    if (!res.ok) throw new Error('포인트 조회 실패');
    return res.json();
}

export async function getUserPointHistory() {
    const res = await fetch(`${BASE_URL}/api/user/points/history`, {
        method: 'GET',
        headers: getAuthHeaders(),
    });

    if (!res.ok) throw new Error('포인트 내역 조회 실패');
    return res.json();
}