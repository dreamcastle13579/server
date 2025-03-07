# 몽글몽글 - 꿈 해몽 서비스
![image](https://github.com/user-attachments/assets/e0e9d176-d71d-486b-814b-c5a0b28e04cc)

## 🚀 서비스 

### 🔗 링크
- [서비스 직접 사용 해보기](https://dream-castle.shop/)  
- [서비스 소개 보러가기](https://ablaze-stargazer-5cd.notion.site/1a8bc20b0a408050ae21dc38027546b4)
- [비사이드 x 네이버클라우드](https://bside.best/projects/detail/P250219095755)


### 프로젝트 소개


### 🧑🏻‍💻 사용 기술

![Java](https://img.shields.io/badge/-Java%2017-007396?style=plastic&logo=Java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-Spring%20Boot%203.4.3-6DB33F?style=plastic&logo=Spring%20Boot&logoColor=white)
![JUnit5](https://img.shields.io/badge/-JUnit5-%2325A162?style=plastic&logo=JUnit5&logoColor=white)

### 🧱 인프라
![GitHub Actions](https://img.shields.io/badge/-Github%20Actions-%232088FF?style=plastic&logo=GithubActions&logoColor=white)
![Docker](https://img.shields.io/badge/-Docker-%232496ED?style=plastic&logo=Docker&logoColor=white)

### 📨 협업
![Notion](https://img.shields.io/badge/Notion-000000?style=plastic&logo=Notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=plastic&logo=Slack&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=plastic&logo=Discord&logoColor=white)

### ☁️ Naver Cloud Platform
- [NCP SERVER (VPC)](https://beomsic.tistory.com/entry/NCP%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%B0%ED%8F%AC-1-%EC%84%9C%EB%B2%84)
- [NCP LoadBalancer](https://beomsic.tistory.com/entry/NCP%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%B0%ED%8F%AC-3-%EB%8F%84%EB%A9%94%EC%9D%B8-%EB%B0%8F-SSL-%EC%A0%81%EC%9A%A9)
- NCP Global DNS
- NCP Object Storage
- [CLOVA STUDIO](https://beomsic.tistory.com/entry/CLOVA-Studio-API-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EB%8B%B5%EB%B3%80-%EB%B0%9B%EA%B8%B0)


## 🏃🏻 프로젝트 진행

### 📌 구현 필수 기능

- [x] 꿈 해몽 API

### 📌 사용 기술

### 1️⃣ CLOVA STUDIO (플레이그라운드, 튜닝)

사용자가 꿈을 해몽 요청시, 해당 사용자의 꿈 내용과 컨셉에 맞는 해몽된 결과를 다시 사용자에게 전달해주어야 했다.  
[CLOVA STUDIO](https://www.ncloud.com/product/aiService/clovaStudio)를 이용해 꿈 해몽을 처리하도록 했다.  
컨셉에 맞는 system prompt를 작성해 사용자의 꿈 내용을 해몽하고 이를 사용자에게 다시 반환해주었다.


### 2️⃣ WebClient - nGrinder를 이용한 테스트

CLOVA STUDIO API 요청을 위해서 여러 Client 호출 기능을 고려
- REST TEMPLATE
- FEIGN CLIENT
- WEB CLIENT

이 세가지 호출 기능을 nGrinder를 이용해 성능을 테스트해보기로 했다.

>  조건 1 (Vuser: 99, Duration: 1M)

|  | TPS | Peak TPS | TPSMean Test Time (MS) | 성공 수  | 에러 수 |
| --- | --- | --- | --- | --- | --- |
| RestTemplate | 25.6 | 49.5 | 4,032.23 | 1,386 | 0 |
| Feign Client | 24.7 | 49.5 | 4,033.81 | 1,287 | 0 |
| WebClient | 25.6 | 49.5 | 4,029.20 | 1,386 | 0 |
  

> 조건 2 (Vuser: 148, Duration: 5M)

|  | TPS | Peak TPS | TPSMean Test Time (MS) | 성공 수  | 에러 수 |
| --- | --- | --- | --- | --- | --- |
| RestTemplate | 36.9 | 74.0 | 4,026.92 | 10,804 | 9 |
| Feign Client | 36.9 | 74.0 | 4,023.21 | 10,804 | 15 |
| WebClient | 36.9 | 74.0 | 4,025.40 | 10,804 | 6 |

에러가 나오는 횟수와 TPS의 평균등을 고려하여 WebClient를 사용하여 CLOVA STUDIO API를 호출하도록 했다.


[//]: # (## ✅ TODO)

[//]: # (### 소셜로그인)

[//]: # (소셜 로그인 기능을 구현하여 사용자에 대한 꿈 관련 기록을 저장.)

[//]: # (또한, 추가적인 아카이빙 기능을 위해서는 필수일 것이라 생각.)
