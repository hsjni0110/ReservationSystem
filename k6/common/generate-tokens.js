const fs = require('fs');
const axios = require('axios');

const USERS = [
    { email: 'hanemail10@email.com', password: 'hanpw10' },
    { email: 'hanemail9@email.com', password: 'hanpw9' },
    { email: 'hanemail8@email.com', password: 'hanpw8' },
    { email: 'hanemail7@email.com', password: 'hanpw7' },
    { email: 'hanemail6@email.com', password: 'hanpw6' },
    { email: 'hanemail5@email.com', password: 'hanpw5' },
    { email: 'hanemail4@email.com', password: 'hanpw4' },
    { email: 'hanemail3@email.com', password: 'hanpw3' },
    { email: 'hanemail2@email.com', password: 'hanpw2' },
    { email: 'hanemail@email.com', password: 'hanpw' }
];

const BASE_URL = 'http://localhost:8080';

async function generateTokens() {
    const tokens = [];

    for (const user of USERS) {
        try {
            const response = await axios.post(`${BASE_URL}/sign-in`, user, {
                headers: { 'Content-Type': 'application/json' },
            });

            const token = response.data.accessToken;
            tokens.push(token);
            console.log(`✅ Token generated for ${user.email}`);
        } catch (error) {
            console.error(`❌ Failed for ${user.email}:`, error.response?.status || error.message);
        }
    }

    // 저장
    fs.writeFileSync('k6/common/tokens.json', JSON.stringify(tokens, null, 2));
    console.log('🎉 All tokens saved to tokens.json');
}

generateTokens();
