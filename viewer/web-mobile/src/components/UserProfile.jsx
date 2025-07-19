import { useState, useEffect } from 'react'
import { getUserProfile } from '../api/user'

export default function UserProfile() {
    const [profile, setProfile] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadProfile()
    }, [])

    const loadProfile = async () => {
        try {
            const data = await getUserProfile()
            setProfile(data)
        } catch (error) {
            console.error('프로필 조회 실패:', error)
        } finally {
            setLoading(false)
        }
    }

    if (loading) {
        return (
            <div className="bg-white rounded shadow p-4">
                <div className="animate-pulse">
                    <div className="h-4 bg-gray-200 rounded w-1/4 mb-2"></div>
                    <div className="h-3 bg-gray-200 rounded w-1/2 mb-1"></div>
                    <div className="h-3 bg-gray-200 rounded w-1/3"></div>
                </div>
            </div>
        )
    }

    if (!profile) {
        return (
            <div className="bg-white rounded shadow p-4">
                <p className="text-gray-500">프로필을 불러올 수 없습니다.</p>
            </div>
        )
    }

    return (
        <div className="bg-white rounded shadow p-4">
            <h2 className="text-lg font-semibold text-gray-800 mb-3">프로필 정보</h2>
            <div className="space-y-2">
                <div>
                    <span className="text-sm text-gray-600">이름:</span>
                    <span className="ml-2 text-sm font-medium">{profile.name}</span>
                </div>
                <div>
                    <span className="text-sm text-gray-600">이메일:</span>
                    <span className="ml-2 text-sm font-medium">{profile.email}</span>
                </div>
                <div>
                    <span className="text-sm text-gray-600">전화번호:</span>
                    <span className="ml-2 text-sm font-medium">{profile.phoneNumber}</span>
                </div>
            </div>
        </div>
    )
}