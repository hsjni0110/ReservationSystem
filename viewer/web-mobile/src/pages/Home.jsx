import { useState } from 'react';
import SearchForm from '../components/forms/SearchForm';

export default function Home() {
    const [showSearch, setShowSearch] = useState(false); // 검색창 표시 여부
    const [schedules, setSchedules] = useState([]);

    return (
        <div className="flex flex-col min-h-screen bg-gray-50">
            {/* 헤더 */}
            <header className="bg-blue-600 text-white py-4 px-6 shadow flex justify-between items-center">
                <h1 className="text-lg font-bold">고속버스 예약</h1>
                {/* 필요하면 여기에 메뉴 버튼/로고 등 */}
            </header>

            {/* 메인 */}
            <main className="flex-1 p-4 flex flex-col gap-4">
                {/* 상단 안내 */}
                <div className="bg-white rounded shadow p-4 flex flex-col items-center text-center gap-2">
                    <h2 className="text-base font-semibold text-gray-800">국내 고속버스 예약 서비스</h2>
                    <p className="text-sm text-gray-600">편리하게 출발지와 도착지를 선택하고 예약하세요!</p>
                    <button
                        onClick={() => setShowSearch(true)}
                        className="bg-blue-600 hover:bg-blue-700 text-white rounded-full px-6 py-2 text-sm font-semibold transition-colors mt-2"
                    >
                        예약 경로 검색하기
                    </button>
                </div>

                {/* 검색창 */}
                {showSearch && (
                    <div className="bg-white rounded shadow p-4">
                        <SearchForm onSearchResults={setSchedules} />
                        <button
                            onClick={() => setShowSearch(false)}
                            className="text-blue-600 hover:underline text-xs mt-2"
                        >
                            검색 닫기
                        </button>
                    </div>
                )}

                {/* 현재 예약 현황 */}
                <div className="bg-white rounded shadow p-4">
                    <h2 className="text-base font-semibold text-gray-800 mb-2">현재 예약 현황</h2>
                    {schedules.length === 0 ? (
                        <p className="text-sm text-gray-500">아직 검색된 예약 정보가 없습니다.</p>
                    ) : (
                        <ul className="flex flex-col gap-2">
                            {schedules.map((s, index) => (
                                <li key={index} className="border border-gray-200 rounded p-3 text-sm bg-gray-50">
                                    <p>출발지: {s.departure}</p>
                                    <p>도착지: {s.arrival}</p>
                                    <p>출발 시간: {s.timeSlot}</p>
                                    <p>버스 ID: {s.busId}</p>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </main>
        </div>
    );
}