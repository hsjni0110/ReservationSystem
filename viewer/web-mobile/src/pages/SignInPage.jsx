import AuthLayout from '../components/layouts/AuthLayout'
import SignInForm from '../components/forms/SignInForm'

export default function SignInPage() {
    return (
        <AuthLayout 
            title="환영합니다!" 
            subtitle="Smart Journey로 편리한 버스 여행을 시작하세요"
        >
            <SignInForm />
        </AuthLayout>
    )
}