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
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div className="flex flex-col gap-1">
                <label htmlFor="email" className="text-sm text-gray-700 font-medium">Email</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    placeholder="Enter your email"
                    className="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
                    onChange={handleChange}
                />
            </div>

            <div className="flex flex-col gap-1">
                <label htmlFor="password" className="text-sm text-gray-700 font-medium">Password</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Enter your password"
                    className="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
                    onChange={handleChange}
                />
            </div>

            <button
                type="submit"
                className="bg-blue-600 hover:bg-blue-700 text-white rounded py-2 font-semibold transition-colors"
            >
                Sign In
            </button>

            <p className="text-sm text-center text-gray-600">
                Don&apos;t have an account?{' '}
                <Link to="/sign-up" className="text-blue-600 hover:underline">Sign up</Link>
            </p>
        </form>
    )
}