import { useState, useEffect } from 'react'
import { getUserPoints } from '../api/user'

export default function PointSummary() {
    const [points, setPoints] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadPoints()
    }, [])

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

    if (loading) {
        return (
            <div className="bg-blue-50 border-l-4 border-blue-400 rounded p-4">
                <div className="animate-pulse">
                    <div className="h-4 bg-blue-200 rounded w-1/3 mb-2"></div>
                    <div className="h-6 bg-blue-200 rounded w-1/2"></div>
                </div>
            </div>
        )
    }

    const totalPoints = points?.totalPoints?.amount || 0

    return (
        <div className="bg-blue-50 border-l-4 border-blue-400 rounded p-4">
            <h3 className="text-sm font-medium text-blue-800 mb-1">보유 포인트</h3>
            <p className="text-2xl font-bold text-blue-900">
                {totalPoints.toLocaleString()}P
            </p>
            <p className="text-xs text-blue-600 mt-1">
                결제 시 현금처럼 사용 가능합니다
            </p>
        </div>
    )
}