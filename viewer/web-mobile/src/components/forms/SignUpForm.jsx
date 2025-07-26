import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { signUp } from '../../api/auth'

export default function SignUpForm() {
    const navigate = useNavigate()
    const [form, setForm] = useState({
        email: '',
        password: '',
        name: '',
        phoneNumber: '',
    })

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value })
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            await signUp(form)
            alert('회원가입 성공! 로그인 페이지로 이동합니다.')
            navigate('/sign-in')
        } catch (error) {
            alert('회원가입 실패! 다시 시도해 주세요.')
        }
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <div className="form-group">
                        <label htmlFor="name" className="form-label">
                            <span className="flex items-center gap-2">
                                <span>👤</span>
                                이름
                            </span>
                        </label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            placeholder="이름을 입력하세요"
                            className="form-input"
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="phoneNumber" className="form-label">
                            <span className="flex items-center gap-2">
                                <span>📱</span>
                                전화번호
                            </span>
                        </label>
                        <input
                            type="text"
                            id="phoneNumber"
                            name="phoneNumber"
                            placeholder="전화번호 입력"
                            className="form-input"
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="email" className="form-label">
                        <span className="flex items-center gap-2">
                            <span>📧</span>
                            이메일
                        </span>
                    </label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="이메일을 입력하세요"
                        className="form-input"
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password" className="form-label">
                        <span className="flex items-center gap-2">
                            <span>🔒</span>
                            비밀번호
                        </span>
                    </label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="비밀번호를 입력하세요"
                        className="form-input"
                        onChange={handleChange}
                        required
                    />
                </div>
            </div>

            <div className="space-y-4">
                <div className="bg-blue-50 border border-blue-200 rounded-xl p-4">
                    <div className="flex items-start gap-3">
                        <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                            <span>💎</span>
                        </div>
                        <div className="space-y-1">
                            <p className="text-sm font-medium text-blue-800">Smart Journey 혜택</p>
                            <p className="text-xs text-blue-700">
                                회원가입 시 즉시 500P 적립! 예약할 때마다 포인트를 모아보세요.
                            </p>
                        </div>
                    </div>
                </div>

                <button
                    type="submit"
                    className="btn-primary w-full py-3 text-lg rounded-xl shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-300"
                >
                    <span className="flex items-center justify-center gap-2">
                        <span>✨</span>
                        회원가입하기
                    </span>
                </button>

                <div className="text-center space-y-3">
                    <div className="flex items-center gap-4">
                        <div className="flex-1 h-px bg-gray-200"></div>
                        <span className="text-sm text-gray-500">또는</span>
                        <div className="flex-1 h-px bg-gray-200"></div>
                    </div>
                    
                    <p className="text-sm text-gray-600">
                        이미 계정이 있으신가요?{' '}
                        <Link 
                            to="/sign-in" 
                            className="text-blue-600 hover:text-blue-700 font-medium hover:underline transition-colors"
                        >
                            로그인하기
                        </Link>
                    </p>
                </div>
            </div>
        </form>
    )
}