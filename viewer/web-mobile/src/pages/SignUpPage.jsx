import AuthLayout from '../components/layouts/AuthLayout'
import SignUpForm from '../components/forms/SignUpForm'

export default function SignUpPage() {
    return (
        <AuthLayout title="Create Account">
            <SignUpForm />
        </AuthLayout>
    )
}