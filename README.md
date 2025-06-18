# SpringBoard 📝(진행중)

**SpringBoard**는 Java와 Spring Boot 기반으로 개발된 커뮤니티 게시판 백엔드 프로젝트입니다.  
사용자는 회원가입 및 로그인 후 게시글을 작성하거나 댓글을 달 수 있으며, 댓글은 트리 구조(대댓글 포함)를 지원합니다.  
또한 JWT 기반 인증과 소프트 삭제(Soft Delete) 처리, 게시글 좋아요 기능 등 실전 개발에서 자주 사용되는 기능들을 포함하고 있습니다.

---

## ✅ 주요 기능

- **회원가입 / 로그인 (JWT 인증 방식)**
- **게시글 CRUD**  
  - 삭제 시 소프트 삭제 방식 적용 (isDeleted 필드)
- **댓글 CRUD (트리 구조 지원)**  
  - 부모 댓글 ID 기반 대댓글 구현
- **게시글 좋아요 기능**  
  - 로그인 유저 기준으로 '좋아요 여부' 응답 포함
- **중복 닉네임 검사**  
  - isDeleted = false 상태인 유저만 중복 검사 대상으로 포함
- **단위 테스트 작성**  
  - JUnit + Mockito 기반 서비스 단 테스트 진행 중

---

## 🛠 기술 스택

| 항목 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.4.4 |
| 빌드 도구 | Gradle |
| 보안/인증 | Spring Security, JWT |
| ORM | Spring Data JPA (Hibernate) |
| 데이터베이스 | MySQL |
| 테스트 | JUnit5, Mockito |
| 기타 | Lombok, H2 (개발 환경) |

## 🧪 API 테스트 방법

> 개발 환경에서는 H2 DB와 Postman 등을 활용해 테스트할 수 있습니다.

## 📚 API 문서 (Swagger)

해당 프로젝트는 **Swagger UI**를 사용해 API 명세서를 제공합니다.  
개발 환경에서 아래 주소로 접속하면 Swagger 기반의 자동 문서를 확인할 수 있습니다.

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- Swagger 설정은 SpringDoc(OpenAPI 3) 기반으로 작성
- API 요청/응답 구조, 필드 설명, 예시 값 등 포함

