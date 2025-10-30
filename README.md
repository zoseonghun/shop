# 목차

- 프로젝트 개요
- 프로젝트 구성
- 프로젝트 수행과정 및 결과
- 프로젝트 후기

## 1. 프로젝트 개요

### 프로젝트 필요성  

쇼핑몰과 같은 전자상거래 웹 애플리케이션에서 회원 관리, 상품 관리, 장바구니 기능, 주문 처리 등은 필수적입니다.
본 프로젝트는 Spring Framework 기반으로 쇼핑몰 전반의 주요 기능을 구현하여 실무 경험을 쌓기 위한 학습용 및 포트폴리오용 프로젝트로 시작되었습니다.


### 서비스 대상  

- 소규모 웹 애플리케이션 운영자 및 개발자
- Spring MVC 및 JPA 기반 백엔드 기술 학습자
- Java 웹 프로젝트에서 프론트엔드와 백엔드 통신 구현 경험을 원하는 개발자


### 담당 업무  

- Spring MVC 컨트롤러 및 서비스 개발
- JPA 리포지토리 및 쿼리 작성
- Thymeleaf 템플릿과 JavaScript(Fetch API)를 활용한 동적 UI 구현
- Spring Security 적용 및 CSRF 보안 설정
- 프로젝트 빌드, 배포, 디버깅 작업

---

## 2. 프로젝트 구성

### 기술 스택  

- Java 21
- Spring Boot 3.5.3 (Spring MVC, Spring Security)
- Spring Data JPA (Hibernate)
- MariaDB
- Thymeleaf, HTML, CSS, JavaScript (Fetch API)
- Apache Tomcat 9
- IntelliJ IDEA Ultimate
  

### 시스템 아키텍처

#### 시퀀스 다이어그램  
- 클라이언트 → 서버: 상품 조회, 장바구니 담기, 주문 요청 (REST API)
- 서버 → DB: JPA를 통한 CRUD 실행
- 서버 → 클라이언트: JSON 응답 및 Thymeleaf 렌더링 결과 전송


#### 데이터베이스 구조  
- member, item, cart, order 등 주요 테이블로 구성
- 각 테이블은 PK 및 필요한 컬럼들로 구성되어 있음


#### member 테이블
| 컬럼명    | 데이터 타입    | 제약 조건                  | 설명                 |
|-----------|---------------|--------------------------|----------------------|
| member_id | BIGINT        | PK, AUTO_INCREMENT       | 회원 고유 ID         |
| email     | VARCHAR(100)  | UNIQUE, NOT NULL         | 회원 이메일          |
| password  | VARCHAR(255)  | NOT NULL                 | 비밀번호             |
| name      | VARCHAR(100)  | NOT NULL                 | 회원 이름            |
| role      | VARCHAR(50)   | NOT NULL                 | 권한 (ROLE_USER 등)  |
| reg_time  | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP| 가입일시             |


#### item 테이블
| 컬럼명      | 데이터 타입    | 제약 조건                  | 설명                 |
|-------------|---------------|--------------------------|----------------------|
| item_id     | BIGINT        | PK, AUTO_INCREMENT       | 상품 고유 ID         |
| item_name   | VARCHAR(200)  | NOT NULL                 | 상품명               |
| item_detail | TEXT          |                          | 상품 상세 설명       |
| price       | INT           | NOT NULL                 | 가격                 |
| stock       | INT           | NOT NULL                 | 재고 수량            |


#### cart 테이블
| 컬럼명       | 데이터 타입    | 제약 조건              | 설명                   |
|--------------|---------------|----------------------|------------------------|
| cart_id      | BIGINT        | PK, AUTO_INCREMENT   | 장바구니 고유 ID       |
| member_id    | BIGINT        | FK(member.member_id) | 소유 회원 ID           |
| created_date | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP | 생성일시           |


