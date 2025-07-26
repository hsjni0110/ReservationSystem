import AuthLayout from '../components/layouts/AuthLayout'
import SignUpForm from '../components/forms/SignUpForm'

export default function SignUpPage() {
    return (
        <AuthLayout 
            title="회원가입" 
            subtitle="몇 분만에 가입하고 특별한 여행 혜택을 받아보세요"
        >
            <SignUpForm />
        </AuthLayout>
    )
}