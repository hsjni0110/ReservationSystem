export default function AuthLayout({ title, children }) {
    return (
        <div className="flex items-center justify-center min-h-screen bg-white">
            <div className="w-full max-w-sm bg-gray-50 p-8 rounded shadow-md">
                <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">{title}</h1>
                {children}
            </div>
        </div>
    )
}