#### cart_item 테이블
| 컬럼명       | 데이터 타입    | 제약 조건            | 설명                  |
|--------------|---------------|--------------------|-----------------------|
| cart_item_id | BIGINT        | PK, AUTO_INCREMENT | 장바구니 항목 고유 ID |
| cart_id      | BIGINT        | FK(cart.cart_id)   | 장바구니 ID           |
| item_id      | BIGINT        | FK(item.item_id)   | 상품 ID               |
| count        | INT           | NOT NULL           | 수량                  |


#### orders 테이블
| 컬럼명     | 데이터 타입    | 제약 조건              | 설명               |
|------------|---------------|----------------------|--------------------|
| order_id   | BIGINT        | PK, AUTO_INCREMENT   | 주문 고유 ID       |
| member_id  | BIGINT        | FK(member.member_id) | 주문 회원 ID       |
| order_date | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP | 주문일시       |
| status     | VARCHAR(50)   | NOT NULL             | 주문 상태          |


#### order_item 테이블
| 컬럼명      | 데이터 타입    | 제약 조건            | 설명                  |
|-------------|---------------|--------------------|-----------------------|
| order_item_id | BIGINT        | PK, AUTO_INCREMENT | 주문 상세 항목 고유 ID |
| order_id    | BIGINT        | FK(orders.order_id)| 주문 ID               |
| item_id     | BIGINT        | FK(item.item_id)   | 상품 ID               |
| quantity   | INT           | NOT NULL           | 수량                  |

  

#### API 설계
| HTTP Method | Endpoint               | 예상 역할                   |
|-------------|------------------------|----------------------------|
| POST        | /members/signup        | 회원가입                   |
| POST        | /members/login         | 로그인                     |
| POST        | /members/logout        | 로그아웃                   |
| GET         | /members/me            | 현재 로그인 회원 정보 조회  |
| GET         | /items                 | 전체 상품 목록 조회         |
| GET         | /items/{id}            | 특정 상품 상세 조회        |
| POST        | /items                 | 상품 등록 (관리자용)        |
| PUT         | /items/{id}            | 상품 수정 (관리자용)        |
| DELETE      | /items/{id}            | 상품 삭제 (관리자용)        |
| POST        | /cart/add              | 장바구니에 상품 추가        |
| PUT         | /cart/update/{cartItemId} | 장바구니 수량 변경       |
| DELETE      | /cart/delete/{cartItemId} | 장바구니 상품 삭제       |
| GET         | /cart                  | 장바구니 전체 목록 조회     |
| POST        | /orders/create         | 주문 생성                   |
| GET         | /orders/{orderId}      | 주문 상세 조회              |
| GET         | /orders                | 주문 내역 전체 조회         |


#### API 명세서
| Method | Endpoint                | 설명                       |
|--------|-------------------------|----------------------------|
| POST   | /members/signup         | 신규 회원 가입             |
| POST   | /members/login          | 회원 로그인                |
| POST   | /members/logout         | 회원 로그아웃              |
| GET    | /members/me             | 로그인한 회원 정보 조회     |
| GET    | /items                  | 상품 전체 리스트 조회       |
| GET    | /items/{id}             | 상품 상세 조회             |
| POST   | /items                  | 상품 추가 (관리자용)       |
| PUT    | /items/{id}             | 상품 수정 (관리자용)       |
| DELETE | /items/{id}             | 상품 삭제 (관리자용)       |
| POST   | /cart/add               | 장바구니 상품 추가         |
| PUT    | /cart/update/{cartItemId} | 장바구니 수량 변경       |
| DELETE | /cart/delete/{cartItemId} | 장바구니 상품 삭제       |
| GET    | /cart                   | 장바구니 목록 조회         |
| POST   | /orders/create          | 주문 생성                  |
| GET    | /orders/{orderId}       | 주문 상세 조회             |
| GET    | /orders                 | 주문 내역 조회             |

---

## 3. 프로젝트 수행과정 및 결과

---

## 4. 프로젝트 후기


