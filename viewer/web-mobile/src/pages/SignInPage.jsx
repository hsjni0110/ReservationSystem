import AuthLayout from '../components/layouts/AuthLayout'
import SignInForm from '../components/forms/SignInForm'

export default function SignInPage() {
    return (
        <AuthLayout title="Login to Bus Reservation">
            <SignInForm />
        </AuthLayout>
    )
}