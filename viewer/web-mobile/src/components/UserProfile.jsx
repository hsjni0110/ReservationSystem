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
        <div className="card">
            <div className="card-header">
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <svg className="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                            <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd"/>
                        </svg>
                    </div>
                    <h3 className="text-xl font-bold text-gray-800">프로필 정보</h3>
                </div>
            </div>
            
            <div className="space-y-4">
                <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                    <div className="w-16 h-16 bg-gradient-to-br from-blue-400 to-purple-500 rounded-full flex items-center justify-center text-white font-bold text-xl">
                        {profile.name?.charAt(0) || '?'}
                    </div>
                    <div className="flex-1">
                        <h4 className="font-bold text-lg text-gray-800">{profile.name}</h4>
                        <p className="text-sm text-gray-600">Smart Journey 회원</p>
                    </div>
                </div>
                
                <div className="space-y-3">
                    <div className="flex items-center gap-3 p-3 border border-gray-200 rounded-lg">
                        <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                            <svg className="w-4 h-4 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z"/>
                                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z"/>
                            </svg>
                        </div>
                        <div className="flex-1">
                            <div className="text-xs text-gray-500 font-medium">이메일</div>
                            <div className="text-sm font-medium text-gray-800">{profile.email}</div>
                        </div>
                    </div>
                    
                    <div className="flex items-center gap-3 p-3 border border-gray-200 rounded-lg">
                        <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                            <svg className="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z"/>
                            </svg>
                        </div>
                        <div className="flex-1">
                            <div className="text-xs text-gray-500 font-medium">전화번호</div>
                            <div className="text-sm font-medium text-gray-800">{profile.phoneNumber}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}