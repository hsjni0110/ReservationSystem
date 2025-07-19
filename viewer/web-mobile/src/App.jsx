import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import SignInPage from './pages/SignInPage'
import SignUpPage from './pages/SignUpPage'
import HomePage from './pages/Home'
import MyPage from './pages/MyPage'
import ProtectedRoute from './components/ProtectedRoute'

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/sign-in" element={<SignInPage />} />
                <Route path="/sign-up" element={<SignUpPage />} />
                <Route path="/mypage" element={
                    <ProtectedRoute>
                        <MyPage />
                    </ProtectedRoute>
                } />
                {/* 잘못된 경로 처리 */}
                <Route path="*" element={<Navigate to="/sign-in" replace />} />
            </Routes>
        </Router>
    )
}