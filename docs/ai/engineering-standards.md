# 구현 규칙

루트의 `AGENTS.md`가 핵심 규칙의 기준이다. 이 문서는 구현 전에 확인할 이유와 적용 순서를 보완한다.

## API와 계층

```text
Controller Request DTO
  -> toServiceRequest()
  -> Service interface / implementation
  -> Repository interface / implementation / JpaRepository
  -> Service Response DTO
  -> ApiResponse
```

- Controller request는 `controller/request`에 두고 Bean Validation을 선언한다.
- Controller DTO를 Service에 직접 전달하지 않는다. `toServiceRequest()`로 변환한다.
- 엔티티와 JPA repository를 Controller에 노출하지 않는다.
- Service는 repository 추상화에 의존한다. QueryDSL 또는 조합 조회가 필요하면 `repository/impl`에 둔다.
- 읽기는 `XxxReadService`와 `@Transactional(readOnly = true)`, 쓰기는 `XxxService`와 트랜잭션을 사용한다.
- Controller는 `ApiResponse.ok`, `created`, `of`, `error`를 사용한다. 직접 `ResponseEntity`를 조립하지 않는다.

## 인증·예외·트랜잭션

- 현재 로그인 사용자는 `SecurityService.getCurrentLoginUserInfo()`로 가져온다. request path나 body로 userId를
  받지 않는다.
- 예상 가능한 실패는 `AddictionException`, `NotFoundException`, `UnauthenticatedException` 중 의미에 맞는
  예외로 표현한다. Controller에서 예외를 삼키지 않는다.
- 전역 `Exception` 처리기는 Slack과 LogAgent 알림을 발생시킨다. 일반 검증 실패나 조회 실패가 여기에 닿지
  않도록 한다.
- 보상, 미션 완료, 기록 집계처럼 재시도될 수 있는 쓰기는 중복 요청과 실패 후 재시도를 먼저 설계한다.
- 보안 규칙·CORS·JWT filter 변경은 별도 보안 변경으로 취급한다. 기능 구현 편의를 위해 공개 matcher를 넓히지 않는다.

## 변경 시 확인할 질문

1. 이 기능이 어느 도메인의 상태를 읽고 쓰는가?
2. 정상·없는 데이터·권한 없음·중복 요청에서 각각 어떤 응답 또는 예외가 나오는가?
3. 외부 호출 또는 스케줄러가 포함되면 DB 커밋과 외부 부수 효과의 순서는 안전한가?
4. 프런트가 엔티티가 아닌 안정적인 응답 DTO만 받는가?
5. 동작과 응답 필드가 달라졌다면 테스트와 REST Docs를 함께 바꿨는가?

## 허용하지 않는 지름길

- 생성자 주입 대신 필드 주입을 추가하지 않는다.
- 기존 구조를 우회해 `XxxJpaRepository`를 Service에 직접 주입하지 않는다.
- `src/main/generated`의 QueryDSL 생성물을 수정하지 않는다.
- 연관 없는 포맷 변경, 패키지명 일괄 수정, 의존성 업그레이드를 기능 PR에 섞지 않는다.
