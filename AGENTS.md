# AGENTS.md

Quitmate(금연 관리 앱) 백엔드. Spring Boot 3.4.2 / Java 17 / Gradle 8.12.1.

이 문서는 **코드를 읽어서는 알 수 없는 규칙**만 담는다. 기술 스택·기능 소개는 `README.md` 참고.

---

## 검증 명령

```bash
./gradlew test          # 전체 테스트 (CI는 ./gradlew test --parallel)
./gradlew test --tests "*UserServiceTest"   # 단건
./gradlew build         # bootJar → asciidoctor 의존, 문서까지 빌드됨
```

- 코드를 변경했다면 **최소한 관련 테스트를 돌려서 통과를 확인**한 뒤 완료를 보고할 것.
- 테스트는 `test` 프로파일 + H2로 돌아가며 별도 인프라(DB/Mongo)가 필요 없다.

---

## 패키지 구조

도메인별 수직 분할. 예: `com.addiction.user.users`, `com.addiction.challenge.mission`.

```
<domain>/
  controller/            XxxController
  controller/request/    XxxRequest          (외부 노출 DTO)
  service/               XxxService, XxxReadService   (인터페이스)
  service/impl/          XxxServiceImpl, XxxReadServiceImpl
  service/request/       XxxServiceRequest   (내부 DTO)
  service/response/      XxxResponse
  repository/            XxxRepository(인터페이스), XxxJpaRepository(Spring Data)
  repository/impl/       XxxRepositoryImpl   (QueryDSL / 위임)
  entity/                엔티티, entity/enums/
```

공통은 `global/` (ApiResponse, BaseTimeEntity, config, exception, security, page, slack).

---

## 반드시 지킬 레이어 규칙

새 API를 추가할 때 이 규칙을 깨는 코드가 자주 나온다. 기존 도메인(`user/users`가 가장 완성도 높음)을 그대로 따라갈 것.

### 1. DTO는 3단으로 흐른다

```
XxxRequest --.toServiceRequest()--> XxxServiceRequest --> XxxResponse
```

- 컨트롤러 DTO(`controller/request`)를 서비스로 **그대로 넘기지 않는다**. 반드시 `toServiceRequest()`로 변환.
- 서비스는 `service/response`의 응답 객체를 반환한다. 엔티티를 그대로 반환하지 않는다.
- 검증 애노테이션(`@Valid`, `@NotNull` 등)은 `controller/request` 쪽에 둔다.

### 2. 읽기/쓰기 서비스를 분리한다

- 쓰기: `UserService` / `UserServiceImpl` — 클래스 레벨 `@Transactional`
- 읽기: `UserReadService` / `UserReadServiceImpl` — 클래스 레벨 `@Transactional(readOnly = true)`
- 조회 로직을 쓰기 서비스에 넣지 말 것.

### 3. 리포지토리는 3단

- `XxxRepository` (인터페이스, 서비스가 의존하는 대상)
- `XxxRepositoryImpl` (`repository/impl`, `@Repository`, JpaRepository에 위임 + QueryDSL 동적 쿼리)
- `XxxJpaRepository` (Spring Data JPA)

서비스는 `XxxJpaRepository`를 직접 주입받지 않는다. 항상 `XxxRepository` 인터페이스에 의존한다.

### 4. 컨트롤러 응답은 항상 `ApiResponse`

```java
return ApiResponse.ok(userService.update(request.toServiceRequest()));
```

`ApiResponse.ok / created / of / error`만 사용. `ResponseEntity`를 직접 조립하지 않는다.

### 5. 의존성 주입은 생성자 주입

`@RequiredArgsConstructor` + `private final`. `@Autowired` 필드 주입 금지.

### 6. 예외

- 비즈니스 오류: `AddictionException`
- 조회 실패: `NotFoundException`
- 인증 실패: `UnauthenticatedException`

