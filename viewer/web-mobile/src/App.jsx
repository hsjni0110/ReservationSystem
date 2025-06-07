import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import SignInPage from './pages/SignInPage'
import SignUpPage from './pages/SignUpPage'
import HomePage from './pages/Home'

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/sign-in" element={<SignInPage />} />
                <Route path="/sign-up" element={<SignUpPage />} />
                {/* 잘못된 경로 처리 */}
                <Route path="*" element={<Navigate to="/sign-in" replace />} />
            </Routes>
        </Router>
    )
}