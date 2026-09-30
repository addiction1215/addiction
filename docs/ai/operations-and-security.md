# 운영·보안 경계

## 환경과 시크릿

- `application-dev.yml`, `application-prd.yml`은 빌드 시 환경 변수로 치환된다. 실제 키·비밀번호·웹훅·DB 주소를
  코드나 문서에 기록하지 않는다.
- 새 환경 변수를 사용하면 해당 프로파일 설정뿐 아니라 `build.gradle`의 `processResources.expand(...)`도 함께
  검토한다.
- 테스트는 `application-test.yml`과 H2를 사용하며 Mongo 자동 설정을 제외한다. 테스트용 값은 운영 시크릿이 아니다.
- 로컬 실행 설정, Docker Compose, Firebase credential 파일은 팀의 명시적 공유 결정 없이는 커밋 대상이 아니다.

## 외부 연동

| 연동 | 확인할 경계 |
|---|---|
| OAuth Feign | provider별 request/response와 오류를 일반 사용자 오류로 변환하는지 |
| MongoDB | 일별 흡연 이력 저장·조회와 자정 이관의 순서 |
| OCI/S3 | bucket 종류, URL 노출 범위, credential 주입 경로 |
| Expo·SMTP | 외부 실패가 핵심 DB 트랜잭션을 깨지 않는지 |
| Slack·LogAgent | 예상 가능한 오류가 전역 `Exception` handler까지 흘러 알림 폭주를 만들지 않는지 |

외부 시스템을 mock으로 대체한 테스트가 실제 연동 성공을 뜻하지는 않는다. 실제 환경 확인을 했는지와
코드 단위 검증을 구분해서 보고한다.

## 스케줄러와 배포

- 애플리케이션은 Scheduler를 활성화한다. cron·fixed delay 변경은 시간대, 중복 실행, 실패 후 재시도, DB 부하를
  함께 검토한다.
- 흡연 기록 이관과 Push Outbox는 운영 데이터 상태를 바꾼다. 삭제·상태 전이·재시도를 기능 변경에 무심코 섞지 않는다.
- GitHub Actions의 CI는 PR에서 `./gradlew test --parallel`을 실행한다.
- 개발·운영 배포 workflow는 수동 실행이며 SSH, 원격 FCM 파일, `appspec.yml`, `scripts/dev|prd`에 의존한다.
  이 경로들은 배포 변경 요청이 아닌 한 수정하지 않는다.

## 인증·관측성

- `SecurityConfig`의 URL matcher, JWT filter, `SecurityService`가 함께 인증 경계를 이룬다. 한 곳만 바꿔서
  보안을 판단하지 않는다.
- Actuator·Prometheus, Swagger, REST Docs처럼 외부에 노출될 수 있는 경로는 환경별 공개 범위와 민감 정보 노출을
  검토한 뒤 변경한다.
- 로그에는 access token, refresh token, 비밀번호, 인증 코드, 원본 개인정보를 남기지 않는다.
