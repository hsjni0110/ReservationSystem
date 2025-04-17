// get-balance.test.js
import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from './common/test-options.js';

export { options };

const accessTokens = options.accessTokens;

export default function () {
    const token = accessTokens[Math.floor(Math.random() * accessTokens.length)];

    const res = http.get(`${BASE_URL}/account`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    check(res, {
        'Account request returned 200': (r) => r.status === 200,
    });

    sleep(1);
}