전역 처리는 `global/exception/ApiControllerAdvice`에서 한다. 컨트롤러에서 try-catch로 감싸지 말 것.
`@ExceptionHandler(Exception.class)`는 Slack/LogAgent로 알림을 보내므로, 예상 가능한 오류를 여기까지 흘리지 않는다.

### 7. 로그인 사용자 조회

`SecurityService.getCurrentLoginUserInfo()`로 가져온다. 컨트롤러 파라미터로 userId를 받지 않는다.

---

## 테스트 규칙

테스트는 3종류이며, 각각 정해진 부모 클래스를 상속한다.

| 종류 | 부모 | 위치 |
|---|---|---|
| 통합/서비스 | `IntegrationTestSupport` (`@SpringBootTest`) | `src/test/java/com/addiction/<domain>/service` |
| 컨트롤러 | `ControllerTestSupport` (`@WebMvcTest`) | `src/test/java/com/addiction/<domain>/controller` |
| API 문서 | `RestDocsSupport` | `src/test/java/docs/<domain>` |

### 반드시 주의할 것

- **새 컨트롤러를 만들면 `ControllerTestSupport`의 `@WebMvcTest(controllers = {...})` 목록과 `@MockitoBean` 필드에 등록해야 한다.** 빠뜨리면 해당 컨트롤러 테스트가 빈을 찾지 못하고 실패한다.
- **새 서비스/리포지토리를 통합 테스트에서 쓰려면 `IntegrationTestSupport`에 `@Autowired`(또는 외부 연동이면 `@MockitoBean`)로 추가한다.**
- **픽스처는 `IntegrationTestSupport`의 기존 헬퍼를 재사용한다** (`createUser`, `createChallenge`, `createMission`, `createSurveyQuestion`, `createAlertSetting` 등). 테스트 클래스 안에서 빌더를 새로 짜지 말 것.
- 데이터 정리는 `DatabaseCleaner`가 `@AfterEach`에서 자동 수행한다. 테스트에 직접 delete 로직을 넣지 않는다.
- 외부 연동(S3, Kakao/Google Feign, Expo 푸시, MongoTemplate)은 `IntegrationTestSupport`에서 이미 `@MockitoBean`으로 대체되어 있다. 실제 호출을 시도하지 말 것.
- 테스트 메서드명·`@DisplayName`은 한국어로 작성한다 (기존 관례).

---

## API 문서 (Spring REST Docs)

엔드포인트를 **추가하거나 요청/응답 필드를 바꾸면 문서도 함께 갱신**한다.

1. `src/test/java/docs/<domain>/XxxControllerDocsTest.java` — 스니펫 생성
2. `src/docs/asciidoc/api/<domain>/*.adoc` — 스니펫 include
3. `src/docs/asciidoc/index.adoc` — 새 문서 파일 include 등록

`./gradlew build` 시 `asciidoctor`가 `bootJar`보다 먼저 실행되어 `static/docs`로 들어간다. 문서 테스트가 깨지면 빌드 전체가 깨진다.

---

## 수정 금지 / 주의 대상

| 경로 | 이유 |
|---|---|
| `src/main/generated/**` | QueryDSL Q타입 생성물. `.gitignore` 대상이고 `clean` 시 삭제된다. 직접 수정 금지 |
| `src/main/resources/application-dev.yml`, `application-prd.yml` | `processResources`에서 환경변수를 치환한다(`${DB_HOST}` 등). **실제 시크릿 값을 하드코딩해 커밋하지 말 것.** 새 설정값이 필요하면 `build.gradle`의 `expand(...)` 목록에도 추가해야 한다 |
| `scripts/`, `appspec.yml` | CodeDeploy 무중단 배포(blue/green 스위칭) 스크립트. 명시적 요청 없이 수정 금지 |
| `build/`, `.gradle/` | 빌드 산출물 |

---

## Git / PR

