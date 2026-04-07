# 🛡️ SchemaGuard

> Static Analysis Tool for Detecting Potential Error Points Caused by Database Schema Changes in Spring Boot-Based Backends

---

## 📌 프로젝트 소개

SchemaGuard는 Spring Boot 기반 백엔드 시스템에서 DB 스키마 변경으로 인해 발생할 수 있는 런타임 오류 지점을 사전에 탐지하기 위한 정적 분석 도구입니다.

SchemaGuard는 코드에서 사용되는 엔터티, JPQL, Native Query를 분석하여
스키마 변경 시 영향을 받는 지점을 식별하고 경고를 생성합니다.

실제 서비스에서는 DB 스키마 변경이 발생하더라도 컴파일 단계에서는 문제가 발생하지 않고, 실행 시점에서 API가 깨지는 문제가 자주 발생합니다.

이 프로젝트는 이러한 문제를 해결하기 위해 코드와 DB 간의 의존 관계를 분석하고, 스키마 변경에 따른 영향 범위를 사전에 탐지하는 것을 목표로 합니다.

---

## 🎯 프로젝트 목표

* DB 스키마 변경으로 인한 런타임 오류 사전 탐지
* Entity, Repository, JPQL, Native Query 간 의존성 분석
* 변경된 스키마가 미치는 영향 범위 추적
* 위험도 기반 경고 제공

---

## 🏗️ 실험용 서비스 구조

본 프로젝트는 게시판 기반 서비스로 구성됩니다.

### 📦 도메인 구성

* User
* Post
* Comment
* PostLike
* Category

---

## 🧾 엔터티 정의

### 1. User

| 속성명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 사용자 식별자 |
| email | VARCHAR | UNIQUE, NOT NULL | 사용자 이메일 |
| nickname | VARCHAR | NOT NULL | 사용자 닉네임 |
| status | VARCHAR | NOT NULL | 사용자 상태 |
| created_at | DATETIME | NOT NULL | 생성 시각 |

---

### 2. Category

| 속성명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 카테고리 식별자 |
| name | VARCHAR | UNIQUE, NOT NULL | 카테고리명 |
| description | VARCHAR | NULL | 카테고리 설명 |
| created_at | DATETIME | NOT NULL | 생성 시각 |

---

### 3. Post

| 속성명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 게시글 식별자 |
| title | VARCHAR | NOT NULL | 게시글 제목 |
| content | TEXT | NOT NULL | 게시글 내용 |
| author_id | BIGINT | FK, NOT NULL | 작성자 ID |
| category_id | BIGINT | FK, NOT NULL | 카테고리 ID |
| created_at | DATETIME | NOT NULL | 생성 시각 |
| updated_at | DATETIME | NULL | 수정 시각 |

---

### 4. Comment

| 속성명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 댓글 식별자 |
| content | VARCHAR | NOT NULL | 댓글 내용 |
| post_id | BIGINT | FK, NOT NULL | 게시글 ID |
| author_id | BIGINT | FK, NOT NULL | 작성자 ID |
| created_at | DATETIME | NOT NULL | 생성 시각 |

---

### 5. PostLike

| 속성명 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| id | BIGINT | PK | 좋아요 식별자 |
| post_id | BIGINT | FK, NOT NULL | 게시글 ID |
| user_id | BIGINT | FK, NOT NULL | 사용자 ID |
| created_at | DATETIME | NOT NULL | 생성 시각 |

---

