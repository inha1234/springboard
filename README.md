# SpringBoard 📝 (진행중)

**SpringBoard**는 Java와 Spring Boot 기반으로 개발된 커뮤니티 게시판 백엔드 프로젝트입니다.  
회원가입, 로그인, 게시글 작성 및 댓글 기능을 포함하며, 댓글은 대댓글을 지원하는 트리 구조로 설계되어 있습니다.  
JWT 기반 인증, 소프트 삭제 처리, 게시글 좋아요 기능 등 실무에서 자주 활용되는 핵심 기능들을 구현했습니다.

---

## ✅ 주요 기능

- **회원가입 / 로그인**  
  - JWT 기반 인증 방식 적용  
  - 중복 닉네임 검사 (삭제되지 않은 사용자만 대상)

- **게시글 기능**
  - 게시글 작성 / 조회 / 수정 / 삭제 (CRUD)
  - 소프트 삭제 방식 적용 (`isDeleted` 필드 활용)

- **댓글 기능**
  - 댓글 작성 / 조회 / 수정 / 삭제 (CRUD)
  - 트리 구조 기반 대댓글 구현 (`parentId` 기반)

- **좋아요 기능**
  - 게시글 좋아요 등록/취소
  - 로그인한 유저 기준 '좋아요 여부' 응답 포함

- **단위 테스트**
  - JUnit + Mockito 기반 서비스 레이어 단위 테스트 작성 중

---

## 🛠 기술 스택

| 항목       | 기술                                       |
|------------|--------------------------------------------|
| 언어       | Java 17                                    |
| 프레임워크 | Spring Boot 3.4.4                          |
| 빌드 도구  | Gradle                                     |
| 보안/인증  | Spring Security, JWT                       |
| ORM        | Spring Data JPA (Hibernate)                |
| 데이터베이스 | MySQL                                   |
| 테스트     | JUnit 5, Mockito                           |
| 기타       | Lombok, H2 Database (개발 환경 전용)        |

---

## 🧪 API 테스트 방법

- 개발 환경에서는 **H2 DB**, **Postman**, 또는 **Swagger UI**를 통해 API를 직접 테스트할 수 있습니다.

---

## 📚 API 문서 (Swagger)

- SpringDoc(OpenAPI 3)을 활용해 Swagger UI 기반의 자동 문서를 제공합니다.  
- 개발 서버 구동 후 다음 주소에서 확인 가능합니다:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

> 문서에는 API 요청/응답 구조, 필드 설명, 예시 값 등을 포함합니다.

---
