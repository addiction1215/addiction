# AI 작업 안내서

이 디렉터리는 Codex와 Claude가 같은 기준으로 저장소를 읽고, 작은 범위의 검증 가능한 변경을
만들기 위한 근거 문서다. 코드보다 이 문서를 우선하는 것이 아니라, 코드에서 드러나지 않는
규칙·경계·확인 절차를 보완한다.

## 읽는 순서

1. 항상 루트의 [`AGENTS.md`](../../AGENTS.md)를 읽는다.
2. 요청 범위에 따라 아래 문서를 추가로 읽는다.
3. 실제 변경 전에 관련 Controller, Service, Repository, 테스트, API 문서를 추적해 현재 동작을
   확인한다. 문서와 코드가 다르면 코드를 사실로 보고 문서를 함께 고친다.

| 작업 | 필수 문서 |
|---|---|
| 새 기능, API, 도메인 수정 | `architecture.md`, `engineering-standards.md`, `workflow.md` |
| 금연 행동, 미션, 보상, 사용자 흐름 변경 | `product-principles.md` |
| 테스트, DTO, 응답 필드 변경 | `testing-and-api-docs.md` |
| 인증·인가, DB, Mongo, S3, OAuth, 푸시, 메일 | `operations-and-security.md` |
| 배치·스케줄러·알림 Outbox | `architecture.md`, `operations-and-security.md` |
| 문서만 수정 | `workflow.md` |

## 문서의 책임

- `architecture.md`: 도메인 경계, 요청·비동기 데이터 흐름, 외부 시스템의 역할.
- `engineering-standards.md`: 코드 배치, 계층 간 DTO 흐름, 트랜잭션·예외·보안 경계.
- `product-principles.md`: 기능 범위를 판단하는 QuitMate 제품 원칙과 보상·행동 변화 기준.
- `testing-and-api-docs.md`: 기존 테스트 지원 클래스, REST Docs, 검증 범위.
- `workflow.md`: 조사부터 PR 보고까지의 작업 절차와 변경 범위 통제.
- `operations-and-security.md`: 환경별 설정, 시크릿, 배포, 외부 연동의 변경 금지 경계.

## 유지 원칙

- 문서는 실제 코드 경로와 검증 명령을 가리킨다. 파일 목록을 복제하거나 추측으로 작성하지 않는다.
- 새로운 도메인·외부 연동·배치가 생기면 먼저 이 문서의 영향을 검토한다.
- 문서가 너무 길어져서 매 작업마다 모두 읽어야 하면, 공통 규칙은 `AGENTS.md`에 남기고
  세부 내용은 이 디렉터리에서 작업별로 분리한다.