## 프로젝트 구조
```
schemaguard-community/
├── README.md
├── build.gradle
├── settings.gradle
└── src/
    └── main/
        ├── java/com/schemaguard/community/
        │   ├── SchemaGuardApplication.java
        │   │
        │   ├── user/
        │   │   ├── entity/
        │   │   │   └── User.java
        │   │   ├── repository/
        │   │   │   └── UserRepository.java
        │   │   ├── service/
        │   │   │   └── UserService.java
        │   │   ├── controller/
        │   │   │   └── UserController.java
        │   │   └── dto/
        │   │       ├── UserRequest.java
        │   │       └── UserResponse.java
        │   │
        │   ├── post/
        │   │   ├── entity/
        │   │   │   └── Post.java
        │   │   ├── repository/
        │   │   │   └── PostRepository.java
        │   │   ├── service/
        │   │   │   └── PostService.java
        │   │   ├── controller/
        │   │   │   └── PostController.java
        │   │   └── dto/
        │   │       ├── PostRequest.java
        │   │       ├── PostResponse.java
        │   │       └── PostSummaryResponse.java
        │   │
        │   ├── comment/
        │   │   ├── entity/
        │   │   │   └── Comment.java
        │   │   ├── repository/
        │   │   │   └── CommentRepository.java
        │   │   ├── service/
        │   │   │   └── CommentService.java
        │   │   ├── controller/
        │   │   │   └── CommentController.java
        │   │   └── dto/
        │   │       ├── CommentRequest.java
        │   │       └── CommentResponse.java
        │   │
        │   ├── postlike/
        │   │   ├── entity/
        │   │   │   └── PostLike.java
        │   │   ├── repository/
        │   │   │   └── PostLikeRepository.java
        │   │   ├── service/
        │   │   │   └── PostLikeService.java
        │   │   ├── controller/
        │   │   │   └── PostLikeController.java
        │   │   └── dto/
        │   │       └── PostLikeResponse.java
        │   │
        │   └── category/
        │       ├── entity/
        │       │   └── Category.java
        │       ├── repository/
        │       │   └── CategoryRepository.java
        │       ├── service/
        │       │   └── CategoryService.java
        │       ├── controller/
        │       │   └── CategoryController.java
        │       └── dto/
        │           ├── CategoryRequest.java
        │           └── CategoryResponse.java
        │
        └── resources/
            ├── application.yml
            └── static/
                ├── index.html
                ├── detail.html
                ├── write.html
                ├── edit.html
                ├── css/
                │   └── style.css
                └── js/
                    └── main.js
```

---

## ⚙️ 기술 스택

* Backend: Spring Boot
* Language: Java
* ORM: Spring Data JPA
* Database: MySQL / H2
* Build Tool: Gradle

---

## 🔍 분석 대상

* Entity 클래스 (@Entity, @Column, @Table)
* Repository 계층 (JpaRepository)
* JPQL (@Query)
* Native SQL Query
* DTO Projection
* Controller → Service → Repository 호출 구조

---

## 🚨 DB 스키마 변경 유형 및 위험도

| 변경 유형               | 위험도    | 설명                             |
| ------------------- | ------ | ------------------------------ |
| 컬럼 삭제               | HIGH   | JPQL, Native Query, DTO 매핑 불일치 |
| 컬럼명 변경              | HIGH   | Entity, Query, Projection 불일치  |
| 타입 변경               | HIGH   | Java ↔ DB 타입 불일치               |
| 테이블 삭제              | HIGH   | Repository 및 Query 실행 실패       |
| 테이블명 변경             | HIGH   | @Table 불일치 및 Query 오류          |
| FK 컬럼 변경            | HIGH   | 연관관계 매핑 깨짐                     |
| nullable → not null | MEDIUM | INSERT/UPDATE 시 오류             |
| unique 제약 추가        | MEDIUM | 중복 데이터 제약 위반                   |

---

## 🧪 실험 시나리오

### 1. 컬럼 삭제

* Post.content 삭제

### 2. 컬럼명 변경

* User.email → login_email

### 3. 타입 변경

* User.status (VARCHAR → INT)

### 4. 테이블 삭제

* Category 테이블 삭제

### 5. FK 변경

* Post.author_id 제거

### 6. 제약 조건 변경

* Comment.content → NOT NULL
* User.email → UNIQUE

---

## 📊 기대 효과

* 런타임 오류 사전 탐지
* 유지보수 비용 절감
* 개발 생산성 향상
* DB 변경 안정성 확보

---

## 🚀 향후 확장

* CI/CD 연동
* IDE 플러그인
* 다양한 프레임워크 지원
* 시각화 기반 영향 분석

