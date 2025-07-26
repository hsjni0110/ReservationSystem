import { useState, useEffect } from 'react'
import { getUserPoints } from '../api/user'

export default function PointSummary() {
    const [points, setPoints] = useState(null)
    const [loading, setLoading] = useState(true)
    const [isAnimated, setIsAnimated] = useState(false)

    useEffect(() => {
        loadPoints()
    }, [])

    useEffect(() => {
        if (!loading && points) {
            const timer = setTimeout(() => setIsAnimated(true), 100)
            return () => clearTimeout(timer)
        }
    }, [loading, points])

    const loadPoints = async () => {
        try {
            const data = await getUserPoints()
            setPoints(data)
        } catch (error) {
            console.error('포인트 조회 실패:', error)
        } finally {
            setLoading(false)
        }
    }

    // 로딩 상태 - 더 매력적인 스켈레톤
    if (loading) {
        return (
            <div className="card bg-gradient-to-br from-slate-100 to-slate-200 relative overflow-hidden animate-pulse">
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent -translate-x-full animate-[shimmer_2s_infinite] transform"></div>
                
                <div className="space-y-6">
                    <div className="flex items-center gap-3">
                        <div className="w-12 h-12 bg-slate-300 rounded-full"></div>
                        <div className="space-y-2">
                            <div className="h-4 bg-slate-300 rounded w-24"></div>
                            <div className="h-3 bg-slate-300 rounded w-32"></div>
                        </div>
                    </div>
                    
                    <div className="space-y-3">
                        <div className="h-12 bg-slate-300 rounded w-40"></div>
                        <div className="h-4 bg-slate-300 rounded w-48"></div>
                    </div>
                    
                    <div className="flex gap-3">
                        <div className="flex-1 bg-slate-300 rounded-xl h-16"></div>
                        <div className="flex-1 bg-slate-300 rounded-xl h-16"></div>
                    </div>
                </div>
            </div>
        )
    }

    const totalPoints = points?.totalPoints?.amount || 0

    return (
        <div className="card bg-gradient-to-br from-indigo-600 via-purple-600 to-pink-600 relative overflow-hidden transform transition-all duration-500 hover:scale-[1.02] hover:shadow-xl">
            {/* 배경 장식 요소들 */}
            <div className="absolute top-0 right-0 w-40 h-40 bg-white/10 rounded-full -translate-y-20 translate-x-20 blur-3xl"></div>
            <div className="absolute bottom-0 left-0 w-32 h-32 bg-white/10 rounded-full translate-y-16 -translate-x-16 blur-2xl"></div>
            <div className="absolute top-1/2 right-1/4 w-2 h-2 bg-white/30 rounded-full animate-pulse"></div>
            <div className="absolute top-1/4 right-1/3 w-1 h-1 bg-white/40 rounded-full animate-pulse delay-1000"></div>
            <div className="absolute bottom-1/3 left-1/3 w-1.5 h-1.5 bg-white/25 rounded-full animate-pulse delay-500"></div>
            
            <div className="relative z-10 space-y-6">
                {/* 헤더 섹션 */}
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        <div className="w-14 h-14 bg-white/20 backdrop-blur-sm rounded-2xl flex items-center justify-center shadow-lg transform transition-transform duration-300 hover:rotate-12">
                            <svg className="w-7 h-7 drop-shadow-lg" fill="currentColor" viewBox="0 0 20 20" style={{ color: '#000000 !important' }}>
                                <path d="M8.433 7.418c.155-.103.346-.196.567-.267v1.698a2.305 2.305 0 01-.567-.267C8.07 8.34 8 8.114 8 8c0-.114.07-.34.433-.582zM11 12.849v-1.698c.22.071.412.164.567.267.364.243.433.468.433.582 0 .114-.07.34-.433.582a2.305 2.305 0 01-.567.267z"/>
                                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-13a1 1 0 10-2 0v.092a4.535 4.535 0 00-1.676.662C6.602 6.234 6 7.009 6 8c0 .99.602 1.765 1.324 2.246.48.32 1.054.545 1.676.662v1.941c-.391-.127-.68-.317-.843-.504a1 1 0 10-1.51 1.31c.562.649 1.413 1.076 2.353 1.253V15a1 1 0 102 0v-.092a4.535 4.535 0 001.676-.662C13.398 13.766 14 12.991 14 12c0-.99-.602-1.765-1.324-2.246A4.535 4.535 0 0011 9.092V7.151c.391.127.68.317.843.504a1 1 0 101.511-1.31c-.563-.649-1.413-1.076-2.354-1.253V5z" clipRule="evenodd"/>
                            </svg>
                        </div>
                        <div>
                            <h3 className="text-xl font-bold drop-shadow-sm" style={{ color: '#000000 !important' }}>보유 포인트</h3>
                            <p className="text-sm font-medium" style={{ color: '#000000 !important' }}>Smart Journey Points</p>
                        </div>
                    </div>
                    
                    {/* 상태 인디케이터 */}
                    <div className="flex items-center gap-2 px-3 py-1 bg-white/20 backdrop-blur-sm rounded-full">
                        <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse"></div>
                        <span className="text-xs font-medium" style={{ color: '#000000 !important' }}>활성</span>
                    </div>
                </div>
                
                {/* 메인 포인트 표시 */}
                <div className="space-y-3">
                    <div className={`transform transition-all duration-1000 ${isAnimated ? 'translate-y-0 opacity-100' : 'translate-y-4 opacity-0'}`}>
                        <div className="flex items-baseline gap-2">
                            <span className="text-5xl font-black drop-shadow-lg tracking-tight" style={{ color: '#000000 !important' }}>
                                {totalPoints.toLocaleString()}
                            </span>
                            <span className="text-2xl font-bold drop-shadow-md" style={{ color: '#000000 !important' }}>P</span>
                        </div>
                    </div>
                    
                    <div className="flex items-center gap-2 text-sm font-medium" style={{ color: '#000000 !important' }}>
                        <div className="w-5 h-5 bg-white/20 rounded-full flex items-center justify-center">
                            <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20" style={{ color: '#000000 !important' }}>
                                <path fillRule="evenodd" d="M4 4a2 2 0 00-2 2v4a2 2 0 002 2V6h10a2 2 0 00-2-2H4zm2 6a2 2 0 012-2h8a2 2 0 012 2v4a2 2 0 01-2 2H8a2 2 0 01-2-2v-4zm6 4a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd"/>
                            </svg>
                        </div>
                        <span>결제 시 현금처럼 사용 가능</span>
                    </div>
                </div>
                
                {/* 통계 카드들 */}
                <div className={`flex gap-3 transform transition-all duration-1000 delay-300 ${isAnimated ? 'translate-y-0 opacity-100' : 'translate-y-4 opacity-0'}`}>
                    <div className="flex-1 bg-white/15 backdrop-blur-sm rounded-2xl p-4 text-center border border-white/20 shadow-lg hover:bg-white/20 transition-all duration-300 group">
                        <div className="space-y-1">
                            <div className="text-xs font-semibold uppercase tracking-wide" style={{ color: '#000000 !important' }}>이번 달 적립</div>
                            <div className="text-xl font-bold drop-shadow-sm group-hover:scale-110 transition-transform duration-300" style={{ color: '#000000 !important' }}>
                                + 0P
                            </div>
                        </div>
                    </div>
                    
                    <div className="flex-1 bg-white/15 backdrop-blur-sm rounded-2xl p-4 text-center border border-white/20 shadow-lg hover:bg-white/20 transition-all duration-300 group">
                        <div className="space-y-1">
                            <div className="text-xs font-semibold uppercase tracking-wide" style={{ color: '#000000 !important' }}>총 사용</div>
                            <div className="text-xl font-bold drop-shadow-sm group-hover:scale-110 transition-transform duration-300" style={{ color: '#000000 !important' }}>
                                0P
                            </div>
                        </div>
                    </div>
                </div>
                
                {/* 추가 액션 힌트 */}
                <div className={`text-center transform transition-all duration-1000 delay-500 ${isAnimated ? 'translate-y-0 opacity-100' : 'translate-y-4 opacity-0'}`}>
                    <p className="text-xs font-medium" style={{ color: '#000000 !important' }}>탭하여 상세 내역 확인</p>
                </div>
            </div>
            
            {/* 호버 시 글로우 효과 */}
            <div className="absolute inset-0 bg-gradient-to-br from-white/5 to-transparent opacity-0 hover:opacity-100 transition-opacity duration-500 pointer-events-none"></div>
        </div>
    )
}