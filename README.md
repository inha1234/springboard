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
## ⚠️ Trouble Shooting 경험 정리

### 1️⃣ Hibernate 클래스 중복 인식 문제

- 프로젝트 리팩토링 후 `com.springboard.entity.Comment`와 `com.springboard.springboard.entity.Comment` 클래스가 공존하게 되어 Hibernate가 충돌 오류를 발생시킴
- 원인은 과거 구조에서 컴파일된 `.class` 파일이 `build/` 또는 `out/` 폴더에 남아 있었기 때문이며, Hibernate가 이를 스캔 대상으로 인식하고 있었음
- **Ctrl + Shift + F9** 단축키로 전체 프로젝트 Rebuild를 수행하여 컴파일 캐시를 정리하고 문제 해결


---

### 2️⃣ Swagger + Spring Security 환경에서 Authorization 헤더 누락 문제

- 게시글 작성 기능에서 요청 헤더에 JWT 토큰을 포함했지만 서버에서 인식하지 못하는 문제가 발생
- Spring Security의 CORS 설정만으로는 해결되지 않았고, 이전 프로젝트에서 작성했던 Swagger 설정을 참고하여 `SwaggerConfig` 클래스에 전역 Authorization 헤더 설정을 추가함
- 이를 통해 Swagger를 통한 요청에서도 JWT 토큰이 정상적으로 전달되어 인증 문제가 해결됨



---

### 3️⃣ JPA 테스트에서 ID 수동 설정 문제

- 대댓글 기능 테스트 작성 중, 부모 댓글 ID가 필요한 상황에서 `@GeneratedValue`로 자동 생성되는 ID 필드에 값을 직접 넣을 수 없었음
- JPA 엔티티의 무결성을 해치지 않기 위해 **setter를 의도적으로 제거한 구조**로 설계했으며, 외부에서 ID를 임의로 조작하는 것을 막고 싶었음
- 하지만 테스트에서는 DB에 저장되지 않은 객체에도 ID가 필요했기 때문에, **Spring의 `ReflectionTestUtils.setField(...)`를 활용해 private 필드에 값을 강제로 주입**하여 해결

