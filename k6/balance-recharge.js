import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from './common/test-options.js';

export { options };

const token = options.accessTokens[0];

export default function () {
    const payload = JSON.stringify({
        rechargeAmount: 10000,
    });

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
    };

    const res = http.post(`${BASE_URL}/account/recharge`, payload, { headers });

    check(res, {
        '✅ recharge success (200) or lock conflict (409)': (r) =>
            r.status === 200 || r.status === 409,
    });

    sleep(1);
}
