# 테스트와 API 문서화

## 테스트 선택

| 목적 | 부모 클래스 | 위치·주의사항 |
|---|---|---|
| Service·Repository 통합 검증 | `IntegrationTestSupport` | `src/test/java/com/addiction/<domain>/service`; `test` 프로파일과 H2 사용 |
| Controller HTTP 검증 | `ControllerTestSupport` | `src/test/java/com/addiction/<domain>/controller`; controller와 service mock 등록 필요 |
| REST Docs 스니펫 | `RestDocsSupport` | `src/test/java/docs/<domain>`; standalone MockMvc 사용 |

## 새 코드에 따른 등록 규칙

- 새 Controller의 테스트를 추가하면 `ControllerTestSupport`의 `@WebMvcTest(controllers = ...)`와 필요한
  `@MockitoBean`을 함께 추가한다.
- 새 Service나 Repository를 통합 테스트에서 사용하면 `IntegrationTestSupport`에 필요한 repository 또는
  외부 연동 mock을 등록한다.
- 기존 fixture 헬퍼를 우선 사용한다. 공통 fixture가 정말 부족할 때만 support 클래스에 추가한다.
- `DatabaseCleaner`가 각 통합 테스트 뒤 정리한다. 테스트마다 직접 전체 삭제를 넣지 않는다.
- 외부 시스템(S3, OAuth Feign, Expo, MongoTemplate)은 support 클래스의 mock을 사용한다.

## 최소 검증 기준

- public Service 메서드를 새로 만들거나 정책을 바꾸면 정상, 실패, 경계 또는 중복 상태를 검증한다.
- Controller 요청·응답을 바꾸면 status, validation, response body를 검증한다.
- 변경 범위에 맞는 테스트를 먼저 실행하고, 가능하면 `./gradlew test`를 실행한다.
- `./gradlew build`는 테스트와 Asciidoctor를 포함하므로 API 문서 변경의 최종 확인에 적합하다.
- 실행하지 못한 검증은 통과로 표현하지 말고, 이유와 미실행 범위를 결과에 남긴다.

## REST Docs 갱신 조건

엔드포인트 추가, 요청 필드 변경, 응답 필드 변경, 상태 코드 변경 시 아래 세 곳을 함께 확인한다.

1. `src/test/java/docs/<domain>/XxxControllerDocsTest.java`에서 snippet을 생성한다.
2. `src/docs/asciidoc/api/<domain>/*.adoc`에서 snippet을 include한다.
3. `src/docs/asciidoc/index.adoc`에서 문서가 연결됐는지 확인한다.

현재 저장소의 배포 API 문서 기준은 Spring REST Docs다. OpenAPI 또는 Swagger UI 변경은 별도 범위로 다루며,
단순 의존성 추가만으로 기존 REST Docs 갱신 의무가 사라지지 않는다.
