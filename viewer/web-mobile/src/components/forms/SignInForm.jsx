import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { signIn } from '../../api/auth'

export default function SignInForm() {
    const navigate = useNavigate()
    const [form, setForm] = useState({ email: '', password: '' })

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value })
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            await signIn(form)
            navigate('/')
        } catch {
            alert('로그인 실패! 다시 확인해 주세요.')
        }
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
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
                <button
                    type="submit"
                    className="btn-primary w-full py-3 text-lg rounded-xl shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-300"
                >
                    <span className="flex items-center justify-center gap-2">
                        <span>🚀</span>
                        로그인하기
                    </span>
                </button>

                <div className="text-center space-y-3">
                    <div className="flex items-center gap-4">
                        <div className="flex-1 h-px bg-gray-200"></div>
                        <span className="text-sm text-gray-500">또는</span>
                        <div className="flex-1 h-px bg-gray-200"></div>
                    </div>
                    
                    <p className="text-sm text-gray-600">
                        계정이 없으신가요?{' '}
                        <Link 
                            to="/sign-up" 
                            className="text-blue-600 hover:text-blue-700 font-medium hover:underline transition-colors"
                        >
                            회원가입하기
                        </Link>
                    </p>
                </div>
            </div>
        </form>
    )
}