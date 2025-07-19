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

            <div className="flex flex-col gap-1">
                <label htmlFor="name" className="text-sm text-gray-700 font-medium">Name</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    placeholder="Enter your name"
                    className="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
                    onChange={handleChange}
                />
            </div>

            <div className="flex flex-col gap-1">
                <label htmlFor="phoneNumber" className="text-sm text-gray-700 font-medium">Phone Number</label>
                <input
                    type="text"
                    id="phoneNumber"
                    name="phoneNumber"
                    placeholder="Enter your phone number"
                    className="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
                    onChange={handleChange}
                />
            </div>

            <button
                type="submit"
                className="bg-blue-600 hover:bg-blue-700 text-white rounded py-2 font-semibold transition-colors"
            >
                Sign Up
            </button>

            <p className="text-sm text-center text-gray-600">
                Already have an account?{' '}
                <Link to="/sign-in" className="text-blue-600 hover:underline">Sign in</Link>
            </p>
        </form>
    )
}