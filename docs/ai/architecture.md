# 시스템 구조와 데이터 흐름

## 전체 구조

QuitMate는 Spring Boot 애플리케이션 하나로 동작한다. `AddictionApplication`이 OpenFeign과
Spring Scheduler를 활성화하며, HTTP 요청은 도메인별 Controller → Service → Repository를 거쳐
저장소 또는 외부 시스템으로 흐른다.

```text
Mobile/Web client
  -> Spring Security + JWT filter
  -> Controller
  -> Service (business policy, transaction)
  -> Repository abstraction
  -> MySQL (transactional data) / MongoDB (daily cigarette history)

Service or scheduler
  -> Feign OAuth / OCI Object Storage / SMTP / Expo push / Slack and LogAgent
```

공통 경계는 `global/`에 있다. 응답은 `ApiResponse`, 인증 사용자 조회는 `SecurityService`, 예상 가능한
실패는 `ApiControllerAdvice`가 처리한다. 도메인 코드는 `com.addiction` 하위의 수직 분할 패키지에 둔다.

## 주요 도메인

| 영역 | 책임 | 대표 경로 |
|---|---|---|
| 사용자·인증 | 가입, OAuth, JWT, 프로필, 설문 결과, 금연 혜택 | `user/users`, `jwt`, `user/refreshToken` |
| 흡연 기록·통계 | 당일 흡연 원본, 과거 일별 통계, 그래프·피드백 | `user/userCigarette`, `user/userCigaretteHistory` |
| 챌린지·미션 | 챌린지 참여, 미션 제출, 진행 상태 | `challenge/` |
| 친구·알림 | 친구 관계, 알림 설정·이력, 푸시 Outbox | `friend`, `alertSetting`, `alertHistory`, `pushOutbox` |
| 설문 | 질문·답변·결과·사용자 응답 | `survey/` |
| 보조 기능 | 파일 저장, FAQ, 문의, 일일 흡연 푸시 | `storage`, `faq`, `inquiry`, `dailySmokingPush` |

새 도메인은 위 책임 중 어느 흐름을 확장하는지 먼저 정한다. 둘 이상의 도메인 상태를 바꾸면
트랜잭션 범위와 중복 요청의 결과를 명시한다.

## 대표 요청 흐름

### 인증된 사용자 API

```text
Authorization: Bearer <access token>
  -> JwtAuthenticationFilter
  -> Controller
  -> SecurityService.getCurrentLoginUserInfo()
  -> Service
  -> Repository
  -> ApiResponse<Response DTO>
```

`SecurityConfig`의 matcher가 실제 공개 범위를 결정한다. Controller의 Swagger 애노테이션이나 URL 이름만으로
접근 제어를 판단하지 말고, 인증·인가를 바꾸는 요청은 matcher와 JWT filter를 함께 검토한다.

### 흡연 기록의 일별 이관

```text
MySQL UserCigarette (당일 원본)
  -> 매일 00:00 Asia/Seoul userCigaretteHistoryBatch
  -> 사용자별 일별 집계
  -> MongoDB CigaretteHistoryDocument 저장
  -> 집계가 끝난 원본 삭제
```

이 흐름은 과거 통계 조회의 저장 위치를 바꾼다. 이관·삭제 순서, 실패한 사용자 처리, 재실행 시 중복 여부를
확인하지 않고 배치를 수정하지 않는다.

### 푸시 알림 Outbox

```text
도메인 이벤트/서비스 -> PushOutbox 저장
  -> 5초 간격 PushOutboxDispatcher
  -> 후보 조회·claim -> Expo 전송
  -> 성공 sent / 실패 retryOrFail
```

푸시를 추가할 때 외부 전송을 비즈니스 트랜잭션에 직접 결합하지 말고, 기존 Outbox 상태 전이와 재시도 정책을
따른다.

## 데이터·외부 연동 경계

- MySQL/JPA/QueryDSL: 사용자, 미션, 친구, 알림 등 정합성이 필요한 업무 데이터.
- MongoDB: 과거 흡연 기록의 일별 통계 문서.
- OpenFeign: Kakao, Google, Naver OAuth 사용자 정보 조회.
- OCI Object Storage와 AWS S3 SDK: 업로드·파일 URL 관련 기능. 변경 전 실제 사용하는 서비스를 확인한다.
- Expo 푸시, SMTP 메일, Slack·LogAgent: 실패 가능성이 있는 외부 부수 효과다.

## 레거시 관례

패키지·대소문자에는 역사적 불일치가 있다. 예를 들어 `challange`, `inquryQuestion`, `Impl`과 `impl`이 공존한다.
기존 도메인을 수정할 때 전체 이름을 정리하지 말고 그 도메인의 가까운 관례를 따른다. 새 도메인은
`controller`, `service/impl`, `repository/impl`, `entity` 구조를 사용한다.
