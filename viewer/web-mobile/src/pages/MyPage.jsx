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
        <div className="flex flex-col min-h-screen" style={{ backgroundColor: 'var(--color-neutral-50)' }}>
            {/* 헤더 */}
            <header className="hero-gradient text-white py-6 px-6 shadow-lg">
                <div className="flex justify-between items-center">
                    <button
                        onClick={handleGoHome}
                        className="flex items-center gap-3 text-white hover:text-white/80 transition-colors"
                    >
                        <span className="text-xl">←</span>
                        <div className="text-left">
                            <div className="brand-logo text-xl text-white">Smart Journey</div>
                            <div className="text-sm text-white/90">마이페이지</div>
                        </div>
                    </button>
                    <button
                        onClick={handleSignOut}
                        className="text-sm bg-white/20 hover:bg-white/30 text-white backdrop-blur-sm px-4 py-2 rounded-full font-medium transition-all duration-200 hover:scale-105 shadow-md border border-white/20"
                    >
                        로그아웃
                    </button>
                </div>
            </header>

            {/* 메인 콘텐츠 */}
            <main className="flex-1 p-6 space-y-6">
                {/* 대시보드 헤더 */}
                <section className="text-center space-y-2">
                    <h1 className="text-2xl font-bold gradient-text">내 여행 대시보드</h1>
                    <p className="text-gray-600">편리한 여행을 위한 모든 정보를 한눈에</p>
                </section>

                {/* 포인트 요약 카드 */}
                <section>
                    <PointSummary />
                </section>

                {/* 프로필 & 포인트 내역 그리드 */}
                <div className="grid gap-6">
                    {/* 프로필 정보 */}
                    <UserProfile />

                    {/* 포인트 내역 */}
                    <PointHistory />
                </div>

                {/* 빠른 액션 메뉴 */}
                <section className="card">
                    <div className="card-header">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center">
                                <svg className="w-5 h-5 text-purple-600" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.38z" clipRule="evenodd"/>
                                </svg>
                            </div>
                            <h3 className="text-xl font-bold text-gray-800">빠른 액션</h3>
                        </div>
                    </div>
                    
                    <div className="grid grid-cols-1 gap-3">
                        <button 
                            onClick={handleGoHome}
                            className="btn-outline p-4 rounded-xl flex items-center gap-4 hover:scale-105 transition-transform"
                        >
                            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                                <svg className="w-6 h-6 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
                                </svg>
                            </div>
                            <div className="text-left">
                                <div className="font-semibold text-gray-800">홈으로 돌아가기</div>
                                <div className="text-sm text-gray-600">새로운 여행 예약하기</div>
                            </div>
                        </button>
                        
                        <button 
                            className="p-4 rounded-xl flex items-center gap-4 bg-gray-50 border border-gray-200 opacity-60 cursor-not-allowed"
                            disabled
                        >
                            <div className="w-12 h-12 bg-gray-100 rounded-full flex items-center justify-center">
                                <svg className="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                                </svg>
                            </div>
                            <div className="text-left">
                                <div className="font-semibold text-gray-600">프로필 수정</div>
                                <div className="text-sm text-gray-500">개인정보 업데이트 (준비중)</div>
                            </div>
                        </button>
                        
                        <button 
                            className="p-4 rounded-xl flex items-center gap-4 bg-gray-50 border border-gray-200 opacity-60 cursor-not-allowed"
                            disabled
                        >
                            <div className="w-12 h-12 bg-gray-100 rounded-full flex items-center justify-center">
                                <svg className="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clipRule="evenodd"/>
                                </svg>
                            </div>
                            <div className="text-left">
                                <div className="font-semibold text-gray-600">예약 내역</div>
                                <div className="text-sm text-gray-500">과거 여행 기록 보기 (준비중)</div>
                            </div>
                        </button>
                    </div>
                </section>
            </main>
        </div>
    )
}