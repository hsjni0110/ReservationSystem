import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from './common/test-options.js';

export { options };

const token = options.accessTokens[0];

export default function () {
    const randomRouteId = Math.floor(Math.random() * 12000) + 1;

    const payload = JSON.stringify({
        routeScheduleId: randomRouteId,
    });

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
    };

    const res = http.post(`${BASE_URL}/reservation/seats`, JSON.stringify({
        routeScheduleId: randomRouteId,
    }), { headers });

    check(res, {
        '✅ seat query success (200)': (r) => r.status === 200,
        '❌ failed or 500': (r) => r.status !== 500,
    });

    sleep(1);
}