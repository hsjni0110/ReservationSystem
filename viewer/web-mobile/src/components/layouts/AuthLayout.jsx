import { useNavigate } from 'react-router-dom'

export default function AuthLayout({ title, subtitle, children }) {
    const navigate = useNavigate()

    return (
        <div className="flex flex-col min-h-screen" style={{ backgroundColor: 'var(--color-neutral-50)' }}>
            {/* Header */}
            <header className="hero-gradient text-white py-6 px-6 shadow-lg">
                <div className="flex items-center gap-3">
                    <button
                        onClick={() => navigate('/')}
                        className="flex items-center gap-3 text-white hover:text-blue-100 transition-colors"
                    >
                        <span className="text-xl">←</span>
                        <div className="flex items-center gap-2">
                            <div className="w-8 h-8 bg-white bg-opacity-20 rounded-full flex items-center justify-center">
                                <span className="text-lg">🚌</span>
                            </div>
                            <h1 className="brand-logo text-xl">Smart Journey</h1>
                        </div>
                    </button>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-1 flex items-center justify-center p-6">
                <div className="w-full max-w-md">
                    {/* Auth Card */}
                    <div className="card space-y-6">
                        {/* Title Section */}
                        <div className="text-center space-y-3">
                            <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center mx-auto shadow-lg">
                                <span className="text-2xl text-white">✈️</span>
                            </div>
                            <div className="space-y-2">
                                <h2 className="text-2xl font-bold gradient-text">{title}</h2>
                                {subtitle && (
                                    <p className="text-gray-600">{subtitle}</p>
                                )}
                            </div>
                        </div>

                        {/* Form */}
                        {children}
                    </div>

                    {/* Footer */}
                    <div className="text-center mt-8 space-y-2">
                        <p className="text-sm text-gray-500">
                            스마트한 여행의 시작
                        </p>
                        <div className="flex items-center justify-center gap-4 text-xs text-gray-400">
                            <span>🔒 안전한 결제</span>
                            <span>•</span>
                            <span>⚡ 빠른 예약</span>
                            <span>•</span>
                            <span>💎 포인트 적립</span>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    )
}