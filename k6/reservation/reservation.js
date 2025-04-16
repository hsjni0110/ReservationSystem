import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from '../common/test-options.js';
import { Trend } from 'k6/metrics';

const successDuration = new Trend('reservation_success_duration', true);

export { options };

const accessTokens = options.accessTokens;

export default function () {
    const token = accessTokens[Math.floor(Math.random() * accessTokens.length)];

    const randomRouteId = Math.floor(Math.random() * 12000) + 1;
    const startSeatId = Math.floor(Math.random() * 19); // 0~18
    const seatIds = [startSeatId, startSeatId + 1];

    const payload = JSON.stringify({
        routeScheduleId: randomRouteId,
        scheduleSeatIds: seatIds,
    });

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
    };

    const res = http.post(`${BASE_URL}/reservation/seat`, payload, { headers });

    if (res.status === 200) {
        successDuration.add(res.timings.duration);
    }

    check(res, {
        '✅ 예약 성공 or 락 충돌 (200 or 409)': (r) => r.status === 200 || r.status === 409,
    });

    sleep(1);
}