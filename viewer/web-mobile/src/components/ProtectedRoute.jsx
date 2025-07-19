import { Navigate } from 'react-router-dom'
import { isAuthenticated } from '../api/auth'

export default function ProtectedRoute({ children }) {
    if (!isAuthenticated()) {
        return <Navigate to="/sign-in" replace />
    }
    
    return children
}