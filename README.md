# Spring Boot 기반 JWT 인증 시스템

## 개요
이 프로젝트는 Spring Boot 기반의 JWT(JSON Web Token)를 활용한 인증 시스템을 구현한 것입니다. OAuth2를 통한 소셜 로그인(Google, Naver, Kakao)과 JWT 기반의 인증 처리를 제공합니다.

## 주요 기능
- OAuth2 소셜 로그인 (Google, Naver, Kakao)
- JWT 기반 인증 시스템
- 리프레시 토큰을 통한 액세스 토큰 갱신
- RTR(Refresh Token Rotation) 기법을 통한 보안 강화
- 중복 로그인 방지
- 역할 기반 접근 제어(RBAC)

## 시스템 구조

### 패키지 구조
```
com.cake7.guestbook
├── common
│   ├── config          # 시스템 설정
│   ├── dto             # 데이터 전송 객체
│   ├── exception       # 예외 처리
│   ├── filter          # 인증 필터
│   ├── handler         # 이벤트 핸들러
│   ├── oauth           # OAuth2 관련 클래스
│   │   ├── jwtAssistant          # JWT 생성 지원
│   │   └── userInfoAssistant     # 사용자 정보 처리
│   ├── scheduler       # 스케줄링 작업
│   ├── service         # 비즈니스 로직
│   └── util            # 유틸리티 클래스
├── controller          # API 컨트롤러
├── domain              # 도메인 모델
└── mapper              # 데이터 접근 계층
```

## 인증 흐름

### 소셜 로그인 인증 흐름
1. 사용자가 소셜 로그인(Google, Naver, Kakao) 요청
2. OAuth2 인증 성공 시 `OAuth2AuthenticationSuccessHandler`에서 처리
3. JWT 액세스 토큰 및 리프레시 토큰 생성
4. 액세스 토큰은 쿠키에 저장되어 클라이언트에 전달
5. 리프레시 토큰은 데이터베이스에 저장

### 인증 검증 흐름
1. 클라이언트의 요청마다 `JwtAuthenticationFilter`에서 토큰 검증
2. 토큰이 유효한 경우 Security Context에 인증 정보 설정
3. 권한에 따른 접근 제어 적용

### 토큰 갱신 흐름
1. 액세스 토큰 만료 시 리프레시 토큰으로 갱신 요청
2. `RefreshTokenServiceImpl`에서 리프레시 토큰 검증
3. 유효한 리프레시 토큰인 경우 새 액세스 토큰과 리프레시 토큰 발급
4. RTR 기법에 따라 기존 리프레시 토큰은 사용 처리

## 보안 특징

### JWT 토큰 보안
- `JwtConfig`에서 JWT 비밀키와 만료 시간 설정
- 토큰에 발급자(issuer)와 대상자(audience) 정보 포함
- 서명 알고리즘으로 HMAC SHA-256 사용

### 쿠키 보안
- `httpOnly` 속성으로 JavaScript에서 쿠키 접근 방지
- `secure` 속성으로 HTTPS 연결에서만 쿠키 전송
- `sameSite=Strict` 설정으로 CSRF 공격 방지

### 토큰 재사용 감지
- RTR 기법으로 리프레시 토큰은 한 번만 사용 가능
- 토큰 재사용 시도 감지 시 해당 사용자의 모든 토큰 무효화

### 중복 로그인 방지
- `PreventDuplicateLoginFilter`를 통해 이미 인증된 사용자의 로그인 방지

## 주요 컴포넌트 설명

### 설정 (Config)
- `SecurityConfig`: Spring Security 설정
- `JwtConfig`: JWT 관련 설정
- `SwaggerConfig`: API 문서화 설정

### 필터 (Filter)
- `JwtAuthenticationFilter`: JWT 토큰 검증 및 인증 처리
- `PreventDuplicateLoginFilter`: 중복 로그인 방지

### 서비스 (Service)
- `JwtServiceImpl`: JWT 토큰 생성 및 검증
- `RefreshTokenServiceImpl`: 리프레시 토큰 관리
- `CustomOAuth2UserService`: OAuth2 사용자 정보 처리

### 핸들러 (Handler)
- `OAuth2AuthenticationSuccessHandler`: 소셜 로그인 성공 처리
- `CustomLogoutSuccessHandler`: 로그아웃 처리

### 유틸리티 (Util)
- `JwtUtils`: JWT 토큰 처리 유틸리티

## 사용 방법

### API 엔드포인트
- 소셜 로그인: `/oauth2/authorization/{provider}` (provider: google, naver, kakao)
- 토큰 갱신: `/v1/auth/refresh`
- 로그아웃: `/logout`

### 권한 설정
- `ROLE_USER`: 일반 사용자 권한
- `ROLE_ADMIN`: 관리자 권한
- `ROLE_DB`: 데이터베이스 접근 권한

### 역할 계층
```
ROLE_ADMIN > ROLE_DB
ROLE_DB > ROLE_USER
ROLE_USER > ROLE_ANONYMOUS
```

## 개발 및 배포 고려사항

### 환경 변수 설정
- `jwt.secret`: JWT 서명에 사용할 비밀키
- `jwt.expiration`: JWT 토큰 만료 시간(밀리초)

### 보안 고려사항
1. 프로덕션 환경에서는 강력한 비밀키 사용
2. HTTPS 적용 필수
3. 민감한 정보는 환경 변수로 관리
4. 토큰 만료 시간을 적절히 설정

### 성능 고려사항
1. 리프레시 토큰 데이터베이스 인덱싱
2. 만료된 토큰 정리를 위한 스케줄러 활용

## 향후 개선 사항
1. 토큰 블랙리스트 기능 추가
2. 디바이스 관리 기능 (로그인 세션 관리)
3. 비정상적인 로그인 시도 시 메일(알림) 기능
4. 로그인 시도 제한 기능
5. 2단계 인증(2FA) 지원