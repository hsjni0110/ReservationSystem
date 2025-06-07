const BASE_URL = import.meta.env.VITE_API_URL;

export async function getAvailableRouteSchedules(data) {
    const res = await fetch(`${BASE_URL}/route/route-schedules`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });

    if (!res.ok) throw new Error('스케줄 조회 실패');
    return res.json();
}