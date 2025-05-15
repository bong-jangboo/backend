# Bong-Jangboo 백엔드 서버

Bong-Jangboo는 학생 단체 회계 관리를 자동화하기 위한 백엔드 시스템입니다.

은행 API, 영수증 OCR, 장부 결재 프로세스 등을 통합하여, 회계/총무 업무를 간소화하고 투명하게 관리할 수 있도록 지원합니다.

---

## 🚀 프로젝트 특징

- OAuth 기반 오픈뱅킹 연동 (계좌 조회, 거래내역 확인)
- S3 기반 영수증 이미지 업로드
- OCR을 활용한 자동 영수증 처리
- 장부 등록 및 결재 승인 워크플로우
- 회장 → 부회장/총무/학생 권한 체계 및 위임 기능

---

## 🛠 기술 스택

| 영역 | 기술 |
| --- | --- |
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.x |
| 빌드 도구 | Gradle |
| 인증 | JWT + Spring Security |
| 파일 저장소 | AWS S3 |
| OCR | 외부 OCR API (Naver Clova) |
| DB | MySQL |
| 배포 | Docker, GitHub Actions (CI), Jenkins (CD) |

---

## 🗂 디렉토리 구조

```
com.example.jangboo
├── account/          # 계좌 등록 및 조회
├── auth/             # 로그인, 인증 처리
├── appoint/          # 역할 임명
├── delegate/         # 회장 권한 위임
├── receipt/          # 영수증 업로드, OCR
├── transaction/      # 거래내역
├── accountbook/      # 장부 및 결재
├── users/            # 사용자 정보 및 납부 처리
├── role/             # 권한 조회, 홈 화면 라우팅
├── oauth/            # 오픈뱅킹 인증 처리
├── infra/            # S3, 외부 API 클라이언트
└── global/           # 공통 예외, 응답, 보안 등

```

---

## 🔑 인증 및 권한

- JWT 기반 로그인 및 사용자 인증
- 회장 / 부회장 / 총무 / 학생 역할 기반 인가 처리
- Swagger 테스트 시 `Authorization: Bearer <토큰>` 헤더 필요

---

## 📚 API 명세

- [API 명세 보기](https://chatgpt.com/c/api_spec_summary.md) ← 별도 정리된 요약 문서 참고
- Swagger: `http://localhost:8080/swagger-ui/index.html`

---

## 📄 라이선스

MIT License
