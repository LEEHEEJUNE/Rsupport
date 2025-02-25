# 공지사항 관리 REST API 구현

이 프로젝트는 JWT 기반 로그인 시스템을 통해 관리자만이 공지사항을 등록, 수정, 삭제하는 등 관리 기능을 수행할 수 있으며, 일반 사용자들은 별도의 인증 없이 공지사항을 조회할 수 있는 애플리케이션이다.
- admin(관리자용): JWT 토큰 인증을 통해 관리자를 검증하고(현재는 유무만 판단), 검증된 관리자에게 공지사항 등록, 수정, 삭제, 상세 조회 및 리스트 조회 등의 기능을 제공한다.
- auth(인증): 관리자의 ID, PW 확인 후(현재는 무조건 발급) JWT 토큰을 발급한다.
- user(사용자용):사용자에게 공지사항 조회 기능을 제공한다.

## 데이터베이스 설계
### announcement Table
```sql
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '공지사항 ID',
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '제목',
  `content` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '내용',
  `view_count` int DEFAULT '0' COMMENT '조회수',
  `writer` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '작성자',
  `start_date` datetime DEFAULT NULL COMMENT '공지 시작일시',
  `end_date` datetime DEFAULT NULL COMMENT '공지 종료일시',
  `files` json DEFAULT NULL COMMENT '첨부 파일',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='공지사항';
```
### file_management Table
```sql
CREATE TABLE `file_management` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `original_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '원본 이름',
  `stored_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '내부 저장 이름',
  `path` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '저장 경로',
  `size` bigint DEFAULT NULL COMMENT '파일 크기',
  `type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '파일 타입',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='파일관리';
```

## 핵심 문제 해결
### admin(관리자용) : 관리자 API를 호출하기 전에 먼저 /auth/login 엔드포인트에서 JWT 토큰을 발급받은 뒤, 해당 토큰을 요청에 포함하여야 한다.
- 공지사항 등록
  1. 제목, 내용, 작성자(jwt토큰 ID 추출), 공지시작일, 공지종료일, 파일리스트를 'multipart/form-data'로 요청 받는다.
  2. 파일은 file_management 테이블에 데이터를 저장하고 파일은 로컬 서버 /uploads/ 에 stored_name으로 저장된다.
  3. announcement 테이블에 files에 요청받고 저장 된 파일들의 id 리스트를 배열로 저장한다.
- 공지사항 삭제
  1. 요청 한 공지사항 ID 값의 파일 리스트를 로컬에서 삭제하고, file_management 테이블에도 삭제한다.
  2. 요청 한 공지사항 데이터도 announcement테이블에서 삭제한다.
- 공지사항 수정
  1. 요청 한 공지사항 ID 값의 파일 리스트를 로컬에서 삭제하고 요청 받은 파일리스트를 저장한다.
  2. 요청 한 공지사항 정보를 모두 적용한다.
- 공지사항 리스트 조회
  1. page, size 값을 요청받아(default page=0, size=20) 페이징 처리하여 대용량 트래픽에 대처한다.
  2. 요청받은 page, size에 맞춰 announcement 테이블에 조회한다.
  3. 응답 데이터는 id,제목,작성자,공지시작일시,공지종료일시 이다.
- 공지사항 상세 조회
  1. 요청 한 공지사항 ID의 데이터를 조회한다.
  2. 응답 데이터는 id, 제목, 내용, 작성자, 공지시작일시, 공지종료일시, 파일리스트(원본파일명,저장된 파일명, 로컬서버정장경로, 파일사이즈, 파일타입) 이다.
     
### user(유저용) : 모두가 접근 가능한 API 이다.
- 공지사항 조회
  1. 조회 하면 조회수가 증가되며 증가 된 조회수는 저장된다.
  2. 응답 데이터는 제목, 내용, 등록일시, 증가 된 조회수, 작성자 이다.
     
### auth(인증) : 인증을 담당한다.
- 로그인
  1. id, pw 를 입력받지만 현재는 검증은 하지않고 토큰을 발급한다.
  
## 실행방법
1. 클린 빌드
```bash
./gradlew clean
```
2.bootJar 생성
```bash
./gradlew bootJar
```
3.JAR 파일 실행
```bash
java -jar build/libs/announcement-0.0.1-SNAPSHOT.jar
```

## 기술 스택
- 언어: Kotlin 1.9.22
- 웹 프레임워크: Spring Boot 3.4.3
- Persistence 프레임 워크: Hibernate 
- 보안: Spring Security, JWT (JJWT 라이브러리)
- 파일 관리: MultipartFile을 통한 파일 업로드 및 로컬 파일 시스템 저장
- 빌드 도구: Gradle (Kotlin DSL)
- 데이터베이스: MySQL

