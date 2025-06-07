import { useState } from 'react';
import { getAvailableRouteSchedules } from '../../api/route';

const regions = [
    { name: '서울', terminals: ['서울고속버스터미널', '센트럴시티터미널', '동서울종합터미널', '서울남부터미널'] },
    { name: '인천/경기', terminals: ['인천종합버스터미널', '수원종합버스터미널', '고양종합터미널', '부천터미널 소풍'] },
    { name: '부산', terminals: ['부산종합버스터미널', '부산서부시외버스터미널'] },
    { name: '대구', terminals: ['동대구터미널', '서대구고속버스터미널'] },
    { name: '광주', terminals: ['광주종합버스터미널'] },
    { name: '대전', terminals: ['대전복합버스터미널'] },
    { name: '울산', terminals: ['울산고속버스터미널'] },
    { name: '강원', terminals: ['춘천고속버스터미널', '강릉고속버스터미널', '원주고속버스터미널'] },
    { name: '충북', terminals: ['청주고속버스터미널', '충주공용버스터미널'] },
    { name: '충남', terminals: ['천안고속버스터미널', '아산온양고속버스터미널'] },
    { name: '전북', terminals: ['전주고속버스터미널', '군산고속버스터미널'] },
    { name: '전남', terminals: ['목포고속버스터미널', '순천종합버스터미널'] },
    { name: '경북', terminals: ['포항고속버스터미널', '경주고속버스터미널'] },
    { name: '경남', terminals: ['창원종합버스터미널', '진주고속버스터미널'] },
];

export default function SearchForm({ onSearchResults }) {
    const [step, setStep] = useState('departureRegion');
    const [departureRegion, setDepartureRegion] = useState('');
    const [departure, setDeparture] = useState('');
    const [arrivalRegion, setArrivalRegion] = useState('');
    const [arrival, setArrival] = useState('');
    const [date, setDate] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const results = await getAvailableRouteSchedules({
                departure,
                arrival,
                scheduleDate: date,
            });
            onSearchResults(results);
        } catch (error) {
            console.error(error);
            alert('스케줄을 불러오는 데 실패했습니다.');
        }
    };

    const goBack = () => {
        if (step === 'departureTerminal') {
            setStep('departureRegion');
            setDepartureRegion('');
        } else if (step === 'arrivalRegion') {
            setStep('departureTerminal');
            setDeparture('');
        } else if (step === 'arrivalTerminal') {
            setStep('arrivalRegion');
            setArrivalRegion('');
        } else if (step === 'date') {
            setStep('arrivalTerminal');
            setArrival('');
        }
    };

    return (
        <div className="bg-white rounded shadow p-4 flex flex-col gap-4 text-sm">
            {(step !== 'departureRegion') && (
                <button
                    onClick={goBack}
                    className="text-blue-600 text-left text-xs hover:underline"
                >
                    ← 뒤로 가기
                </button>
            )}

            {step === 'departureRegion' && (
                <div>
                    <h2 className="font-semibold mb-2">출발지 지역 선택</h2>
                    <div className="grid grid-cols-3 gap-2">
                        {regions.map((r) => (
                            <button
                                key={r.name}
                                onClick={() => {
                                    setDepartureRegion(r.name);
                                    setStep('departureTerminal');
                                }}
                                className="bg-gray-100 rounded py-2 text-center"
                            >
                                {r.name}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {step === 'departureTerminal' && (
                <div>
                    <h2 className="font-semibold mb-2">출발지 터미널 선택 ({departureRegion})</h2>
                    <div className="grid grid-cols-2 gap-2">
                        {regions.find((r) => r.name === departureRegion)?.terminals.map((t) => (
                            <button
                                key={t}
                                onClick={() => {
                                    setDeparture(t);
                                    setStep('arrivalRegion');
                                }}
                                className="bg-gray-100 rounded py-2 text-center"
                            >
                                {t}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {step === 'arrivalRegion' && (
                <div>
                    <h2 className="font-semibold mb-2">도착지 지역 선택</h2>
                    <div className="grid grid-cols-3 gap-2">
                        {regions.map((r) => (
                            <button
                                key={r.name}
                                onClick={() => {
                                    setArrivalRegion(r.name);
                                    setStep('arrivalTerminal');
                                }}
                                className="bg-gray-100 rounded py-2 text-center"
                            >
                                {r.name}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {step === 'arrivalTerminal' && (
                <div>
                    <h2 className="font-semibold mb-2">도착지 터미널 선택 ({arrivalRegion})</h2>
                    <div className="grid grid-cols-2 gap-2">
                        {regions.find((r) => r.name === arrivalRegion)?.terminals.map((t) => (
                            <button
                                key={t}
                                onClick={() => {
                                    setArrival(t);
                                    setStep('date');
                                }}
                                className={`bg-gray-100 rounded py-2 text-center ${
                                    arrival === t ? 'ring-2 ring-blue-500' : ''
                                }`}
                            >
                                {t}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {step === 'date' && (
                <div>
                    <h2 className="font-semibold mb-2">날짜 선택</h2>
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        className="border border-gray-300 rounded px-3 py-2 w-full"
                        required
                    />
                    <button
                        onClick={handleSubmit}
                        className="w-full bg-blue-600 text-white rounded py-2 mt-2"
                    >
                        검색하기
                    </button>
                </div>
            )}
        </div>
    );
}