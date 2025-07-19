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

    if (loading) {
        return (
            <div className="bg-white rounded shadow p-4">
                <h3 className="text-lg font-semibold text-gray-800 mb-3">포인트 내역</h3>
                <div className="space-y-3">
                    {[1, 2, 3].map(i => (
                        <div key={i} className="animate-pulse border-b border-gray-100 pb-3">
                            <div className="flex justify-between items-center">
                                <div className="h-4 bg-gray-200 rounded w-1/3"></div>
                                <div className="h-4 bg-gray-200 rounded w-1/4"></div>
                            </div>
                            <div className="h-3 bg-gray-200 rounded w-1/4 mt-1"></div>
                        </div>
                    ))}
                </div>
            </div>
        )
    }

    return (
        <div className="bg-white rounded shadow p-4">
            <h3 className="text-lg font-semibold text-gray-800 mb-3">포인트 내역</h3>
            {history.length === 0 ? (
                <p className="text-gray-500 text-sm text-center py-4">
                    포인트 내역이 없습니다.
                </p>
            ) : (
                <div className="space-y-3 max-h-64 overflow-y-auto">
                    {history.map((item) => (
                        <div key={item.historyId} className="border-b border-gray-100 pb-3 last:border-b-0">
                            <div className="flex justify-between items-center">
                                <span className="text-sm font-medium text-gray-800">
                                    {item.description}
                                </span>
                                <span className="text-sm font-bold text-blue-600">
                                    +{item.earnedPoints?.amount?.toLocaleString() || 0}P
                                </span>
                            </div>
                            <p className="text-xs text-gray-500 mt-1">
                                {formatDate(item.createdAt)}
                            </p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}