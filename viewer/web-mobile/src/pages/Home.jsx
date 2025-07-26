import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { isAuthenticated, signOut } from '../api/auth';
import SearchForm from '../components/forms/SearchForm';

export default function Home() {
    const navigate = useNavigate();
    const [showSearch, setShowSearch] = useState(false); // 검색창 표시 여부
    const [schedules, setSchedules] = useState([]);

    const handleMyPageClick = () => {
        if (isAuthenticated()) {
            navigate('/mypage');
        } else {
            navigate('/sign-in');
        }
    };

    const handleSignOut = () => {
        if (confirm('로그아웃 하시겠습니까?')) {
            signOut();
            setShowSearch(false); // 검색창 닫기
            setSchedules([]); // 검색 결과 초기화
            alert('로그아웃되었습니다.');
        }
    };

    const handleSearchClick = () => {
        if (isAuthenticated()) {
            setShowSearch(true);
        } else {
            alert('로그인이 필요한 서비스입니다.');
            navigate('/sign-in');
        }
    };

    return (
        <div className="flex flex-col min-h-screen" style={{ backgroundColor: 'var(--color-neutral-50)' }}>
            {/* 헤더 */}
            <header className="hero-gradient text-white py-6 px-6 shadow-lg flex justify-between items-center">
                <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-white bg-opacity-20 rounded-full flex items-center justify-center">
                        <span className="text-lg">🚌</span>
                    </div>
                    <h1 className="brand-logo">Smart Journey</h1>
                </div>
                <div className="flex gap-3">
                    {isAuthenticated() ? (
                        <>
                            <button
                                onClick={handleMyPageClick}
                                className="text-sm bg-white/20 hover:bg-white/30 text-white backdrop-blur-sm px-4 py-2 rounded-full font-medium transition-all duration-200 hover:scale-105 border border-white/20"
                            >
                                마이페이지
                            </button>
                            <button
                                onClick={handleSignOut}
                                className="text-sm bg-white/20 hover:bg-white/30 text-white backdrop-blur-sm px-4 py-2 rounded-full font-medium transition-all duration-200 hover:scale-105 shadow-md border border-white/20"
                            >
                                로그아웃
                            </button>
                        </>
                    ) : (
                        <button
                            onClick={handleMyPageClick}
                            className="text-sm bg-white/20 hover:bg-white/30 text-white backdrop-blur-sm border border-white/30 hover:bg-white hover:text-purple-600 px-6 py-2 rounded-full font-medium transition-all duration-200"
                        >
                            로그인
                        </button>
                    )}
                </div>
            </header>

            {/* 메인 */}
            <main className="flex-1 p-6 flex flex-col gap-8">
                {/* Hero 섹션 */}
                <section className="relative overflow-hidden rounded-2xl shadow-xl">
                    {/* 배경 장식 */}
                    <div className="absolute inset-0 bg-gradient-to-br from-blue-50/50 via-white to-purple-50/50">
                        <div className="absolute top-0 right-0 w-32 h-32 bg-blue-200/20 rounded-full blur-2xl transform translate-x-16 -translate-y-16"></div>
                        <div className="absolute bottom-0 left-0 w-24 h-24 bg-purple-200/20 rounded-full blur-xl transform -translate-x-12 translate-y-12"></div>
                        <div className="absolute top-1/2 left-1/2 w-20 h-20 bg-pink-200/10 rounded-full blur-lg transform -translate-x-1/2 -translate-y-1/2"></div>
                    </div>
                    
                    {/* 컨텐츠 */}
                    <div className="relative bg-white/60 backdrop-blur-sm border border-white/20 p-8 text-center space-y-6">
                        <div className="space-y-3">
                            <h2 className="text-3xl font-bold gradient-text">
                                편안하고 안전한 여행
                            </h2>
                            <p className="text-lg text-gray-600 leading-relaxed">
                                스마트한 고속버스 예약으로 시작하는 특별한 여행
                            </p>
                        </div>
                    
                    {isAuthenticated() ? (
                        <div className="bg-gradient-to-r from-green-50 to-blue-50 p-4 rounded-xl border border-green-200">
                            <p className="text-green-700 font-medium flex items-center justify-center gap-2">
                                <span>✨</span>
                                환영합니다! 원하는 목적지를 검색해보세요
                            </p>
                        </div>
                    ) : (
                        <div className="bg-gradient-to-r from-blue-50 to-purple-50 p-4 rounded-xl border border-blue-200">
                            <p className="text-blue-700 font-medium">
                                로그인 후 전국 어디든 편리하게 예약하세요
                            </p>
                        </div>
                    )}
                    
                    <div className="flex flex-col gap-4">
                        <button
                            onClick={handleSearchClick}
                            className="btn-primary text-lg py-4 px-8 rounded-xl shadow-lg hover:shadow-xl transform hover:-translate-y-1 transition-all duration-300"
                        >
                            <span className="flex items-center justify-center gap-3">
                                <span>🔍</span>
                                {isAuthenticated() ? '예약 경로 검색하기' : '로그인하여 예약하기'}
                            </span>
                        </button>
                        
                        {/* 빠른 링크 */}
                        <div className="flex gap-4 justify-center mt-4">
                            <div className="text-center">
                                <div className="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center mb-2 mx-auto">
                                    <span className="text-xl">📍</span>
                                </div>
                                <p className="text-sm text-gray-600 font-medium">실시간<br/>노선 정보</p>
                            </div>
                            <div className="text-center">
                                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-2 mx-auto">
                                    <span className="text-xl">💳</span>
                                </div>
                                <p className="text-sm text-gray-600 font-medium">간편<br/>결제</p>
                            </div>
                            <div className="text-center">
                                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mb-2 mx-auto">
                                    <span className="text-xl">🎯</span>
                                </div>
                                <p className="text-sm text-gray-600 font-medium">포인트<br/>적립</p>
                            </div>
                        </div>
                    </div>
                </div>
                </section>

                {/* 검색창 */}
                {showSearch && (
                    <section className="relative overflow-hidden rounded-2xl shadow-xl animate-fadeIn">
                        {/* 배경 그라디언트 */}
                        <div className="absolute inset-0 bg-gradient-to-br from-blue-50 via-purple-50 to-pink-50">
                            <div className="absolute inset-0 bg-gradient-to-r from-blue-500/10 to-purple-500/10"></div>
                            {/* 장식적 요소들 */}
                            <div className="absolute top-4 right-4 w-20 h-20 bg-blue-200/30 rounded-full blur-xl"></div>
                            <div className="absolute bottom-4 left-4 w-16 h-16 bg-purple-200/30 rounded-full blur-lg"></div>
                            <div className="absolute top-1/2 left-1/3 w-12 h-12 bg-pink-200/20 rounded-full blur-lg"></div>
                        </div>
                        
                        {/* 컨텐츠 */}
                        <div className="relative bg-white/80 backdrop-blur-sm border border-white/20 p-6 space-y-4">
                            <div className="flex items-center justify-between">
                                <div className="flex items-center gap-3">
                                    <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center shadow-lg">
                                        <span className="text-lg text-white">🚌</span>
                                    </div>
                                    <div>
                                        <h3 className="text-xl font-bold text-gray-800">노선 검색</h3>
                                        <p className="text-sm text-gray-600">원하는 목적지를 찾아보세요</p>
                                    </div>
                                </div>
                                <button
                                    onClick={() => setShowSearch(false)}
                                    className="text-gray-500 hover:text-gray-700 p-2 rounded-full hover:bg-white/50 transition-all duration-200"
                                >
                                    <span className="text-xl">✕</span>
                                </button>
                            </div>
                            <SearchForm onSearchResults={setSchedules} />
                        </div>
                    </section>
                )}

                {/* 검색 결과 */}
                <section className="card">
                    <div className="card-header">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                                <span className="text-lg">📋</span>
                            </div>
                            <h3 className="text-xl font-bold text-gray-800">검색 결과</h3>
                        </div>
                    </div>
                    
                    {schedules.length === 0 ? (
                        <div className="text-center py-12 space-y-4">
                            <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto">
                                <span className="text-2xl">🔍</span>
                            </div>
                            <div className="space-y-2">
                                <p className="text-gray-600 font-medium">검색된 노선이 없습니다</p>
                                <p className="text-sm text-gray-500">
                                    위의 검색 버튼을 클릭하여 원하는 노선을 찾아보세요
                                </p>
                            </div>
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {schedules.map((s, index) => (
                                <div key={index} className="border border-gray-200 rounded-xl p-4 hover:border-blue-300 hover:shadow-md transition-all duration-200 bg-gradient-to-r from-white to-blue-50">
                                    <div className="flex justify-between items-start mb-3">
                                        <div className="flex items-center gap-3">
                                            <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
                                            <span className="font-semibold text-gray-800">{s.departure}</span>
                                        </div>
                                        <div className="flex items-center gap-2 text-gray-500">
                                            <span>→</span>
                                        </div>
                                        <div className="flex items-center gap-3">
                                            <span className="font-semibold text-gray-800">{s.arrival}</span>
                                            <div className="w-3 h-3 bg-orange-500 rounded-full"></div>
                                        </div>
                                    </div>
                                    <div className="flex justify-between items-center text-sm">
                                        <div className="flex items-center gap-2 text-gray-600">
                                            <span>🕐</span>
                                            <span>{s.timeSlot}</span>
                                        </div>
                                        <div className="flex items-center gap-2 text-gray-600">
                                            <span>🚌</span>
                                            <span>버스 #{s.busId}</span>
                                        </div>
                                    </div>
                                    <button className="w-full mt-3 btn-primary py-2 text-sm rounded-lg">
                                        예약하기
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
}