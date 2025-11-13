# 🚭 Quitmate - 금연 관리 앱

> 사용자 맞춤형 금연 지원 및 커뮤니티 기반 동기부여 시스템

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)

## 📋 프로젝트 소개

Quitmate은 금연을 시도하는 사용자들을 위한 종합 관리 플랫폼입니다. 개인별 흡연 패턴 분석, 맞춤형 미션 제공, 친구 기능을 통한 동기부여, 그리고 실시간 알림을 통해 효과적인 금연을 지원합니다.


## 📱 API 문서

Spring REST Docs를 활용한 자동화된 API 문서를 제공합니다.

```
https://api-dev.quitmate.co.kr/docs/index.html
```

### 주요 기능

#### 👤 사용자 관리
- **소셜 로그인**: Kakao, Google OAuth 2.0 연동
- **JWT 기반 인증**: Access Token / Refresh Token을 활용한 안전한 인증 시스템
- **사용자 프로필**: 금연 목표, 흡연 이력, 개인 통계 관리

#### 📊 금연 데이터 분석
- **실시간 흡연 기록**: 흡연 시간, 횟수, 장소 추적
- **통계 대시보드**: 주간/월간 흡연 패턴 분석 및 시각화
- **금연 시간 계산**: 금연 시작 후 경과 시간 및 절약 금액 계산
- **비교 분석**: 전주 대비 금연 성과 비교 (흡연 횟수, 평균 금연 시간)

#### 🎯 챌린지 & 미션 시스템
- **일일/주간 미션**: 단계별 금연 목표 달성 미션
- **챌린지 참여**: 그룹 기반 금연 챌린지 생성 및 참여
- **보상 시스템**: 미션 완료 시 포인트 및 뱃지 지급
- **진행 상황 추적**: 실시간 챌린지 진행률 모니터링

#### 👥 소셜 기능
- **친구 관리**: 친구 추가, 친구 금연 현황 확인
- **커뮤니티**: 금연 성공 사례 공유 및 응원 메시지
- **랭킹 시스템**: 금연 기간 기반 리더보드

#### 🔔 알림 시스템
- **Firebase Cloud Messaging (FCM)**: 실시간 푸시 알림
- **맞춤형 알림**: 흡연 예방 알림, 미션 리마인더, 응원 메시지
- **알림 히스토리**: 과거 알림 내역 조회 및 관리

#### 📝 설문조사 & 문의
- **니코틴 의존도 검사**: 과학적 설문을 통한 흡연 심각도 평가
- **1:1 문의**: 사용자 피드백 및 건의사항 관리

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.4.2
- **Language**: Java 17
- **Build Tool**: Gradle 8.12.1

### Database
- **Primary DB**: MySQL 8.x (사용자 데이터, 트랜잭션 데이터)
- **PostgreSQL**: 마이그레이션 지원
- **NoSQL**: MongoDB (로그 데이터, 비정형 데이터)
- **ORM**: Spring Data JPA, QueryDSL 5.0.0

### Security & Authentication
- **Spring Security 6.x**: 인증 및 권한 관리
- **JWT (Json Web Token)**: Stateless 인증
- **AES 암호화**: 민감 정보 암호화

### Cloud & Infrastructure
- **Cloud Storage**: Oracle Cloud Infrastructure (OCI) Object Storage
- **Push Notification**: Firebase Cloud Messaging (FCM)
- **Email**: Spring Mail (SMTP)
- **CI/CD**: GitHub Actions

