# AGENTS.md

QuitMate 금연 관리 앱 백엔드. Spring Boot 3.4.2 / Java 17 / Gradle 8.12.1.

이 파일은 모든 AI가 자동으로 읽는 **작업 진입점**이다. 상세 규칙은 `docs/ai/` 문서에 두고,
작업 범위에 필요한 문서만 추가로 읽는다.

## 작업 전 읽기

| 작업 | 추가로 읽을 문서 |
|---|---|
| 기능, API, 도메인 구현·리팩터링 | `docs/ai/architecture.md`, `docs/ai/engineering-standards.md` |
| 금연 행동, 미션, 보상, 사용자 흐름 변경 | `docs/ai/product-principles.md` |
| 테스트, DTO, 요청·응답 필드 변경 | `docs/ai/testing-and-api-docs.md` |
| 인증, DB, 외부 연동, 배치, 배포 변경 | `docs/ai/operations-and-security.md` |
| 모든 변경의 검증, Git, PR | `docs/ai/workflow.md` |

문서와 코드가 다르면 코드를 사실로 보고, 해당 문서도 함께 갱신한다.

## 반드시 지킬 규칙

- 변경 전 실제 Controller → Service → Repository → 테스트 흐름을 추적한다.
- Controller는 Request DTO를 ServiceRequest로 변환하고, 응답은 Response DTO와 `ApiResponse`로 반환한다.
- 현재 로그인 사용자는 `SecurityService.getCurrentLoginUserInfo()`로 조회한다.
- 예상 가능한 오류를 전역 `Exception` handler까지 흘리지 않는다.
- 기존 도메인은 해당 도메인의 가까운 관례를 따르고, 무관한 전역 리팩터링을 하지 않는다.
- 코드 변경 후 관련 테스트를 실행하고, 실행하지 못한 검증은 이유를 함께 보고한다.
- API 계약 변경 시 REST Docs도 갱신한다.
- 시크릿을 하드코딩하거나, QueryDSL 생성물·배포 스크립트·환경 파일을 요청 없이 변경하지 않는다.
- 사용자 요청 없이 커밋·푸시·PR을 만들지 않는다.

## 검증 명령

```bash
./gradlew test
./gradlew test --tests "*UserServiceTest"
./gradlew build
```

## 제품 원칙 요약

- 사용자가 금연 행동을 한 번 더 선택하도록 돕는가?
- 불필요한 입력, 복잡한 상태 전이, 중복 요청을 만들지 않는가?
- 보상과 완료 처리를 서버에서 일관되고 중복 없이 검증하는가?
