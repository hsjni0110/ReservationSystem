#!/usr/bin/env python3
"""
🚌 버스 예약 시스템 동적 더미 데이터 생성기

이 스크립트는 Jinja2 템플릿을 사용하여 대량의 버스 노선 데이터를 
동적으로 생성합니다.

사용법:
    python generate_dummy_data.py --month 7 --year 2025 --output ../generated/complete_dummy_data.sql
    
작성자: Claude Code
생성일: 2025-07-26
"""

import argparse
import datetime
import os
import sys
from collections import defaultdict, Counter
from itertools import combinations
from jinja2 import Environment, FileSystemLoader

# 설정 파일 import
from config.terminals import TERMINALS, get_region_by_terminal, get_route_priority
from config.time_slots import get_time_slots_for_route, get_distance_type
from config.patterns import (
    generate_july_dates, get_day_type, should_include_route, 
    get_route_frequency, is_reverse_route_needed, get_traffic_multiplier
)

class DummyDataGenerator:
    def __init__(self, year=2025, month=7):
        self.year = year
        self.month = month
        self.buses = []
        self.routes = []
        self.time_slots = []
        self.region_stats = []
        
    def generate_buses(self, count=50):
        """버스 데이터 생성"""
        bus_types = [
            {'name': '프리미엄버스', 'capacity': 25, 'ratio': 0.2},
            {'name': '우등고속버스', 'capacity': 28, 'ratio': 0.4},
            {'name': '일반고속버스', 'capacity': 45, 'ratio': 0.4}
        ]
        
        # 지역별 번호판 prefix
        regions = ['서울12가', '부산12바', '대구12사', '광주12아', '대전12자', '인천12타', '울산12차']
        
        bus_id = 1
        for bus_type in bus_types:
            type_count = int(count * bus_type['ratio'])
            for i in range(type_count):
                region = regions[i % len(regions)]
                bus_number = f"{region}{1000 + bus_id:04d}"
                
                self.buses.append({
                    'bus_id': bus_id,
                    'created_at': '2024-01-01 10:00:00',
                    'updated_at': '2024-01-01 10:00:00',
                    'bus_name': bus_type['name'],
                    'bus_number': bus_number,
                    'capacity': bus_type['capacity']
                })
                bus_id += 1
        
        print(f"✅ 버스 {len(self.buses)}대 생성 완료")
        
    def generate_routes(self):
        """노선 데이터 생성"""
        route_id = 1
        dates = generate_july_dates()
        region_route_counts = Counter()
        
        print(f"📅 {len(dates)}일간의 노선 데이터 생성 중...")
        
        for date in dates:
            day_type = get_day_type(date)
            day_routes = 0
            
            # 모든 권역 조합 생성
            for region1, terminals1 in TERMINALS.items():
                for region2, terminals2 in TERMINALS.items():
                    # 같은 권역 내 노선도 포함
                    if not should_include_route(region1, region2, date):
                        continue
                    
                    # 터미널 조합 선택 (모든 조합은 너무 많으므로 샘플링)
                    terminal_pairs = self._select_terminal_pairs(terminals1, terminals2, region1, region2)
                    
                    for dep_terminal, arr_terminal in terminal_pairs:
                        # 정방향 노선
                        self.routes.append({
                            'route_id': route_id,
                            'departure': dep_terminal,
                            'arrival': arr_terminal,
                            'schedule_date': date.strftime('%Y-%m-%d')
                        })
                        route_id += 1
                        day_routes += 1
                        region_route_counts[(region1, region2)] += 1
                        
                        # 역방향 노선 (필요한 경우)
                        if is_reverse_route_needed(region1, region2, date):
                            self.routes.append({
                                'route_id': route_id,
                                'departure': arr_terminal,
                                'arrival': dep_terminal,
                                'schedule_date': date.strftime('%Y-%m-%d')
                            })
                            route_id += 1
                            day_routes += 1
                            region_route_counts[(region2, region1)] += 1
            
            print(f"  📍 {date.strftime('%Y-%m-%d')} ({self._get_weekday_ko(date)}): {day_routes}개 노선")
        
        # 권역별 통계 생성
        self._generate_region_stats(region_route_counts)
        
        print(f"✅ 총 {len(self.routes)}개 노선 생성 완료")
        
    def _select_terminal_pairs(self, terminals1, terminals2, region1, region2):
        """터미널 조합 선택 (효율적인 샘플링)"""
        priority = get_route_priority(region1, region2)
        
        # 우선순위에 따른 터미널 조합 수 결정
        if priority >= 4:
            # 고우선순위: 더 많은 조합
            max_pairs = min(8, len(terminals1) * len(terminals2))
        elif priority >= 3:
            max_pairs = min(4, len(terminals1) * len(terminals2))
        elif priority >= 2:
            max_pairs = min(2, len(terminals1) * len(terminals2))
        else:
            max_pairs = 1
        
        # 모든 조합 생성 후 샘플링
        all_pairs = [(t1, t2) for t1 in terminals1 for t2 in terminals2]
        
        if len(all_pairs) <= max_pairs:
            return all_pairs
        
        # 중요한 터미널 우선 선택 (이름에 '고속버스터미널' 포함)
        priority_pairs = [(t1, t2) for t1, t2 in all_pairs 
                         if '고속버스터미널' in t1 or '고속버스터미널' in t2]
        
        if len(priority_pairs) >= max_pairs:
            return priority_pairs[:max_pairs]
        else:
            # 우선순위 터미널 + 일반 터미널 조합
            remaining = max_pairs - len(priority_pairs)
            other_pairs = [pair for pair in all_pairs if pair not in priority_pairs]
            return priority_pairs + other_pairs[:remaining]
    
    def generate_time_slots(self):
        """시간대 슬롯 생성"""
        slot_id = 1
        route_time_counts = Counter()
        
        print("⏰ 시간대 슬롯 생성 중...")
        
        for route in self.routes:
            # 출발/도착 터미널의 권역 찾기
            dep_region = get_region_by_terminal(route['departure'])
            arr_region = get_region_by_terminal(route['arrival'])
            
            # 날짜 정보로 요일 타입 결정
            date = datetime.datetime.strptime(route['schedule_date'], '%Y-%m-%d').date()
            day_type = get_day_type(date)
            
            # 해당 노선의 운행 빈도 결정
            frequency = get_route_frequency(dep_region, arr_region, date)
            
            # 시간대 가져오기
            available_times = get_time_slots_for_route(dep_region, arr_region, day_type)
            
            # 빈도에 맞게 시간대 선택
            selected_times = available_times[:frequency] if len(available_times) >= frequency else available_times
            
            for time_str in selected_times:
                self.time_slots.append({
                    'slot_id': slot_id,
                    'route_id': route['route_id'],
                    'time': time_str
                })
                slot_id += 1
                route_time_counts[route['route_id']] += 1
        
        print(f"✅ 총 {len(self.time_slots)}개 시간대 슬롯 생성 완료")
        print(f"  📊 노선당 평균 {sum(route_time_counts.values()) / len(route_time_counts):.1f}개 시간대")
    
    def _generate_region_stats(self, region_route_counts):
        """권역별 통계 생성"""
        for (region_from, region_to), count in region_route_counts.most_common():
            # 해당 권역 조합의 평균 시간대 수 계산
            relevant_routes = [r for r in self.routes 
                             if (get_region_by_terminal(r['departure']) == region_from and 
                                 get_region_by_terminal(r['arrival']) == region_to)]
            
            total_time_slots = sum(1 for ts in self.time_slots 
                                 if any(ts['route_id'] == r['route_id'] for r in relevant_routes))
            
            avg_time_slots = total_time_slots / count if count > 0 else 0
            
            self.region_stats.append({
                'region_from': region_from,
                'region_to': region_to,
                'route_count': count,
                'avg_time_slots': round(avg_time_slots, 1)
            })
    
    def _get_weekday_ko(self, date):
        """한국어 요일 반환"""
        weekdays = ['월', '화', '수', '목', '금', '토', '일']
        return weekdays[date.weekday()]
    
    def generate_sql(self, template_path, output_path):
        """SQL 파일 생성"""
        print(f"📝 SQL 파일 생성 중: {output_path}")
        
        # Jinja2 환경 설정
        template_dir = os.path.dirname(template_path)
        template_name = os.path.basename(template_path)
        
        env = Environment(loader=FileSystemLoader(template_dir))
        template = env.get_template(template_name)
        
        # 템플릿 변수 준비
        template_vars = {
            'generation_time': datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
            'start_date': f'{self.year}-{self.month:02d}-01',
            'end_date': f'{self.year}-{self.month:02d}-31',
            'buses': self.buses,
            'routes': self.routes,
            'time_slots': self.time_slots,
            'region_stats': self.region_stats,
            'bus_count': len(self.buses),
            'total_routes': len(self.routes),
            'total_time_slots': len(self.time_slots),
            'max_capacity': max(bus['capacity'] for bus in self.buses),
            'reservation_rate': 75,  # 25% 예약률
            'prices': {
                'premium': {'high': 38000, 'low': 35000},
                'deluxe': {'high': 28000, 'low': 25000},
                'standard': {'high': 21000, 'low': 18000}
            }
        }
        
        # SQL 생성
        sql_content = template.render(**template_vars)
        
        # 파일 저장
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(sql_content)
        
        print(f"✅ SQL 파일 생성 완료: {output_path}")
        print(f"  📄 파일 크기: {len(sql_content.splitlines())}줄")
        
    def generate_all(self, bus_count=50, template_path=None, output_path=None):
        """전체 데이터 생성 프로세스"""
        print(f"🚀 {self.year}년 {self.month}월 더미 데이터 생성 시작")
        print("=" * 60)
        
        # 단계별 생성
        self.generate_buses(bus_count)
        self.generate_routes()
        self.generate_time_slots()
        
        # 요약 정보 출력
        print("\n📊 생성 결과 요약:")
        print(f"  🚍 버스: {len(self.buses)}대")
        print(f"  🛣️  노선: {len(self.routes)}개")
        print(f"  ⏰  시간대: {len(self.time_slots)}개")
        print(f"  📈  예상 좌석 수: {sum(bus['capacity'] for bus in self.buses) * len(self.time_slots):,}개")
        
        # SQL 생성
        if template_path and output_path:
            print("\n" + "=" * 60)
            self.generate_sql(template_path, output_path)
        
        print("\n🎉 모든 작업 완료!")

def main():
    parser = argparse.ArgumentParser(description='버스 예약 시스템 더미 데이터 생성기')
    parser.add_argument('--year', type=int, default=2025, help='생성할 년도 (기본값: 2025)')
    parser.add_argument('--month', type=int, default=7, help='생성할 월 (기본값: 7)')
    parser.add_argument('--buses', type=int, default=50, help='생성할 버스 수 (기본값: 50)')
    parser.add_argument('--template', type=str, 
                       default='templates/dummy_data_template.sql.j2',
                       help='템플릿 파일 경로')
    parser.add_argument('--output', type=str,
                       default='../generated/complete_dummy_data.sql',
                       help='출력 SQL 파일 경로')

    args = parser.parse_args()
    
    # 경로 정규화
    template_path = os.path.abspath(args.template)
    output_path = os.path.abspath(args.output)
    
    # 템플릿 파일 존재 확인
    if not os.path.exists(template_path):
        print(f"❌ 템플릿 파일을 찾을 수 없습니다: {template_path}")
        sys.exit(1)
    
    # 생성기 실행
    generator = DummyDataGenerator(args.year, args.month)
    generator.generate_all(args.buses, template_path, output_path)

if __name__ == '__main__':
    main()