### Batch & Scheduling
- **Spring Scheduler**: 주기적 작업 실행 (통계 집계, 알림 발송)

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/addiction/
│   │   ├── user/              # 사용자 관리
│   │   │   ├── users/         # 사용자 엔티티 및 비즈니스 로직
│   │   │   ├── userCigarette/ # 사용자 흡연 데이터
│   │   │   ├── userCigaretteHistory/ # 흡연 이력 추적
│   │   │   ├── push/          # 푸시 알림 관리
│   │   │   └── refreshToken/  # JWT 리프레시 토큰
│   │   ├── challenge/         # 챌린지 시스템
│   │   │   ├── challenge/     # 챌린지 관리
│   │   │   ├── mission/       # 미션 관리
│   │   │   ├── challengehistory/ # 챌린지 참여 이력
│   │   │   ├── missionhistory/    # 미션 수행 이력
│   │   │   └── rewardHistory/ # 보상 지급 이력
│   │   ├── friend/            # 친구 관리
│   │   ├── alertSetting/      # 알림 설정
│   │   ├── alertHistory/      # 알림 히스토리
│   │   ├── survey/            # 설문조사
│   │   ├── inquiry/           # 문의 관리
│   │   ├── notice/            # 공지사항
│   │   ├── storage/           # 파일 스토리지 (OCI)
│   │   ├── firebase/          # FCM 통합
│   │   ├── jwt/               # JWT 인증
│   │   ├── batch/             # 배치 작업
│   │   └── global/            # 공통 설정 및 예외 처리
│   └── resources/
│       ├── application-dev.yml    # 개발 환경 설정
│       ├── application-prd.yml    # 운영 환경 설정
│       └── logback/               # 로깅 설정
└── test/                      # 단위 테스트 및 통합 테스트
    └── docs/                  # REST Docs 테스트
```

## 🏗 아키텍처

### Layered Architecture
```
Controller (API Layer)
    ↓
Service (Business Logic Layer)
    ↓
Repository (Data Access Layer)
    ↓
Database (MySQL, MongoDB)
```

### 주요 설계 패턴
- **DTO Pattern**: Request/Response 객체 분리
- **Repository Pattern**: 데이터 접근 추상화
- **Service Layer Pattern**: 비즈니스 로직 캡슐화
- **Global Exception Handler**: 통합 예외 처리
- **OpenFeign**: 선언적 HTTP 클라이언트

## 🔐 보안 기능

### 인증 & 인가
- **JWT 기반 Stateless 인증**
    - Access Token (30분 유효)
    - Refresh Token (14일 유효)
- **OAuth 2.0 소셜 로그인**
    - Kakao, Google, Naver 연동
- **Spring Security**
    - 엔드포인트별 권한 제어
    - CORS 설정

## 📊 데이터베이스 설계

### 주요 엔티티
- **User**: 사용자 정보 (이메일, 닉네임, 프로필)
- **UserCigarette**: 사용자 흡연 설정 (일일 흡연량, 담배 가격)
- **UserCigaretteHistory**: 흡연 기록 이력
- **Challenge**: 금연 챌린지 정보
- **Mission**: 미션 정보
- **Friend**: 친구 관계
- **AlertSetting**: 사용자별 알림 설정
- **Survey**: 니코틴 의존도 설문조사

### 관계 설계
- User ↔ UserCigarette: OneToOne
- User ↔ Challenge: ManyToMany (참여 관계)
- User ↔ Friend: ManyToMany (친구 관계)
- Challenge ↔ Mission: OneToMany

## 🔄 CI/CD

### 배포 파이프라인
```
GitHub Push
    ↓
Build (Gradle)
    ↓
Test (JUnit)
    ↓
Package (JAR)
    ↓
Deploy
    ↓
Health Check
    ↓
Service Switch
```

### 무중단 배포
- Blue-Green Deployment 전략
- Health Check 기반 자동 전환

## 🧪 테스트

### 테스트 전략
- **단위 테스트**: 핵심 비즈니스 로직 테스트
- **통합 테스트**: API 엔드포인트 테스트
- **REST Docs 테스트**: API 문서 자동 생성을 위한 테스트

### 테스트 커버리지
- Service Layer: 핵심 비즈니스 로직 테스트
- Controller Layer: MockMvc 기반 API 테스트
- Repository Layer: QueryDSL 쿼리 테스트