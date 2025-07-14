# Bong-Jangboo 백엔드 서버  
![license](https://img.shields.io/github/license/bong-jangboo/backend?style=flat-square&color=blue)
![contributors](https://img.shields.io/github/contributors/bong-jangboo/backend?style=flat-square&color=blue)
![last-commit](https://img.shields.io/github/last-commit/bong-jangboo/backend?style=flat-square&color=blue)

**Bong-Jangboo**는 학생 단체의 회계 관리를 자동화하고 투명하게 운영할 수 있도록 지원하는 백엔드 시스템입니다.  
오픈뱅킹 API, 영수증 OCR, 결재 워크플로우 등을 통합하여 회계·총무 업무의 디지털화를 실현합니다.

---

## 🙏 Credits & 원작자

이 프로젝트는 2024년 캡스톤디자인 프로젝트인 [`bong-jangboo`](https://github.com/2024-PKNU-capstone)를 기반으로 리팩토링 및 고도화한 버전입니다.  
초기 설계 및 개발에 기여해주신 전 팀원 분들께 감사드립니다.

➡️ [원작 contributors 보기](https://github.com/2024-PKNU-capstone/backend/graphs/contributors)

---

## 🚀 주요 기능

- OAuth 기반 오픈뱅킹 연동 (계좌 조회, 거래내역 확인)
- S3 기반 영수증 이미지 업로드
- OCR을 활용한 자동 영수증 분석
- 장부 등록 및 결재 승인 워크플로우
- 회장 → 부회장/총무/학생으로 이어지는 역할 체계 및 위임 기능

---

## 🛠 기술 스택

![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=spring-boot)
![Gradle](https://img.shields.io/badge/Gradle-7+-02303A?style=flat-square&logo=gradle)
![Clova OCR](https://img.shields.io/badge/Naver_Clova-OCR_API-03C75A?style=flat-square&logo=naver)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=flat-square&logo=docker)
![Jenkins](https://img.shields.io/badge/Jenkins-CD-D24939?style=flat-square&logo=jenkins)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI-2088FF?style=flat-square&logo=github-actions)

| 영역 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.x |
| 빌드 도구 | Gradle |
| 인증 | JWT + Spring Security |
| 파일 저장소 | AWS S3 |
| OCR | Naver Clova OCR API |
| 데이터베이스 | MySQL |
| 배포 | Docker, GitHub Actions (CI), Jenkins (CD) |

---

## 🗂 디렉토리 구조

```
com.bongjangboo
├── account/          # 계좌 등록 및 조회
├── auth/             # 로그인, 인증 처리
├── appoint/          # 역할 임명
├── delegate/         # 회장 권한 위임
├── receipt/          # 영수증 업로드 및 OCR
├── transaction/      # 거래내역 관리
├── accountbook/      # 장부 및 결재 처리
├── users/            # 사용자 정보 및 납부 처리
├── role/             # 권한 조회 및 홈 라우팅
├── oauth/            # 오픈뱅킹 인증 처리
├── infra/            # 외부 API, S3 등 인프라 연동
└── global/           # 공통 예외, 응답, 보안 설정 등
```

---

## 🔑 인증 및 권한 체계

- JWT 기반 사용자 인증 및 권한 부여
- 회장 / 부회장 / 총무 / 학생 등 역할 기반 인가
- Swagger 테스트 시 `Authorization: Bearer <토큰>` 헤더 필요

---

## 📚 API 명세

- [API 명세 요약 보기](https://chatgpt.com/c/api_spec_summary.md)  
- Swagger: `http://localhost:8080/swagger-ui/index.html`

---

## 📄 라이선스

이 프로젝트는 [MIT 라이선스](https://github.com/bong-jangboo/backend/blob/main/LICENSE)에 따라 배포됩니다.