- 대상 브랜치: `main`, `dev`. PR 생성 시 `.github/PULL_REQUEST_TEMPLATE.md` 형식을 따른다.
- 커밋 메시지는 `feat:` / `fix:` / `test:` / `docs:` / `refactor:` 접두사를 사용한다.
- PR을 열면 `.github/workflows/ci-test.yml`이 `./gradlew test --parallel`을 실행한다. 로컬에서 미리 통과시킬 것.
- 사용자가 명시적으로 요청하지 않는 한 커밋·푸시하지 않는다.
# QuitMate 제품 원칙

## 서비스 목적

QuitMate는 사용자가 흡연 욕구를 억지로 참도록 요구하지 않는다. 흡연으로 얻던 즉각적인 만족을 보상, 성취, 걷기 같은 건강한 행동으로 전환해 지속 가능한 금연 습관을 만들도록 돕는다.

사용자는 미션을 수행하며 금연 과정을 눈으로 확인하고, 작은 성취와 실질적인 보상을 경험해야 한다. 이 경험은 걷기와 생활 습관 개선으로 이어지는 긍정적인 습관 루프를 만들어야 한다.

## 기능 및 API 작업 기준

새 도메인, API, 정책을 제안하거나 구현하기 전에 아래 질문에 답한다.

- 사용자가 담배를 피우지 않는 행동을 한 번 더 선택하게 돕는가?
- 금연 미션 수행, 걷기, 생활 습관 개선 중 하나 이상으로 자연스럽게 이어지는가?
- 적은 입력과 요청으로 사용자의 핵심 흐름을 지원하는가?
- 보상이 금연 행동의 결과로 제공되는가?

위 질문에 명확히 답할 수 없다면 해당 기능은 구현하지 않거나 범위를 줄인다.

## 반드시 지킬 원칙

1. **금연을 더 어렵게 만들지 않는다.**
   - 불필요한 데이터 입력, 복잡한 상태 전이, 긴 설정 과정을 요구하지 않는다.
   - 클라이언트가 사용자의 다음 행동을 빠르게 안내할 수 있도록 단순한 데이터와 흐름을 제공한다.

2. **정보보다 행동 변화를 우선한다.**
   - API와 데이터 모델은 금연 행동, 미션 수행, 걷기, 생활 습관 개선을 지원해야 한다.
   - 행동 변화와 무관한 콘텐츠 소비 또는 기능 탐색을 위한 복잡한 도메인을 추가하지 않는다.

3. **보상은 금연 행동을 강화해야 한다.**
   - 보상은 금연 지속, 걷기, 생활 습관 개선 같은 명확한 행동의 결과로만 지급한다.
   - 보상 획득 조건, 중복 지급 방지, 상태 전이는 서버에서 일관되게 검증한다.
   - 보상만을 반복 획득하는 악용 경로가 생기지 않도록 정책과 테스트를 함께 구현한다.

4. **사용자의 시간을 아낀다.**
   - 클라이언트가 핵심 화면을 구성하는 데 필요한 데이터를 과도한 요청 없이 받을 수 있게 설계한다.
   - 중복 조회, 중복 기록, 불필요한 확인 단계를 줄인다.

5. **실제로 반복 사용할 기능만 만든다.**
   - 팀의 보기 좋은 기능이나 기술 시연보다 금연 성공 가능성을 높이는 핵심 경험을 우선한다.
   - 사용 빈도와 행동 변화 효과가 낮은 기능은 추가하지 않는다.

## 백엔드 구현 지침

- 미션, 금연 진행, 걷기, 보상 도메인의 상태와 완료 조건을 명확하게 정의한다.
- 보상 지급과 미션 완료는 멱등성을 고려해 중복 요청에도 일관된 결과를 보장한다.
- 핵심 행동과 직접 관계없는 데이터 수집은 하지 않는다.
- 새로운 정책을 추가할 때는 성공 흐름뿐 아니라 중복 지급, 부정한 상태 전이, 실패·재시도 흐름을 테스트한다.
