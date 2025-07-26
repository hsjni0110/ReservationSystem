# 버스 운행 패턴 설정

import datetime
from .terminals import get_route_priority

# 특별 운행일 설정
SPECIAL_DAYS = {
    '2025-07-05': '토요일',  # 토요일 패턴 적용
    '2025-07-06': '일요일',  # 일요일 패턴 적용
    '2025-08-15': '광복절',  # 연휴 특별 패턴
}

# 연휴/성수기 증편 비율
TRAFFIC_MULTIPLIER = {
    '평일': 1.0,
    '금요일': 1.2,
    '토요일': 1.3,
    '일요일': 1.1,
    '광복절': 1.5,
    '성수기': 1.4,
}

# 월별 성수기 분류
PEAK_SEASONS = {
    7: '성수기',  # 여름휴가철
    8: '성수기',  # 여름휴가철
    12: '성수기', # 연말연시
    1: '성수기',  # 연말연시
}

def get_day_type(date):
    """날짜에 따른 요일 타입 반환"""
    date_str = date.strftime('%Y-%m-%d')
    
    # 특별일 체크
    if date_str in SPECIAL_DAYS:
        special_type = SPECIAL_DAYS[date_str]
        if special_type in ['토요일', '일요일']:
            return 'weekend'
        else:
            return 'special'
    
    # 일반 요일 체크
    weekday = date.weekday()  # 0=월요일, 6=일요일
    
    if weekday >= 5:  # 토요일(5), 일요일(6)
        return 'weekend'
    else:
        return 'weekday'

def get_traffic_multiplier(date):
    """날짜에 따른 운행량 배수 반환"""
    date_str = date.strftime('%Y-%m-%d')
    weekday = date.weekday()
    month = date.month
    
    # 특별일 체크
    if date_str in SPECIAL_DAYS:
        return TRAFFIC_MULTIPLIER.get(SPECIAL_DAYS[date_str], 1.0)
    
    # 성수기 체크
    base_multiplier = 1.0
    if month in PEAK_SEASONS:
        base_multiplier = TRAFFIC_MULTIPLIER['성수기']
    
    # 요일별 조정
    if weekday == 4:  # 금요일
        return base_multiplier * TRAFFIC_MULTIPLIER['금요일']
    elif weekday == 5:  # 토요일
        return base_multiplier * TRAFFIC_MULTIPLIER['토요일']
    elif weekday == 6:  # 일요일
        return base_multiplier * TRAFFIC_MULTIPLIER['일요일']
    else:  # 평일
        return base_multiplier * TRAFFIC_MULTIPLIER['평일']

def should_include_route(dep_region, arr_region, date):
    """특정 날짜에 해당 노선이 운행되는지 판단"""
    # 기본 우선순위 가져오기
    priority = get_route_priority(dep_region, arr_region)
    traffic_mult = get_traffic_multiplier(date)
    
    # 우선순위가 높거나 교통량이 많은 날에는 더 많은 노선 운행
    threshold = 0.3  # 기본 30% 확률
    
    if priority >= 4:
        threshold = 1.0  # 주요 노선은 항상 운행
    elif priority >= 3:
        threshold = 0.8  # 중요 노선은 80% 확률
    elif priority >= 2:
        threshold = 0.6  # 일반 노선은 60% 확률
    
    # 교통량에 따른 조정
    final_threshold = min(1.0, threshold * traffic_mult)
    
    return True if final_threshold >= 0.7 else False

def get_route_frequency(dep_region, arr_region, date):
    """노선의 일일 운행 빈도 반환 (시간대 개수)"""
    priority = get_route_priority(dep_region, arr_region)
    traffic_mult = get_traffic_multiplier(date)
    day_type = get_day_type(date)
    
    # 기본 빈도 설정
    base_frequency = {
        5: 6,  # 최고 우선순위: 6회
        4: 5,  # 높은 우선순위: 5회
        3: 4,  # 중간 우선순위: 4회
        2: 3,  # 낮은 우선순위: 3회
        1: 2,  # 최저 우선순위: 2회
    }
    
    frequency = base_frequency.get(priority, 2)
    
    # 주말에는 빈도 조정
    if day_type == 'weekend':
        frequency = max(2, int(frequency * 0.8))
    
    # 교통량에 따른 조정
    frequency = int(frequency * traffic_mult)
    
    return max(1, min(frequency, 8))  # 최소 1회, 최대 8회

def generate_july_dates():
    """2025년 7월 모든 날짜 생성"""
    dates = []
    for day in range(1, 32):  # 7월은 31일까지
        dates.append(datetime.date(2025, 7, day))
    return dates

def is_reverse_route_needed(dep_region, arr_region, date):
    """역방향 노선이 필요한지 판단"""
    # 주요 노선은 역방향도 운행
    priority = get_route_priority(dep_region, arr_region)
    
    if priority >= 3:
        return True
    elif priority >= 2:
        # 중간 우선순위는 80% 확률
        return get_traffic_multiplier(date) > 1.1
    else:
        # 낮은 우선순위는 주말에만
        return get_day_type(date) == 'weekend'