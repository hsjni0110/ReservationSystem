import { useState, useEffect } from 'react'
import { getUserPointHistory } from '../api/user'

export default function PointHistory() {
    const [history, setHistory] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadHistory()
    }, [])

    const loadHistory = async () => {
        try {
            const data = await getUserPointHistory()
            setHistory(data)
        } catch (error) {
            console.error('포인트 내역 조회 실패:', error)
        } finally {
            setLoading(false)
        }
    }

    const formatDate = (dateString) => {
        const date = new Date(dateString)
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    const formatRelativeTime = (dateString) => {
        const date = new Date(dateString)
        const now = new Date()
        const diffInHours = Math.floor((now - date) / (1000 * 60 * 60))
        
        if (diffInHours < 1) return '방금 전'
        if (diffInHours < 24) return `${diffInHours}시간 전`
        if (diffInHours < 168) return `${Math.floor(diffInHours / 24)}일 전`
        return formatDate(dateString)
    }

    const LoadingSkeleton = () => (
        <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-sm border border-gray-100 p-6 animate-fade-in">
            <div className="flex items-center gap-4 mb-6">
                <div className="w-12 h-12 bg-gradient-to-r from-blue-100 to-indigo-100 rounded-2xl flex items-center justify-center">
                    <div className="w-6 h-6 bg-gradient-to-r from-blue-500 to-indigo-500 rounded animate-pulse"></div>
                </div>
                <div>
                    <div className="h-6 bg-gray-200 rounded-lg w-24 animate-pulse"></div>
                    <div className="h-4 bg-gray-100 rounded w-32 mt-2 animate-pulse"></div>
                </div>
            </div>
            
            <div className="space-y-4">
                {[1, 2, 3, 4].map(i => (
                    <div key={i} className="bg-gradient-to-r from-gray-50 to-gray-50/50 rounded-xl p-4 animate-pulse">
                        <div className="flex items-center gap-4">
                            <div className="w-12 h-12 bg-gray-200 rounded-xl"></div>
                            <div className="flex-1">
                                <div className="flex justify-between items-start">
                                    <div className="space-y-2">
                                        <div className="h-4 bg-gray-200 rounded w-32"></div>
                                        <div className="h-3 bg-gray-100 rounded w-20"></div>
                                    </div>
                                    <div className="h-5 bg-gray-200 rounded w-16"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )

    if (loading) {
        return <LoadingSkeleton />
    }

    return (
        <div className="bg-white/80 backdrop-blur-sm rounded-2xl shadow-sm border border-gray-100 overflow-hidden animate-fade-in">
            <div className="bg-gradient-to-r from-blue-50 to-indigo-50 p-6 border-b border-gray-100">
                <div className="flex items-center gap-4">
                    <div className="w-12 h-12 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-2xl flex items-center justify-center shadow-lg">
                        <svg className="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M4 4a2 2 0 00-2 2v4a2 2 0 002 2V6h10a2 2 0 00-2-2H4zm2 6a2 2 0 012-2h8a2 2 0 012 2v4a2 2 0 01-2 2H8a2 2 0 01-2-2v-4zm6 4a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd"/>
                        </svg>
                    </div>
                    <div>
                        <h3 className="text-xl font-bold text-gray-900">포인트 내역</h3>
                        <p className="text-sm text-gray-600 mt-1">적립된 포인트를 확인하세요</p>
                    </div>
                </div>
            </div>
            
            <div className="p-6">
                {history.length === 0 ? (
                    <div className="text-center py-16 space-y-6 animate-fade-in">
                        <div className="relative">
                            <div className="w-20 h-20 bg-gradient-to-r from-gray-100 to-gray-50 rounded-3xl flex items-center justify-center mx-auto shadow-sm">
                                <svg className="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="1.5">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                </svg>
                            </div>
                            <div className="absolute -top-1 -right-1 w-6 h-6 bg-yellow-100 rounded-full flex items-center justify-center">
                                <svg className="w-3 h-3 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd"/>
                                </svg>
                            </div>
                        </div>
                        <div className="space-y-3">
                            <h4 className="text-lg font-semibold text-gray-900">아직 포인트 내역이 없어요</h4>
                            <div className="space-y-2">
                                <p className="text-gray-600">버스를 예약하면 포인트가 적립됩니다</p>
                                <p className="text-sm text-gray-500 bg-gray-50 rounded-lg px-4 py-2 inline-block">
                                    💡 예약 완료 시 자동으로 포인트가 적립돼요
                                </p>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="space-y-3 max-h-96 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-gray-100">
                        {history.map((item, index) => (
                            <div 
                                key={item.historyId} 
                                className="group relative bg-gradient-to-r from-white to-gray-50/50 border border-gray-100 rounded-xl p-4 hover:shadow-md hover:border-blue-200 transition-all duration-300 transform hover:-translate-y-0.5 animate-slide-up"
                                style={{ animationDelay: `${index * 100}ms` }}
                            >
                                <div className="flex items-start gap-4">
                                    <div className="relative">
                                        <div className="w-12 h-12 bg-gradient-to-r from-green-100 to-emerald-100 rounded-xl flex items-center justify-center shadow-sm group-hover:shadow-md transition-shadow">
                                            <svg className="w-6 h-6 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                                                <path fillRule="evenodd" d="M4 4a2 2 0 00-2 2v4a2 2 0 002 2V6h10a2 2 0 00-2-2H4zm2 6a2 2 0 012-2h8a2 2 0 012 2v4a2 2 0 01-2 2H8a2 2 0 01-2-2v-4zm6 4a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd"/>
                                            </svg>
                                        </div>
                                        <div className="absolute -top-1 -right-1 w-5 h-5 bg-green-500 rounded-full flex items-center justify-center">
                                            <svg className="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                                                <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd"/>
                                            </svg>
                                        </div>
                                    </div>
                                    
                                    <div className="flex-1 min-w-0">
                                        <div className="flex justify-between items-start gap-4">
                                            <div className="flex-1">
                                                <h4 className="font-semibold text-gray-900 text-sm leading-5 mb-1 group-hover:text-blue-700 transition-colors">
                                                    {item.description}
                                                </h4>
                                                <div className="flex items-center gap-2 text-xs text-gray-500">
                                                    <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clipRule="evenodd"/>
                                                    </svg>
                                                    <span>{formatRelativeTime(item.createdAt)}</span>
                                                </div>
                                            </div>
                                            
                                            <div className="text-right flex-shrink-0">
                                                <div className="bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg px-3 py-1.5 border border-green-100">
                                                    <p className="font-bold text-green-700 text-sm">
                                                        +{item.earnedPoints?.amount?.toLocaleString() || 0}
                                                    </p>
                                                    <p className="text-xs text-green-600 font-medium">포인트</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div className="absolute inset-0 bg-gradient-to-r from-blue-500/5 to-indigo-500/5 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}