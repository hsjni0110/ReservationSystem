import { useNavigate } from 'react-router-dom'
import { signOut } from '../api/auth'
import UserProfile from '../components/UserProfile'
import PointSummary from '../components/PointSummary'
import PointHistory from '../components/PointHistory'

export default function MyPage() {
    const navigate = useNavigate()

    const handleSignOut = () => {
        signOut()
        navigate('/sign-in')
    }

    const handleGoHome = () => {
        navigate('/')
    }

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            {/* 헤더 */}
            <header className="bg-blue-600 text-white py-4 px-6 shadow flex justify-between items-center">
                <button
                    onClick={handleGoHome}
                    className="text-lg font-bold hover:text-blue-100"
                >
                    ← 마이페이지
                </button>
                <button
                    onClick={handleSignOut}
                    className="text-sm bg-blue-700 hover:bg-blue-800 px-3 py-1 rounded transition-colors"
                >
                    로그아웃
                </button>
            </header>

            {/* 메인 콘텐츠 */}
            <main className="flex-1 p-4 space-y-4">
                {/* 포인트 요약 */}
                <PointSummary />

                {/* 프로필 정보 */}
                <UserProfile />

                {/* 포인트 내역 */}
                <PointHistory />

                {/* 추가 메뉴 */}
                <div className="bg-white rounded shadow p-4">
                    <h3 className="text-lg font-semibold text-gray-800 mb-3">메뉴</h3>
                    <div className="space-y-2">
                        <button 
                            onClick={handleGoHome}
                            className="w-full text-left p-3 text-sm text-gray-700 hover:bg-gray-50 rounded border"
                        >
                            🏠 홈으로 돌아가기
                        </button>
                        <button 
                            className="w-full text-left p-3 text-sm text-gray-700 hover:bg-gray-50 rounded border"
                            disabled
                        >
                            ✏️ 프로필 수정 (준비중)
                        </button>
                        <button 
                            className="w-full text-left p-3 text-sm text-gray-700 hover:bg-gray-50 rounded border"
                            disabled
                        >
                            📋 예약 내역 (준비중)
                        </button>
                    </div>
                </div>
            </main>
        </div>
    )
}