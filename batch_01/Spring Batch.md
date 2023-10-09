# 시작전에

STS 설치

git 설치

mobaxterm 설치

docker-desktop 설치

docker-compose up -d 
git clone example project







# 1. Spring Batch 개요

매월마다 이전달의 매출액을 집계해야하는 상황이있습니다. 그럼 과연 이 집계 과정을 어디서 수행하면 될까요?

자바 언어와 웹 어플리케이션 밖에 모른다면 통상적으로 Tomcat + Spring MVC 가 생각이 날것같습니다
하지만 이렇게 큰 데이터를 읽고, 가공하고, 저장한다면 해당 서버는 순식간에 CPU, I/O 등의 자원을 다 써버려서 다른 Request 처리를 못하게 됩니다.

그리고 이 집계 기능은 **한달에 1번 수행**됩니다.
이를 위해 API를 구성하는 것은 너무 낭비가 아닐까요? 여기서 추가로 데이터가 너무 많아서 처리중에 실패가 나면 어떻게 될까요?
**5만번째에서 실패했다면, 5만 1번째부터 다시 실행**할 수 있다면 얼마나 좋을까요?

또 이런 경우도 있을수 있습니다.
오늘 아침 누군가가 집계 실행 파일을 실행시켰는데, 다른 누군가가 또 실행시켜 집계 데이터가 2배로 뻥튀기 될 수도 있습니다.
**같은 파라미터로 같은 실행 파일을 실행할 경우** 이미 실행한 적이 있어 실패하는 기능을 지원한다면 얼마나 좋을까요?

바로 이런 단발성으로 대용량의 데이터를 처리하는 어플리케이션을 **배치 어플리케이션**이라고 합니다.
위의 고민들을 다시 생각해보면 배치 어플리케이션을 구성하기 위해선 **비지니스 로직 외에 부가적으로 신경써야할 부분들이 많다**는 것을 알 수 있습니다.

웹 어플리케이션을 개발할때는 Spring MVC를 사용하기 때문에  비지니스 로직에 최대한 집중할 수 있습니다.
그럼 Spring에서 이런 배치성 어플리케이션을 지원하는 모듈은 무었이 있을까요? 

Spring 진영에선 **Spring Batch**가 있습니다.

Spring Batch를 소개하기전에 배치 어플리케이션이란 어떤 것인지, 만족해야 할 조건은 아래와 같습니다.

- 대용량 데이터 - 배치 어플리케이션은 **대량의 데이터를 가져오거나, 계산하거나, 전달하는  등의 처리를 할 수 있어야 합니다**.
- 자동화 - 배치 어플리케이션은 심각한 문제 해결을 제외하고는 **사용자 개입 없이 실행**되어야 합니다.
- 견고성 - 배치 어플리케이션은 **잘못된 데이터를 충돌/중단 없이 처리할 수 있어야 합니다**.
- 신뢰성 - 배치 어플리케이션은 **무엇이 잘못되었는지를 추적**할 수 있어야 합니다. (로깅, 알림)
- 성능 - 배치 어플리케이션은 **지정한 시간 안에 처리를 완료**하거나 동시에 실행되는 **다른 어플리케이션을 방해하지 않도록 수행**되어야합니다.



---





## 1.1 Spring Batch란?

엔터프라이즈 도메인 내의 많은 애플리케이션은 미션 크리티컬 환경에서 비즈니스 운영을 수행하기 위해 대량 처리가 필요합니다. 이러한 비즈니스 운영에는 다음이 포함됩니다.

- 사용자 개입 없이 가장 효율적으로 처리되는 대용량 정보의 자동화된 복잡한 처리입니다. 이러한 작업에는 일반적으로 시간 기반 이벤트(예: 월말 계산, 공지 또는 서신)가 포함됩니다.
- 매우 큰 데이터 세트(예: 보험 혜택 결정 또는 요율 조정)에 걸쳐 반복적으로 처리되는 복잡한 비즈니스 규칙을 주기적으로 적용합니다.
- 일반적으로 트랜잭션 방식으로 형식화, 검증 및 처리가 필요한 내부 및 외부 시스템에서 수신된 정보를 기록 시스템에 통합합니다. 일괄 처리는 기업에서 매일 수십억 건의 거래를 처리하는 데 사용됩니다.

Spring Batch는 기업 시스템의 일상적인 운영에 필수적인 강력한 배치 애플리케이션 개발을 가능하게 하도록 설계된 가볍고 포괄적인 배치 프레임워크입니다. Spring Batch는 사람들이 기대하는 Spring Framework의 특성(생산성, POJO 기반 개발 접근 방식, 일반적인 사용 용이성)을 기반으로 구축되는 동시에 개발자가 필요할 때 더 발전된 엔터프라이즈 서비스에 쉽게 액세스하고 사용할 수 있도록 합니다. Spring Batch는 스케줄링 프레임워크가 아닙니다. 상용 및 오픈 소스 공간 모두에서 사용할 수 있는 훌륭한 엔터프라이즈 스케줄러(예: Quartz, Tivoli, Control-M 등)가 많이 있습니다. Spring Batch는 스케줄러를 대체하기보다는 스케줄러와 함께 작동하도록 고안되었습니다.

Spring Batch는 로깅 및 추적, 트랜잭션 관리, 작업 처리 통계, 작업 재시작, 건너뛰기, 리소스 관리 등 대용량 레코드 처리에 필수적인 재사용 가능한 기능을 제공합니다. 또한 최적화 및 파티셔닝 기술을 통해 대용량, 고성능 배치 작업을 가능하게 하는 고급 기술 서비스 및 기능을 제공합니다. 간단한 사용 사례(예: 데이터베이스로 파일 읽기 또는 저장 프로시저 실행)와 복잡한 대용량 사용 사례(예: 데이터베이스 간에 대용량 데이터 이동, 변환 등) 모두에서 Spring Batch를 사용할 수 있습니다. 

대용량 배치 작업은 확장성이 뛰어난 방식으로 프레임워크를 사용하여 상당한 양의 정보를 처리할 수 있습니다.

### 1.1.1 배경

오픈 소스 소프트웨어 프로젝트 및 관련 커뮤니티는 웹 기반 및 마이크로서비스 기반 아키텍처 프레임워크에 더 많은 관심을 집중하고 있는 반면, Java 기반 배치 처리 요구 사항을 수용하기 위한 재사용 가능한 아키텍처 프레임워크에 대한 초점은 눈에 띄게 부족했습니다. 엔터프라이즈 IT 환경 내에서 처리. 재사용 가능한 표준 배치 아키텍처가 부족하여 클라이언트 엔터프라이즈 IT 기능 내에서 개발된 일회성 사내 솔루션이 많이 확산되었습니다.

SpringSource(현재 VMware)와 Accenture가 협력하여 이를 변경했습니다. 배치 아키텍처 구현에 대한 Accenture의 실제 산업 및 기술 경험, SpringSource의 깊이 있는 기술 경험, Spring의 입증된 프로그래밍 모델이 함께 자연스럽고 강력한 파트너십을 형성하여 엔터프라이즈 Java의 중요한 격차를 메우는 것을 목표로 하는 고품질의 시장 관련 소프트웨어를 만들었습니다. . 두 회사 모두 Spring 기반 배치 아키텍처 솔루션을 개발하여 비슷한 문제를 해결하고 있는 여러 고객과 협력했습니다. 이 입력은 클라이언트가 제기한 실제 문제에 솔루션을 적용할 수 있도록 하는 데 도움이 되는 몇 가지 유용한 추가 세부 정보와 실제 제약 조건을 제공했습니다.

Accenture는 지원, 개선 사항 및 기존 기능 세트를 구동하기 위한 커미터 리소스와 함께 이전에 독점적인 일괄 처리 아키텍처 프레임워크를 Spring Batch 프로젝트에 제공했습니다. Accenture의 기여는 메인프레임의 COBOL, Unix의 C++, 현재는 Java 등 지난 몇 세대의 플랫폼을 사용하여 배치 아키텍처를 구축한 수십 년간의 경험을 바탕으로 이루어졌습니다.

Accenture와 SpringSource 간의 공동 노력은 소프트웨어 처리 접근 방식, 프레임워크 및 기업 사용자가 배치 애플리케이션을 만들 때 일관되게 사용할 수 있는 도구의 표준화를 촉진하는 것을 목표로 했습니다. 기업 IT 환경에 입증된 표준 솔루션을 제공하려는 기업 및 정부 기관은 Spring Batch의 이점을 누릴 수 있습니다.

### 1.1.2 사용 시나리오

일반적인 배치 프로그램은 일반적으로 다음과 같습니다.

- 데이터베이스, 파일 또는 큐에서 많은 수의 레코드를 읽습니다.
- 어떤 방식으로든 데이터를 처리합니다.
- 수정된 형식으로 데이터를 다시 씁니다.

Spring Batch는 이러한 기본 배치 반복을 자동화하여 일반적으로 사용자 상호 작용 없이 오프라인 환경에서 유사한 트랜잭션을 집합으로 처리하는 기능을 제공합니다. 배치 작업은 대부분의 IT 프로젝트의 일부이며 Spring Batch는 강력한 엔터프라이즈 규모 솔루션을 제공하는 유일한 오픈 소스 프레임워크입니다.

#### 1.1.2.1 비즈니스 시나리오

Spring Batch는 다음 비즈니스 시나리오를 지원합니다.

- 주기적으로 일괄 처리를 커밋합니다.
- 동시 일괄 처리: 작업을 병렬로 처리합니다.
- 단계적 기업 메시지 기반 처리.
- 대규모 병렬 배치 처리.
- 실패 후 수동 또는 예약된 다시 시작.
- 종속 단계의 순차적 처리(워크플로우 기반 배치 확장 포함)
- 부분 처리: 레코드를 건너뜁니다(예: 롤백 시).
- 배치 크기가 작거나 기존 저장 프로시저 또는 스크립트가 있는 경우 전체 배치 트랜잭션입니다.

#### 1.1.2.2 기술 목표

Spring Batch에는 다음과 같은 기술적 목표가 있습니다.

- 배치 개발자가 Spring 프로그래밍 모델을 사용하도록 합니다. 비즈니스 로직에 집중하고 프레임워크가 인프라를 관리하도록 합니다.
- 인프라, 배치 실행 환경, 배치 애플리케이션 간의 우려사항을 명확하게 분리합니다.
- 모든 프로젝트가 구현할 수 있는 인터페이스로 공통 핵심 실행 서비스를 제공합니다.
- "즉시" 사용할 수 있는 핵심 실행 인터페이스의 간단한 기본 구현을 제공합니다.
- 모든 계층에서 Spring 프레임워크를 사용하여 서비스를 쉽게 구성, 사용자 정의 및 확장할 수 있습니다.
- 모든 기존 핵심 서비스는 인프라 계층에 영향을 주지 않고 쉽게 교체하거나 확장할 수 있어야 합니다.
- Maven을 사용하여 구축된 애플리케이션과 완전히 분리된 아키텍처 JAR을 사용하여 간단한 배포 모델을 제공합니다.

---





### **1.1.3 Spring Batch 장점**

1) 대용량 데이터 처리에 최적화되어 고성능을 발휘
2) 효과적인 로깅, 통계 처리, 트랜잭션 관리 등 재사용 가능한 필수 기능을 지원
3) 수동으로 처리하지 않도록 자동화되어 있습니다.
4) 예외사항과 비정상 동작에 대한 방어 기능 존재.

---



### **1.1.4 Spring Batch 단점**

\- 스케줄 기능 제공하지 않음

\- 스케줄링을 위해 jenkins나, Quarz를 같이 사용



---



## 1.2 **Spring Batch 사용 사례**

배치 애플리케이션은 일정 주기로 실행되어야 할 때나 실시간 처리가 어려운 대량의 데이터를 처리할 때 주로 사용된다.

- ETL(Extract 추출, Transform 변환, Load 적재)
- 데이터 마이그레이션(Spring Batch는 커밋 횟수 측정, 롤백 기능 제공)
- 대규모 데이터 병렬 처리

 

### **1.2.1 일/월/년 등 특정기간 별 매출 집계하기**

엔터프라이즈 단위의 데이터 집계는 하루에 100만건까지 나올 수 있다. 이를 count 쿼리로 실행하기에는 서버 부하가 심해질 것이다. 그래서 매일 새벽 매출 집계 데이터를 만들어 외부 요청이 올 경우 미리 만들어 준 집계 데이터를 바로 전달하면 성능과 부하를 모두 잡을 수 있다.

 

### 1.2.**2 ERP 연동하기**

재무팀의 요구사항으로 매일 매출 현황을 ERP로 전달해야하는 상황에서 Spring Batch가 많이 사용된다. 매일 아침 8시에 ERP에 전달해야할 매출 데이터를 전송해야한다면 아래와 같은 구조로 쉽게 구현할 수 있다.

 

### **1.2.3 구독 서비스** 

전송할 데이터 내역과 구독자 정보를 받아 정해진 시간에 구독을 신청한 회원에게 규칙적으로 메일을 일괄 전송할 때 Batch를 사용하면 쉽게 구현할 수 있다. 그러면 서버나 클라이언트나 다른 정보를 열람하는 등 다른 서비스에 영향을 주지 않는다.

 

이 외에도 큰 데이터를 활용하여 보험급여를 결정한다거나 트랜잭션 방식으로 포맷, 유효성 확인 및 처리가 필요한 내부 및 외부 시스템에서 수신한 정보를 기록 시스템으로 통합하는 등 여러 방식으로 사용할 수 있다.

 





## 1.3 Spring Batch 아키텍처

Spring Batch 계층화 아키텍처

![img](https://blog.kakaocdn.net/dn/bnFDbn/btrt1J2IjLo/GFyPOicHWmXhiTW2Ymgcck/img.png)



- **Application** 

  - -> 개발자가 만든 모든 배치 Job과 사용자 정의 코드가 포함되어 있습니다.

    -> 개발자는 비즈니스 로직 구현에만 집중하고 공통적인 기반 기술은 프레임워크가 담당합니다.

  - Spring Batch를 사용하여 개발자가 작성한 모든 배치 작업과 사용자 정의 코드
  
- **Batch Core** : 배치 작업을 시작하고 제어하는 데 필요한 핵심 런타임 클래스를 포함(JobLauncher, Job, Step 등..)
- **Batch Infrastructure** : 

  - -> Application, Batch Core 모두 Infrastructure Layer에서 빌드됩니다.

    -> Job의 흐름과 처리를 위한 틀을 제공하며 Reader, Processor, Writer 등이 속합니다.

- 개발자와 애플리케이션에서 사용하는 일반적인 Reader와 Writer 그리고 RetryTemplate과 같은 서비스를 포함

스프링 배치는 계층 구조가 위와 같이 설계되어 있기 때문에 개발자는 **Application** 계층의 비즈니스 로직에 집중할 수 있고, 배치의 동작과 관련된 것은 **Batch Core**에 있는 클래스들을 이용하여 제어할 수 있다.

 

스프링배치는 전반적으로 배치 설계를 해봤다면 익숙하고 편하게 느껴질 만한 컨셉을 사용한다. Job과 Step 

그리고 개발자가 직접 제공해야하는 처리유닛(`ItemReader` `ItemWriter`)으로 구성되어 있는데, 스프링 패턴, operation, 템플릿, 콜백 및 idiom으로 인한 다음과 같은 차별점이 있다.

- 명확한 관심사 분리
- 인터페이스로 제공하는 명확한 아키텍처 레이어와 서비스
- 빠르게 적용하고 쉽게 응용할 수 있는 간단한 디폴트 구현체
- 크게 향상된 확장성

아래는 수십 년간 사용되어온 배치 아키텍처를 간단히 나타낸 다이어그램이다. 배치 프로세싱 도메인 언어를 구성하는 컴포넌트를 개략적으로 설명한다. 이 아키텍처 프레임워크는 지난 몇 세대의 플랫폼(COBOL/Mainframe, C/Unix, and now Java/anywhere)에서 수십 년에 걸쳐 입증한 청사진이다.

스프링 배치는 견고하고 유지보수 가능한 시스템에서 일반적으로 사용하는 레이어, 컴포넌트, 기술 서비스의 물리적 구현체를 제공하는데, 이를 복잡한 요구사항을 해결하기 위한 인프라와 함께 확장하면, 단순한 배치부터 매우 복잡한 배치 응용 프로그램까지 개발할 수 있다.

![img](https://blog.kakaocdn.net/dn/T1ug9/btrtRDW0Rtm/kn0txJZU27MSmo5qDKQjZ1/img.png)



위 다이어그램은 스프링 배치의 도메인 언어를 구성하는 핵심 갠며을 나타내고있다. Job 하나는 1~-n개의 Step을 가지고 있으며 각 Step은 `ItemReader`, `ItemProcessor`, `ItemWrite`를 딱 한 개씩 가지고 있다. 각 Job은 `JobLauncher`가 실행하며, 현재 실행 중인 프로세스의 메타정보는 `JobRepository`에 저장된다.



**JobRepository**

다양한 배치 수행과 관련된 수치 데이터와 잡의 상태를 유지 및 관리한다. 

일반적으로 관계형 데이터베이스를 사용하며 스프링 배치 내의 대부분의 주요 컴포넌트가 공유한다.

실행된 Step, 현재 상태, 읽은 아이템 및 처리된 아이템 수 등이 모두 JobRepository에 저장된다.



**Job**

Job은 배치 처리 과정을 하나의 단위로 만들어 표현한 객체이고 여러 Step 인스턴스를 포함하는 컨테이너이다.

Job이 실행될 때 스프링 배치의 많은 컴포넌트는 탄력성(resiliency)을 제공하기 위해 서로 상호작용을 한다.



**JobLauncher**

Job을 실행하는 역할을 담당한다. Job.execute을 호출하는 역할이다.

Job의 재실행 가능 여부 검증, 잡의 실행 방법, 파라미터 유효성 검증 등을 수행한다.

스프링 부트의 환경에서는 부트가 Job을 시작하는 기능을 제공하므로, 일반적으로 직접 다룰 필요가 없는 컴포넌트다.

Job을 실행하면 해당 잡은 각 Step을 실행한다. 각 스텝이 실행되면 JobRepository는 현재 상태로 갱신된다.





**Step**

스프링 배치에서 가장 일반적으로 상태를 보여주는 단위이다. 각 Step은 잡을 구성하는 독립된 작업의 단위이다.

Step에는 Tasklet, Chunk 기반으로 2가지가 있다.



**Tasklet**

Step이 중지될 때까지 execute 메서드가 계속 반복해서 수행하고 수행할 때마다 독립적인 트랜잭션이 얻어진다. 초기화, 저장 프로시저 실행, 알림 전송과 같은 잡에서 일반적으로 사용된다.



**Chunk**

한 번에 하나씩 데이터(row)를 읽어 Chunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션을 다루는 것

Chunk 단위로 트랜잭션을 수행하기 때문에 실패할 경우엔 해당 Chunk 만큼만 롤백이 되고, 이전에 커밋된 트랜잭션 범위까지는 반영이 된다.

Chunk 기반 Step은 ItemReader, ItemProcessor, ItemWriter라는 3개의 주요 부분으로 구성될 수 있다.







![img](https://blog.kakaocdn.net/dn/tNi1O/btrtZ7v5HrM/4eLtLQJIhopD4xgbpkhJw0/img.png)



ItemReader와 ItemProcessor에서 데이터는 1건씩 다뤄지고, Writer에선 Chunk 단위로 처리된다.

> 일반적으로 스프링 배치는 대용량 데이터를 다루는 경우가 많기 때문에 Tasklet보다 상대적으로 트랜잭션의 단위를 짧게 하여 처리할 수 있는 ItemReader, ItemProcessor, ItemWriter를 이용한 Chunk 지향 프로세싱을 이용한다.



**Item**

작업에 사용하는 데이터이다. Item의 전체적인 Flow는 다음과 같이 읽고 처리하기 쓰기로 단순하다. 그리고 이에 대해 ItemReader, ItemProcessor, ItemWriter가 각 역할을 맞아 수행한다.

 



![img](https://blog.kakaocdn.net/dn/dZTaqa/btrCHpb1Exs/iN3zrFmeNzmjTaIbkkmjS0/img.png)



 

**ItemReader**

ItemReader는 말 그대로 데이터를 읽어들인다. DB 데이터뿐만 아니라 File, XML, JSON, CSV 등 다른 데이터 소스를 배치 처리의 입력으로 사용할 수 있다. 또한 JMS와 같은 다른 유형의 데이터 소스도 지원한다. 정리하면 다음과 같다.

- 입력 데이터에서 읽어오기
- 파일에서 읽어오기
- Database에서 읽어오기
- Java Message Service등 다른 소스에서 읽어오기
- 본인만의 커스텀한 Reader로 읽어오기

 

**ItemWriter**

ItemWriter는 Spring Batch에서 사용하는 출력 기능이다. Spring Batch가 처음 나왔을 때, ItemWriter는 ItemReader와 마찬가지로 item을 하나씩 다루었다. 그러나 Spring Batch2와 청크 (Chunk) 기반 처리의 도입으로 인해 ItemWriter에도 큰 변화가 있었다. 이 업데이트 이후 부터 **ItemWriter는 item 하나를 작성하지 않고 Chunk 단위로 묶인 item List를 다룬다.**

 

**ItemProcessor**

ItemProcessor는 데이터를 가공 (혹은 처리)한다. 해당 기능은 필수가 아니다.

- ItemProcessor는 데이터를 가공하거나 필터링하는 역할을 한다. 이는 Writer 부분에서도 충분히 구현 가능하다.
- 그럼에도 ItemProcessor를 쓰는 것은 Reader, Writer와는 별도의 단계로 기능이 분리되기 때문이다.

 

---



## 1.4 **Spring Batch 구성요소**

### **Job**

job은 전체 배치 프로세스를 캡슐화한 엔티티다. 다른 스프링 프로젝트와 마찬가지로, `Job`은 XML 기반이나 자바 기반 설정을 둘 다 지원한다. 이 설정은 “job 설정”이라고도 할 수 있지만, Job은 아래 다이어그램에서 보듯 가장 상위 개념 일 뿐이다.![img](https://blog.kakaocdn.net/dn/lyFtV/btrr0JjLRrm/NW9YDd8PAQZJXmLRb2NbSK/img.png)

#### JobInstance



`JobInstance`는 논리적인 job 실행을 뜻한다. 앞에 있는 다이어그램의 ‘EndOfDay’ `Job`처럼 하루가 끝날 때마다 한번 실행돼야 하는 배치 job을 생각해보자. ‘EndOfDay’ job은 하나지만, `Job`을 각각 실행할 때마다 따로 추적할 수 있어야 한다. 이 예시에서는 매일 하나의 논리적인 `JobInstance`가 필요하다. 예를 들어, 1월 1일 실행, 1월 2일 실행, 등등. 1월 1일에 실행한 배치가 실패해서 다음 날 다시 실행하는 경우, 1월 1일의 작업과 동일한 작업을 재실행해야 한다 (보통 처리 날짜와 처리할 데이터가 일치하는데, 1월 1일에 실행하면 1월 1일의 데이터를 처리한다는 뜻이다). 따라서 각 `JobInstance`는 실행 결과를 여럿 가질 수 있고(`JobExecution`은 이 챕터 뒷부분에 자세히 나온다), 특정 `Job`과 식별 가능한 `JobParameters`에 상응하는 `JobInstance`는 단 한 개뿐이다.

```
JobInstance`는 로드되는 데이터와는 아무런 관련이 없다. 데이터가 로드되는 방법은 전적으로 `ItemReader` 구현에 달려있다. 예를 들어 앞에서 나온 EndOfDay 케이스에서는, 데이터에 배치를 실행해야 하는 날짜를 의미하는 컬럼이 있을 것이다. 즉 1월 1일 실행은 1일 데이터만 로드하고, 1월 2일 실행은 2일 데이터만 사용할 것이다. 이러한 결정은 비지니스적 요구사항일 가능성이 크므로 `ItemReader`가 결정하도록 설계되었다. 그러나 `JobInstance` 재사용 여부는 이전 실행에서 사용된 상태(`ExecutionContext`는 이번 챕터의 뒷부분에 나온다)를 그대로 사용할지 말지를 결정한다. 새 `JobInstance를 사용한다는것은 처음부터 시작 하는것을 의미하고 이미 있는 instance를 쓴다는것은 멈추었던 곳에서 부터 시작 을 의미한다.

```





Job이 실행될 때 생성되는 Job의 논리적 실행 단위 객체로서 고유하게 식별 가능한 작업 실행을 나타냅니다.

Job의 설정과 구성은 동일하지만 Job이 실행되는 시점에 처리하는 내용은 다릅니다. 즉, 같은 잡을 실행하지만 오늘 실행되는 내용과 내일 실행되는 내용이 다르다는것을 나타냅니다. 

 

JobInstance는 새로 시작되는 배치일 경우 Job + JobParameter로 새로운 JobInstance를 생성합니다. 그런데 이전과 동일한 Job + Jobparameter로 실행될 경우 이미 존재하는 JobInstance를 반환하게 됩니다. 

 

Job과 JobInstance는 1:M의 관계를 가지면서 BATCH_JOB_INSTANCE 테이블과 매핑되는데 JOB_NAME(Job)과 JOB_KEY(JobParameter)가 동일한 데이터는 중복해서 저장이 불가능합니다.

 

1) JobInstance 생성 Flow

![img](https://blog.kakaocdn.net/dn/bFlw6x/btrtyePO0Oz/sP0CvKwGjUhtOAdWDbM7E0/img.png)

2) BATCH_JOB_INSTACE Table

| JOB_INSTANCE_ID | JOB_NAME  | JOB_KEY                          |
| --------------- | --------- | -------------------------------- |
| 1               | SimpleJob | d52d8cd98d00e214e9412998ecf2457e |
| 2               | SimpleJob | e41z8cb98q00b204e9800998ecf8427e |

JOB_INSTANCE_ID에는 각 다른 인스턴스 값이 저장이 되고

JOB_NAME에는 등록된 잡의 이름이 저장이 되고

JOB_KEY에는 Job Parameter를 해싱한 값이 저장이 됩니다. 

JobName + JobParamter는 중복된 데이터는 가질수 없습니다.

 

#### JobParameter

\- Job을 실행할 때 함께 포함되어 사용되는 파라미터를 가진 도메인 객체

\- JobParameter와 JobInstance는 1대1 관계

 `JobInstance`가 Job과 어떻게 다른지 이야기하다 보면 보통 이런 질문이 나온다: “`JobInstance`는 다른 JobInstance와 어떻게 구분하지?” 정답은 `JobParameters`다. `JobParameters`는 배치 job을 시작할 때 사용하는 파라미터 셋을 가지고 있는 객체다. 아래 그림에서 보이듯, 실행 중 job을 식별하거나 참조 데이터로도 사용할 수 있다.

![Job Parameters](https://godekdls.github.io/images/springbatch/job-parameters.png)

앞에 나온 예시에서는 각 1월 1일, 1월 2일, 총 두 개의 인스턴스가 있는데, `Job`은 하나지만 `JobParameter`가 두 개 있다: 하나는 2017/01/01에 사용된 파라미터, 다른 하나는 2017/01/02에 사용된 파라미터. 따라서 이 공식이 도출된다: `JobInstance` = `Job` + 식별용 `JobParameters`. 덕분에 개발자는 효율적으로 `JobInstance`를 정의할 수 있으며, 거기 사용될 파라미터도 제어할 수 있다.

> 모든 job 파라미터를 `JobInstance`를 식별하는 데 사용하진 않는다. 기본적으로는 그렇지만, 프레임워크는 `JobInstance`ID 에 관여하지 않는 파라미터를 사용할 수도 있다.

  

#### JobExecution

JobLauncher : Job을 실행시키는 주체

JobInstance에 대한 한번의 시도를 의미하는 객체로서 Job 실행 중에 발생한 정보들을 저장하는 객체

해당 JobExecution은 JobInstance가 실행 될 때 마다 생성된다.

JobExecution은 FAILED 또는 COMPLETED 등의 Job의 실행 결과 상태를 가지고 있다.

FAILED : JobInstance 실행이 완료되지 않은 것으로 간주해서 재실행이 가능, JobParameter의 동일한 값으로 계속 실행 가능

COMPLETED : JobInstance 실행이 완료된 것으로 간주해서 재실행이 불가능

 

1) BATCH_JOB_EXECUTION Table

\- JobInstance와 JobExecution은 1:M의 관계로서 JobInstance에 대한 성공/실패 내역을 가지고 있다.



`JobExecution` 개념은 Job을 한번 실행하려 했다는 것이다. 하나의 실행은 성공하거나 실패하게 되는데, 실행에 상응하는 `JobInstance`는 실행이 성공적으로 종료되기 전까지는 완료되지 않은 것으로 간주한다. 앞에 나온 EndOfDay `Job`을 실행하고 처음엔 실패한 2017/01/01의 `JobInstance`를 생각해보자. 첫 번째 실행(2017/01/01)과 똑같은 job 파라미터로 재실행한다면, 새 `JobExecution`이 생성된다. 반면 `JobInstance`는 여전히 한 개다.

`Job`은 job이 무엇인지와 어떻게 실행되어야 하는지를 설명하고, `JobInstance`는 주로 올바른 재시작을 위해 실행을 그룹화하는 순수한 구조적 오브젝트다. 반면 `JobExecution`은 실제 실행 중에 필요한 기본 스토리지 메커니즘을 제공하며, 아래 테이블에서 보이듯 더 많은 프로퍼티를 관리하고 유지해야 한다.

**Table 1. JobExecution Properties**

|     Property      |                          Definition                          |
| :---------------: | :----------------------------------------------------------: |
|      Status       | 실행 상태를 나타내는 `BatchStatus` 오브젝트. 실행 중일 때는 `BatchStatus#STARTED`. 실패하면 `BatchStatus#FAILED`. 성공적으로 종료되면 `BatchStatus#COMPLETED` |
|     startTime     | job을 실행할 때의 시스템 시간을 나타내는 `java.util.Date`. 아직 job이 시작되지 않았다면 이 필드는 비어있다. |
|      endTime      | 성공 여부와 상관없이 실행이 종료될 때의 시스템 시간을 나타내는 `java.util.Date`. 아직 job이 종료되지 않았다면 이 필드는 비어있다. |
|    exitStatus     | 실행 결과를 나타내는 `ExitStatus`. 호출자에게 리턴하는 종료 코드를 포함하기 때문에 가장 중요하다. 자세한 내용은 5장 참고. 아직 job이 종료되지 않았다면 이 필드는 비어있다. |
|    createTime     | `JobExecution`이 처음 저장될 때의 시스템 시간을 나타내는 `java.util.Date`. job은 시작되지 않았을 수도 있는데(따라서 시작 시간이 없을 수도 있음), createTime은 프레임워크가 job 레벨의 `ExecutionContext`을 관리할 때 사용하기 때문에 항상 존재한다. |
|    lastUpdated    | `JobExecution`이 저장된 마지막 시간을 나타내는 `java.util.Date`. 아직 job이 시작되지 않았다면 이 필드는 비어있다. |
| executionContext  | 실행하는 동안 유지해야 하는 모든 사용자 데이터를 담고 있는 “property bag”. |
| failureExceptions | `Job` 실행 중 발생한 예외 리스트. `Job`이 실패할 때 둘 이상의 예외가 발생한 경우에 유용하다. |

이 프로퍼티를 저장해 두면 실행 상태를 결정할 수 있으므로 매우 중요하다. 예를 들어, 01-01의 EndOfDay job을 9:00 PM에 실행해서 9:30에 실패했다면, 배치 메타 데이터 테이블에 아래 엔트리들이 저장된다:

**Table 2. BATCH_JOB_INSTANCE**

| JOB_INST_ID |  JOB_NAME   |
| :---------: | :---------: |
|      1      | EndOfDayJob |

**Table 3. BATCH_JOB_EXECUTION_PARAMS**

| JOB_EXECUTION_ID | TYPE_CD |   KEY_NAME    |  DATE_VAL  | IDENTIFYING |
| :--------------: | :-----: | :-----------: | :--------: | :---------: |
|        1         |  DATE   | schedule.Date | 2017-01-01 |    TRUE     |

**Table 4. BATCH_JOB_EXECUTION**

| JOB_EXEC_ID | JOB_INST_ID |    START_TIME    |     END_TIME     | STATUS |
| :---------: | :---------: | :--------------: | :--------------: | :----: |
|      1      |      1      | 2017-01-01 21:00 | 2017-01-01 21:30 | FAILED |

> 단순화와 포맷팅을 위해 일부 컬럼명은 축약하거나 제외했다.

job이 실패했고, 밤새도록 문제를 찾느라 ‘배치 윈도우’가 이제야 닫혔다고 가정해보자. 또한 윈도우는 9:00 PM에 시작하고, 다음 날 중단했던 위치에서부터 job을 다시 시작해서 9:30에 01-01 데이터를 모두 처리했다고 가정해보자. 하루가 지났으므로 01-02 job도 실행해야 하는데, 9시 31분에 바로 시작해서 1시간이 걸려 10시 30분에 정상적으로 종료된 상황이다. 두 job이 동일한 데이터에 접근해서 데이터베이스 레벨에서 잠금 문제를 일으킬 일이 없다면, `JobInstance` 한 개씩 순차적으로 진행할 필요는 없다. `Job`을 실행할지 말지 결정하는 일은 전적으로 스케줄러에 달려있다. 여기서 두 `JobInstance`는 독립적이므로, 스프링 배치는 동시에 실행한다고 해서 job을 중단시키지 않는다. (`JobInstance`가 이미 실행 중인데 같은 `JobInstance`를 실행하려고 하면 `JobExecutionAlreadyRunningException`이 발생한다). 아래 표를 보면 이제 `JobInstance` 및 `JobParameters` 테이블에 엔트리 하나씩이 추가됐고, `JobExecution` 테이블에 두 개의 엔트리가 추가됐다.

**Table 5. BATCH_JOB_INSTANCE**

| JOB_INST_ID |  JOB_NAME   |
| :---------: | :---------: |
|      1      | EndOfDayJob |
|      2      | EndOfDayJob |

**Table 6. BATCH_JOB_EXECUTION_PARAMS**

| JOB_EXECUTION_ID | TYPE_CD |   KEY_NAME    |  DATE_VAL  | IDENTIFYING |
| :--------------: | :-----: | :-----------: | :--------: | :---------: |
|        1         |  DATE   | schedule.Date | 2017-01-01 |    TRUE     |
|        2         |  DATE   | schedule.Date | 2017-01-01 |    TRUE     |
|        3         |  DATE   | schedule.Date | 2017-01-02 |    TRUE     |

**Table 7. BATCH_JOB_EXECUTION**

| JOB_EXEC_ID | JOB_INST_ID |    START_TIME    |     END_TIME     |  STATUS   |
| :---------: | :---------: | :--------------: | :--------------: | :-------: |
|      1      |      1      | 2017-01-01 21:00 | 2017-01-01 21:30 |  FAILED   |
|      2      |      1      | 2017-01-02 21:00 | 2017-01-02 21:30 | COMPLETED |
|      3      |      2      | 2017-01-02 21:31 | 2017-01-02 22:29 | COMPLETED |

> 단순화와 포맷팅을 위해 일부 컬럼명은 축약하거나 제외했다.

------

### **Step**

Job의 실행 단계를 나타내는 것으로, Tasklet 또는 Chunk 지향 처리 방식으로 구성되어 있다.

 \- Batch Job을 구성하는 독립적인 하나의 단계로서 실제 배치 처리를 정의하고 컨트롤하는데 필요한 모든 정보를 가지고 있는 도메인 객체

\- 모든 Job은 하나 이상의 step으로 구성됨

 

#### 기본 구현체

1) Tasklet

\- 가장 기본이 되는 클래스로서 Tasklet 타입의 구현체들을 제어

 

2) PartitionStep

\- 멀티 스레드 방식으로 Step을 여러 개로 분리해서 실행

 

3) JobStep

\- Step 내에서 Job을 실행

 

4) FlowStep

\- Step 내에서 Flow를 실행

 

#### 3. StepExecution

\- Step에 대한 한번의 시도를 의미하는 객체로서 Step 실행 중에 발생한 정보들을 저장하는 객체

\- Step이 매번 시도 될 때마다 각 Step별로 생성

\- Job이 재시작 하더라도 이미 성공적으로 완료된 Step은 재시작하지 않고 실패된 Step만 실행

\- 이전 단계에서 Step이 실패해서 현재 Step을 실행하지 않았다면 StepExecution을 생성하지 않음.

즉, Step이 실제로 시작되었을 때만 StepExecution을 생성

 

#### 4. Flow

![img](https://blog.kakaocdn.net/dn/bLvjbe/btrwRylcb0g/K5H1auAW2C1g611lCdA4D1/img.png)

 

#### 4. StepContribution

\- 청크 프로세스의 변경사항을 StepExecution에 업데이트 해주는 도메인 객체

\- 청크 커밋 직전에 StepExecution의 apply 메소드를 호출하여 상태를 업데이트

\- ExitStatus의 기본 종료 코드 외 사용자 정의 종료코드를 생성해서 적용 가능





`Step`은 배치 job의 독립적이고 순차적인 단계를 캡슐화한 도메인 객체다. 즉, 모든 Job은 하나 이상의 step으로 구성된다. 

`Step`은 배치 job의 독립적이고 순차적인 단계를 캡슐화한 도메인 객체다. 즉, 모든 Job은 하나 이상의 step으로 구성된다. `Step`은 실제 배치 처리를 정의하고 컨트롤하는 데 필요한 모든 정보를 가지고 있다. 설명이 모호하게 느껴질 수도 있는데, `Step`의 모든 내용은 `Job`을 만드는 개발자 재량이기 때문에 그렇다. `Step`은 어떻게 개발하느냐에 따라 간단할 수도 있고 복잡할 수도 있다. 즉, 단순히 데이터베이스에서 파일을 읽는, 코드가 거의 필요 없거나 전혀 필요하지 않은(사용한 구현체에 따라 다름) 간단한 작업이 될 수도 있고, 프로세싱 일부를 처리하는 복잡한 비지니스 로직도 될 수 있다. 아래 그림처럼 `Job`이 고유 `JobExecution`이 있듯, `Step`도 각자의 `StepExecution`이 있다.

![Job Hierarchy With Steps](https://godekdls.github.io/images/springbatch/job-hierarchy-with-steps.png)

### 3.2.1. StepExecution

`StepExecution`은 한 번의 `Step` 실행 시도를 의미한다. `StepExecution`은 `JobExecution`과 유사하게 `Step`을 실행할 때마다 생성한다. 하지만 이전 단계 step이 실패해서 step을 실행하지 않았다면 execution을 저장하지 않는다. `Step`이 실제로 시작됐을 때만 `StepExecution`을 생성한다.

`Step` execution은 `StepExecution` 클래스 객체다. 각 execution은 실행/종료 시각이나 커밋/롤백 횟수를 포함한 해당 step, `JobExecution`, 트랜잭션 관련 데이터를 가지고 있다. 추가로, 각 step execution은 `ExecutionContext`를 가지고 있는데 여기에는 통계나 재시작 시 필요한 상태 정보 등 배치 작업에서 유지해야 하는 모든 데이터가 있다. `StepExecution`의 프로퍼티는 아래 테이블에 있다.

**Table 8. StepExecution Properties**

|     Property     |                          Definition                          |
| :--------------: | :----------------------------------------------------------: |
|      Status      | 실행 상태를 나타내는 `BatchStatus` 객체. 실행 중일 때는 `BatchStatus.STARTED`. 실패하면 `BatchStatus.FAILED`. 성공하면 `BatchStatus.COMPLETED.` |
|    startTime     | step을 실행할 때의 시스템 시간을 나타내는 `java.util.Date`. 아직 step이 시작되지 않았다면 이 필드는 비어있다. |
|     endTime      | 성공 여부와 상관없이 실행이 종료될 때의 시스템 시간을 나타내는 `java.util.Date`. 아직 step이 종료되지 않았다면 이 필드는 비어있다. |
|    exitStatus    | 실행 결과를 나타내는 `ExitStatus`. 호출자에게 리턴하는 종료 코드를 포함하기 때문에 가장 중요하다. 자세한 내용은 5장 참고. 아직 job이 종료되지 않았다면 이 필드는 비어있다. |
| executionContext | 실행하는 동안 유지해야 하는 모든 사용자 데이터를 담고 있는 “property bag”. |
|    readCount     |                 성공적으로 read한 아이템 수.                 |
|    writeCount    |                성공적으로 write한 아이템 수.                 |
|   commitCount    |                실행 중에 커밋된 트랜잭션 수.                 |
|  rollbackCount   |          `Step`에서 처리된 트랜잭션 중 롤백된 횟수.          |
|  readSkipCount   |                 read에 실패해서 스킵된 횟수.                 |
| processSkipCount |               process에 실패해서 스킵된 횟수.                |
|   filterCount    |          `ItemProcessor`에 의해 필터링된 아이템 수.          |
|  writeSkipCount  |                write에 실패해서 스킵된 횟수.                 |

------

#### 3.3 ExecutionContext

`ExecutionContext`는 프레임워크에서 유지/관리하는 키/값 쌍의 컬렉션으로, `StepExecution` 객체 또는 `JobExecution` 객체에 속하는 상태(state)를 저장한다. Quartz에 익숙하다면, JobDataMap과 유사하게 느껴질 것이다. 쉽게 말해 재시작을 용이하게 해준다는 건데, 예를 들어 플랫(flat) 파일을 입력으로 받아 각 라인별로 처리한다면 프레임워크는 커밋 지점에 주기적으로 `ExecutionContext`를 저장한다. 이렇게 하면 `ItemReader`는 실행 중 전원이 나가버리는 등의 치명적인 에러가 발생했을 때에도 상태를 저장해둘 수 있다. 다음 예제와 같이 현재 읽은 라인 수를 컨텍스트에 넣기만 하면 나머지는 프레임워크가 다 처리한다.

```
executionContext.putLong(getKey(LINES_READ_COUNT), reader.getPosition());
```

`Job`의 개념 설명할 때 예시로 사용한 EndOfDay를 그대로 가져와서, 데이터베이스에서 파일을 읽는 ‘loadData’ step 하나가 있다고 가정해보자. 첫 실행에 실패한 이후의 메타 데이터 테이블은 아래와 같을 것이다:

**Table 9. BATCH_JOB_INSTANCE**

| JOB_INST_ID |  JOB_NAME   |
| :---------: | :---------: |
|      1      | EndOfDayJob |

**Table 10. BATCH_JOB_EXECUTION_PARAMS**

| JOB_INST_ID | TYPE_CD |   KEY_NAME    |  DATE_VAL  |
| :---------: | :-----: | :-----------: | :--------: |
|      1      |  DATE   | schedule.Date | 2017-01-01 |

**Table 11. BATCH_JOB_EXECUTION**

| JOB_EXEC_ID | JOB_INST_ID |    START_TIME    |     END_TIME     | STATUS |
| :---------: | :---------: | :--------------: | :--------------: | :----: |
|      1      |      1      | 2017-01-01 21:00 | 2017-01-01 21:30 | FAILED |

**Table 12. BATCH_STEP_EXECUTION**

| STEP_EXEC_ID | JOB_EXEC_ID | STEP_NAME |    START_TIME    |     END_TIME     | STATUS |
| :----------: | :---------: | :-------: | :--------------: | :--------------: | :----- |
|      1       |      1      | loadData  | 2017-01-01 21:00 | 2017-01-01 21:30 | FAILED |

**Table 13. BATCH_STEP_EXECUTION_CONTEXT**

| STEP_EXEC_ID |    SHORT_CONTEXT    |
| :----------: | :-----------------: |
|      1       | {piece.count=40321} |

위 예시에서 `Step`은 30분 동안 실행됐고, 40,321개의 ‘peices’(여기서는 이파일의 라인 수를 의미)를 처리했다. 이 값은 프레임워크가 각 커밋 전 업데이트하며, `ExecutionContext` 내 엔티티에 해당하는 여러 row를 포함할 수 있다. 커밋 전에 통지 받으려면 여러 `StepListener` 구현체(또는 `ItemStream`) 중 하나가 필요한데, 자세한 내용은 이 가이드 뒷부분에 나온다. 이전 예시와 동일하게 다음 날 `Job`을 재실행했다고 가정한다. 재시작할 때 데이터베이스로부터 마지막 실행을 가리키는 `ExecutionContext` 값을 조회한다. 아래 예제처럼, `ItemReader`가 열릴 때 컨텍스트에 저장된 상태가 있는지 확인하고, 있다면 해당 컨텍스트를 참조해서 초기화한다.

```
if (executionContext.containsKey(getKey(LINES_READ_COUNT))) {
    log.debug("Initializing for restart. Restart data is: " + executionContext);

    long lineCount = executionContext.getLong(getKey(LINES_READ_COUNT));

    LineReader reader = getReader();

    Object record = "";
    while (reader.getPosition() < lineCount && record != null) {
        record = readLine();
    }
}
```

위 코드를 실행하면 현재 라인은 40322이므로, 중단됐던 위치부터 `Step`을 다시 실행할 수 있다. 실행 자체에 관한 통계 데이터도 `ExecutionContext`를 활용하면 된다. 예를 들어, 여러 줄을 한 번에 처리해야 하고 그 처리에도 순서가 있다면 몇 번째 순서까지 진행했는지도 기록해야 한다 (단순히 읽어온 라인 수를 기록하는 것과는 다르다). 따라서 `Step`이 끝날 때 몇 번째 순서까지 진행했는지를 메일로 보낸다고 해보자. 프레임워크는 어떤 `JobInstance`를 사용하냐에 따라 그에 맞는 상태를 저장해주는데, 이미 있는 `ExecutionContext`를 사용할지 말지 판단하는 일은 쉽지만은 않다. 위에 나온 ‘EndOfDay’로 예를 들면, 01-01 배치를 두 번째로 실행할 때는 동일한 `JobInstance`를 사용하기 때문에 각 `Step`에서 데이터베이스로부터 `ExecutionContext`를 읽어와 함께 처리한다(`StepExecution`의 일부로). 반대로 01-02 배치에선 다른 `JobInstance`를 사용하므로 `Step`은 빈 컨텍스트를 주입받는다. 프레임워크는 여러 가지 상황을 고려해 job 인스턴스와 컨텍스트를 결정한다. `StepExecution`이 생길 때마다 `ExecutionContext`가 하나씩 생긴다는 점도 알아두자. `ExecutionContext`는 keyspace를 공유하기 때문에 주의해서 사용해야 한다. 즉, 데이터가 겹쳐 써지지 않도록 값을 넣을 때 주의해야 한다. 반대로 `Step`은 데이터 저장하지 않으므로 별다른 영향을 주지 않는다.

또한 `JobExecution` 당 `ExecutionContext`가 하나인 것처럼, 모든 `StepExecution` 마다 `ExecutionContext`를 한 개씩 가지고 있다는 점도 잊지 말자. 예시로 아래 코드를 보자:

```
ExecutionContext ecStep = stepExecution.getExecutionContext();
ExecutionContext ecJob = jobExecution.getExecutionContext();
//ecStep does not equal ecJob
```

주석에서 말하듯이 `ecStep`과 `ecJob`은 같지 않다. `ExecutionContext`는 두 종류가 있다. 하나는 `Step` 레벨로 `Step` 내에서 커밋할 때마다 저장하고, `Job` 레벨의 컨텍스트는 모든 `Step` 실행 사이마다 저장한다.

------





### **Tasklet**

Step에서 실행되는 최소 실행 단위이다. 스프링에서 제공하는 Tasklet 인터페이스를 구현하여 실행시킬 수 있다. Tasklet이 실행되면 Job의 다음 Step으로 넘어가게 된다.

 

### **Chunk 지향 처리(Chunk-oriented Processing)**

대용량 데이터를 처리할 때 사용되는 방식으로, 지정된 chunk size 만큼 데이터를 처리하고 다음 chunk를 처리하는 방식이다. 이 방식은 데이터 처리의 속도를 높이고, 메모리를 효율적으로 사용할 수 있다.

Chunk 지향 처리 방식의 Step은 ItemReader, ItemProcessor, ItemWriter로 구성되어 있다.

 

**ItemReader**

데이터베이스의 데이터 또는 파일 등을 읽어서 반환하는 역할을 수행한다. ItemReader는 첫 번째 데이터부터 하나씩 읽어 ItemProcessor에게 전달한다. 모든 데이터를 읽어 들이면 null을 반환하고 처리가 종료된다.

 

**ItemProcessor**

ItemReader로부터 읽어온 데이터를 가공하거나 필터링하는 역할을 수행한다. 가공된 데이터는 ItemWriter에 전달한다.

 

**ItemWriter**

가공된 데이터를 저장하거나 파일, 데이터베이스 등의 외부 저장소에 출력하는 역할을 수행한다. 만약, 예외가 발생하면 롤백이 가능하다.

 

#### **JobRepository**

스프링 배치에서 Job의 실행 정보를 저장하고 관리하는 데이터베이스로 JobLauncher에 의해 사용된다. 스프링에서는 기본적으로 메모리 기반의 JobRepository를 제공한다.

 

**JobExecution**

Job의 실행 정보를 저장하며 JobInstance와 연관된다.

 

**JobInstance**

Job의 논리적 실행 단위를 의미한다.

 

**StepExecution**

Job의 실행 과정에서 Step의 실행 정보를 저장하며, JobExecution과 연관된다. 한 개의 JobExecution은 여러 개의 StepExecution을 가질 수 있다.

 

**ExecutionContext**

Job 또는 Step 실행 중에 사용자가 저장하고자 하는 임시 데이터를 저장한다.

 

#### **JobLauncher**

Job을 실행하는 인터페이스이다. JobRepository에서 Job의 실행 정보를 읽어 Job을 실행하고 결과를 JobRepository에 저장한다.

 

#### **JobOperator**

Job의 실행, 중지, 재시작 등의 작업을 수행하는 인터페이스이다. 스프링 배치에서는 JobOperator 인터페이스를 구현하는 SimpleJobOperator를 제공하고 있다.







# 2. Spring Batch 실습

## 2.1 사전준비

### 2.1.1 DataBase 준비

```
docker run --name mariadb -d -p 3307:3306 --restart=always -e MYSQL_ROOT_PASSWORD=mariadb mariadb
docker exec -it mariadb bash
mariadb -u root -p

#mysql create db and user
CREATE DATABASE cjs_db;
use cjs_db
CREATE USER 'cjs'@'%' IDENTIFIED BY 'new1234!';
GRANT ALL PRIVILEGES ON cjs_db.* TO 'cjs'@'%';
flush privileges; 
exit;


#webflux 사용자 계정 으로 webflux_db연결
mariadb –u cjs –p
show databases;
use cjs_db


CREATE TABLE tatbestand(  
    id INT NOT NULL AUTO_INCREMENT,  
    tattag DATE,  
    tatzeit VARCHAR(100) NOT NULL,  
    tatort VARCHAR(40) NOT NULL,  
    tatort2 VARCHAR(100) NOT NULL,  
    tatbestand VARCHAR(100) NOT NULL,  
    betrag INT NOT NULL,
    PRIMARY KEY ( id )
);

CREATE TABLE user(  
    id INT NOT NULL ,  
    first_name VARCHAR(100) NOT NULL,  
    last_name VARCHAR(40) NOT NULL,  
    email VARCHAR(100) NOT NULL,  
    gender VARCHAR(100) NOT NULL,  
    ip_address VARCHAR(100) NOT NULL,  
    country_code VARCHAR(100) NOT NULL,  
    PRIMARY KEY ( id )
);
```



## 2.2 맛보기 코드 실행

Spring Batch의 의존성을 추가함으로써 Spring Batch를 사용할 수 있습니다.

프로젝트를 첫 생성 시 Spring Batch Dependency를 추가하게 되면 spring-boot-starter-batch 의존성이 추가되는 것을 확인해 볼 수 있습니다. 

*Spring Boot 3 버전으로 사용 시 Spring Batch 버전이 5 버전으로 의존성이 추가됩니다. 



**src/main/java/com/example/batchprocessing/BatchProcessingApplication.java**

```java
package com.example.batchprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessingApplication {

  public static void main(String[] args) throws Exception {
    System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));
  }
}
```

`@SpringBootApplication` 은 다음을 모두 추가하는 어노테이션입니다.

- `@Configuration`: Application Context에 대한 Bean 정의의 소스로 클래스에 태그를 지정합니다.
- `@EnableAutoConfiguration`: 클래스 경로 설정, 기타 빈 및 다양한 속성 설정을 기반으로 빈 추가를 시작하도록 Spring Boot에 지시합니다.
- `@ComponentScan`: Spring에 패키지의 다른 구성 요소, 구성 및 서비스를 찾아 `com/example` 패키지 하위의 컨트롤러를 찾도록 합니다.



### 2.2.1 샘플 코드 다운로드



### jobname 설정

spring.batch.job.name: ${job.name:NONE}

Spring Batch가 실행될때, **Program arguments로 `job.name` 값이 넘어오면 해당 값과 일치하는 Job만 실행**하겠다는 것입니다.
여기서 `${job.name:NONE}`을 보면 `:`를 사이에 두고 좌측에 `job.name`이, 우측에 `NONE`이 있는데요.
이 코드의 의미는 `job.name`**이 있으면** `job.name`**값을 할당하고, 없으면** `NONE`**을 할당**하겠다는 의미입니다.
중요한 것은! `spring.batch.job.names`에 `NONE`이 할당되면 **어떤 배치도 실행하지 않겠다는 의미**입니다.
즉, 혹시라도 **값이 없을때 모든 배치가 실행되지 않도록 막는 역할**입니다.





## MetaTable

**/org/springframework/batch/core/schema-\*.sql**

Spring Batch 메타데이터 테이블은 Java에서 이를 나타내는 Domain 객체와 매우 밀접하게 일치합니다. 예를 들어, `JobInstance`, , 및 는 각각 BATCH_JOB_INSTANCE, BATCH_JOB_EXECUTION, BATCH_JOB_EXECUTION_PARAMS 및 BATCH_STEP_EXECUTION에 매핑됩니다 `JobExecution`. BATCH_JOB_EXECUTION_CONTEXT 및 BATCH_STEP_EXECUTION_CONTEXT에 모두 매핑됩니다. 그만큼 `JobParameters``StepExecution``ExecutionContext``JobRepository`각 Java 객체를 올바른 테이블에 저장하고 저장하는 일을 담당합니다. 다음 부록에서는 메타데이터 테이블을 생성할 때 내려진 많은 디자인 결정과 함께 메타데이터 테이블을 자세히 설명합니다. 아래의 다양한 테이블 생성 명령문을 볼 때 사용된 데이터 유형이 최대한 일반적이라는 점을 인식하는 것이 중요합니다. Spring Batch는 개별 데이터베이스 공급업체의 데이터 유형 처리 차이로 인해 다양한 데이터 유형을 갖는 많은 스키마를 예제로 제공합니다. 다음은 6개 테이블 모두의 ERD 모델과 서로의 관계입니다.

![img](https://docs.spring.io/spring-batch/docs/3.0.x/reference/html/images/meta-data-erd.png)

https://docs.spring.io/spring-batch/docs/3.0.x/reference/html/metaDataSchema.html



### Job 관련 테이블

#### BATCH_JOB_INSTANCE

\- Job이 실행될 때 **JobInstance 정보가 저장**되며 **job_name, job_key를 키로 하여 하나의 데이터가 저장되기 때문에 중복되는 Job을 저장할 수 없습니다**.

| Column Name     | Column Explanation                                           |
| --------------- | ------------------------------------------------------------ |
| JOB_INSTANCE_ID | Job 인스턴스를 식별하는 고유 ID. getId 메서드를 호출하여 얻을 수 있다. |
| VERSION         | 업데이트 될 때마다 1씩 증가                                  |
| JOB_NAME        | Job을 구성할 때 부여하는 Job의 이름                          |
| JOB_KEY         | job_name과 job_parameter를 합쳐 해싱한 값을 저장             |

 

#### BATCH_JOB_EXECUTION

\- 배치 Job의 실행 정보가 담기며 **Job의 생성, 시작, 종료 시간, 실행 상태 등의 정보를 저장**합니다.

| Column Name      | Column Explanation                                           |
| ---------------- | ------------------------------------------------------------ |
| JOB_EXECUTION_ID | JobExecution을 고유하게 식별할수 있는 기본키, JOB_INSTANCE와 일대 다 관계. getId 메서드를 호출하여 얻을 수 있다. |
| VERSION          | 업데이트 될 때마다 1씩 증가                                  |
| JOB_INSTANCE_ID  | BATCH_JOB_INSTANCE 테이블의 외래 키                          |
| CREATE_TIME      | 실행(Execution)이 생성된 시점을 TimeStamp 형식으로 기록      |
| START_TIME       | 실행(Execution)이 시작된 시점을 TimeStamp 형식으로 기록      |
| END_TIME         | 성공 또는 실패에 관계 없이 실행(Execution)이 완료된 시점을 TimeStamp 형식으로 기록  단, Job 실행 도중 오류가 발생해서 Job이 중단된 경우 값이 저장되지 않을 수 있음 |
| STATUS           | 실행 상태(BatchStatus)를 저장(COMPLETED, STARTED...). BatchStatus의 Enumeration |
| EXIT_CODE        | 실행 종료(ExitStatus) 코드를 저장(COMPLETED, STARTED...)     |
| EXIT_MESSAGE     | Job이 종료된 방법에 대한 설명을 나타냄. Status가 실패일 경우 실패 원인 등의 내용을 저장 |
| LAST_UPDATED     | 마지막 실행(Execution) 시점을 TimeStamp 형식으로 기록        |

 

#### BATCH_JOB_EXECUTION_PARAMS

\- Job과 함께 실행되는 **JobParameter 정보를 저장**합니다.

| Column Name      | Column Explanation                                           |
| ---------------- | ------------------------------------------------------------ |
| JOB_EXECUTION_ID | JobExecution 식별 키. JOB_EXECUTION과는 일대다 관계. 그렇기 때문에 하나의 JOB_EXECUTION_ID에는 여러개의 파라미터 값을 가질수 있음 |
| TYPE_CD          | 저장된 값의 타입 정보를 저장(STRING, LONG, DATE...). 유형을 알아야 하기 때문에 null 일 수 없음 |
| KEY_NAME         | 파라미터 키                                                  |
| STRING_VAL       | 타입이 String일 경우 저장되는 값                             |
| DATE_VAL         | 타입이 Date일 경우 저장되는 값                               |
| LONG_VAL         | 타입이 Long일 경우 저장되는 값                               |
| DOUBLE_VAL       | 타입이 Double일 경우 저장되는 값                             |
| IDENTIFTING      | 배치 잡 파라미터의 값을 식별 여부를 저장 (TRUE, FALSE)       |

 

#### BATCH_JOB_EXECUTION_CONTEXT

\- Job의 실행 동안 여러 가지 상태 정보, 공유 데이터를 직렬화(Json 형식) 해서 저장합니다.

\- ExecutionContext 객체가 가지고 있는 데이터를 통해 Step 간 서로 데이터 공유가 가능합니다.

| Column Name        | Column Explanation                                          |
| ------------------ | ----------------------------------------------------------- |
| STEP_EXECUTION_ID  | JobExecution 식별 키. JOB_EXECUTION 마다 각 생성            |
| SHORT_CONTEXT      | Job의 실행 상태 정보, 공유 데이터 등의 정보를 문자열로 저장 |
| SERIALIZED_CONTEXT | 직렬화된 전체 컨텍스트                                      |



### Step 관련 테이블

#### BATCH_STEP_EXECUTION

\- Step의 실행 정보가 저장되며 **생성, 시작, 종료 시간 등을 관리**합니다.

| Column Name        | Column Explanation                                           |
| ------------------ | ------------------------------------------------------------ |
| STEP_EXECUTION_ID  | Step의 실행벙보를 고유하게 식별할 수 있는 기본 키            |
| VERSION            | 업데이트 될 때마다 1씩 증가                                  |
| STEP_NAME          | Step을 구성할 때 부여하는 Step 이름                          |
| JOB_EXECUTION_ID   | BATCH_JOB_EXECUTION 테이블의 외래키. JobExecution과는 일대다 관계 |
| START_TIME         | 실행이 시작된 시간을 나타내는 타임스탬프                     |
| END_TIME           | 성공, 실패에 상관없이 완료된 시간을 나타내는 타임스탬프      |
| STATUS             | 실행 상태(BatchStatus)를 저장(COMPLETED, STARTED...). BatchStatus의 Enumeration |
| COMMIT_COUNT       | 트랜잭션 당 커밋되는 수를 기록                               |
| READ_COUNT         | 실행 시점에 Read한 item 수를 기록                            |
| FILTER_COUNT       | 실행 도중 필터링 된 item 수를 기록                           |
| WRITE_COUNT        | 실팽 도중 저장되고 커밋된 item 수를 기록                     |
| READ_SKIP_COUNT    | 실행 도중 Read가 Skip된 item 수를 기록                       |
| WRITE_SKIP_COUNT   | 실행 도중 Write가 Skip된 item 수를 기록                      |
| PROCESS_SKIP_COUNT | 실행 도중 Prcess가 Skip된 itme 수를 기록                     |
| ROLLBACK_COUNT     | 실행 도중 Rollback이 일어난 수를 기록                        |
| EXIT_CODE          | 실행 종료(ExitStatus) 코드를 저장(COMPLETED, STARTED...)     |
| EXIT_MESSAGE       | Step이 종료된 방법에 대한 설명을 나타냄. Status가 실패일 경우 실패 원인 등의 내용을 저장 |
| LAST_UPDATED       | 마지막 실행(Execution) 시점을 타임스탬프 형식으로 기록       |

 

#### BATCH_STEP_EXECUTION_CONTEXT

\- Step의 실행 동안 여러 가지 **상태 정보, 공유 데이터를 직렬화(Json 형식) 해서 저장**합니다.

\- Step 별로 저장되며 **Step 간 서로 데이터 공유가 불가능**합니다.

| Column Name        | Column Explanation                                           |
| ------------------ | ------------------------------------------------------------ |
| STEP_EXECUTION_ID  | StepExecution 식별 키, STEP_EXECUTION 마다 각 생성           |
| SHORT_CONTEXT      | STEP의 실행 상태 정보, 공유 데이터 등의 정보를 문자열로 저장 |
| SERIALIZED_CONTEXT | 직렬화된 전체 컨텍스트                                       |



---



## BatchAutoConfiguration

스프링 배치가 초기화될 때 자동으로 실행되는 설정 클래스

Job을 수행하는 JobLauncherApplicationRunner 빈을 생성

스프링 부트가 해당 JobLauncherApplicationRunner클래스가 Job을 수행시켜 준다.

 

\- SimpleBatchConfiguration

JobBuilderFactory와 StepBuildFactory 생성

스프링 배치의 주요 구성 요소가 생성되는데 해당 구성 요소는 프락시 객체로 생성

 

\- BatchConfigurerConfiguration

  \- BasicBatchConfigurer

​     \- SimpleBatchConfiguration에서 생성한 프록시 객체의 실제 대상 객체를 생성하는 설정 클래스

​     \- 빈으로 의존성 주입받아서 주요 객체들을 참조해서 사용할 수 있다.

  \- JpaBatchConfigurer

​     \- JPA 관련 객체를 생성하는 설정 클래스

 

이 해당 클래스가 실행되는 순서를 간략하게 나타내면

@EnableBatchProcessing -> SimpleBatchConfiguration -> BatchConfigurerConfiguration -> BatchAutoConfiguration

이렇게 순서가 진행되데 실제 @EnableBatchProcessing 내부를 들여다보거나 디버깅 시에 해당 순서로 실행되는 모습을 볼 수 있습니다.





## Job 설정

TODO



#### JobParameter(왜 시스템파라미터가아닌지)  / Scope

Spring Batch의 경우 외부 혹은 내부에서 파라미터를 받아 여러 Batch 컴포넌트에서 사용할 수 있게 지원하고 있습니다.
이 파라미터를 **Job Parameter**라고 합니다.
Job Parameter를 사용하기 위해선 항상 Spring Batch 전용 Scope를 선언해야만 하는데요.
크게 `@StepScope`와 `@JobScope` 2가지가 있습니다.
사용법은 간단한데, 아래와 같이 SpEL로 선언해서 사용하시면 됩니다.

```java
@Value("#{jobParameters[파라미터명]}")
```

> `jobParameters` 외에도 `jobExecutionContext`, `stepExecutionContext` 등도 SpEL로 사용할 수 있습니다.
> @JobScope에선 `stepExecutionContext`는 사용할 수 없고, `jobParameters`와 `jobExecutionContext`만 사용할 수 있습니다.

각각의 Scope에서 사용하는 샘플 코드는 아래와 같습니다.





#### [@StepScope](https://github.com/StepScope) & [@JobScope](https://github.com/JobScope) 소개

Spring Batch는 `@StepScope`와 `@JobScope` 라는 아주 특별한 Bean Scope를 지원합니다.
아시다시피, **Spring Bean의 기본 Scope는 singleton**인데요.
그러나 아래처럼 Spring Batch 컴포넌트 (Tasklet, ItemReader, ItemWriter, ItemProcessor 등)에 `@StepScope`를 사용하게 되면

![stepscope1](D:\springbatch\Spring Batch 가이드(Markdown)\images\99A2F9475B7607502D)

Spring Batch가 Spring 컨테이너를 통해 지정된 **Step의 실행시점에 해당 컴포넌트를 Spring Bean으로 생성**합니다.
마찬가지로 `@JobScope`는 **Job 실행시점**에 Bean이 생성 됩니다.
즉, **Bean의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연**시킵니다.

> 어떻게 보면 MVC의 request scope와 비슷할 수 있겠습니다.
> request scope가 request가 왔을때 생성되고, response를 반환하면 삭제되는것처럼, JobScope, StepScope 역시 Job이 실행되고 끝날때, Step이 실행되고 끝날때 생성/삭제가 이루어진다고 보시면 됩니다.

이렇게 Bean의 생성시점을 어플리케이션 실행 시점이 아닌, Step 혹은 Job의 실행시점으로 지연시키면서 얻는 장점은 크게 2가지가 있습니다.

첫째로, **JobParameter의 Late Binding**이 가능합니다.
Job Parameter가 StepContext 또는 JobExecutionContext 레벨에서 할당시킬 수 있습니다.
꼭 Application이 실행되는 시점이 아니더라도 Controller나 Service와 같은 **비지니스 로직 처리 단계에서 Job Parameter를 할당**시킬 수 있습니다.
이 부분은 아래에서 좀 더 자세하게 예제와 함께 설명드리겠습니다.

두번째로, 동일한 컴포넌트를 병렬 혹은 동시에 사용할때 유용합니다.
Step 안에 Tasklet이 있고, 이 Tasklet은 멤버 변수와 이 멤버 변수를 변경하는 로직이 있다고 가정해봅시다.
이 경우 `@StepScope` 없이 Step을 병렬로 실행시키게 되면 **서로 다른 Step에서 하나의 Tasklet을 두고 마구잡이로 상태를 변경**하려고 할것입니다.
하지만 `@StepScope`가 있다면 **각각의 Step에서 별도의 Tasklet을 생성하고 관리하기 때문에 서로의 상태를 침범할 일이 없습니다**.



#### Job Parameter 오해

Job Parameters는 `@Value`를 통해서 가능합니다.
그러다보니 여러가지 오해가 있을 수 있는데요.
Job Parameters는 Step이나, Tasklet, Reader 등 Batch 컴포넌트 Bean의 생성 시점에 호출할 수 있습니다만, 정확히는 **Scope Bean을 생성할때만 가능**합니다.
즉, **`@StepScope`, `@JobScope` Bean을 생성할때만 Job Parameters가 생성**되기 때문에 사용할 수 있습니다.

예를 들어 아래와 같이 메소드를 통해 Bean을 생성하지 않고, 클래스에서 직접 Bean 생성을 해보겠습니다.
Job과 Step의 코드에서 `@Bean`과 `@Value("#{jobParameters[파라미터명]}")`를 **제거**하고 `SimpleJobTasklet`을 생성자 DI로 받도록 합니다.

> `@Autowired`를 쓰셔도 됩니다.

![jobparameter1](D:\springbatch\Spring Batch 가이드(Markdown)\images\9994B94E5B76075129)

그리고 `SimpleJobTasklet`은 아래와 같이 `@Component`와 `@StepScope`로 **Scope가 Step인 Bean**으로 생성합니다.
이 상태에서 `@Value("#{jobParameters[파라미터명]}`를 Tasklet의 멤버변수로 할당합니다.

![jobparameter2](D:\springbatch\Spring Batch 가이드(Markdown)\images\99C0984A5B76075126)

이렇게 **메소드의 파라미터로 JobParameter를 할당받지 않고, 클래스의 멤버 변수로 JobParameter를 할당** 받도록 해도 실행해보시면!

![jobparameter3](D:\springbatch\Spring Batch 가이드(Markdown)\images\994B8F365B76075126)

정상적으로 JobParameter를 받아 사용할 수 있습니다.
이는 **SimpleJobTasklet Bean이 `@StepScope`로 생성**되었기 때문입니다.

반면에, 이 SimpleJobTasklet Bean을 일반 singleton Bean으로 생성할 경우 아래와 같이 `'jobParameters' cannot be found` 에러가 발생합니다.

![jobparameter4](D:\springbatch\Spring Batch 가이드(Markdown)\images\99B8343A5B76075127)

즉, Bean을 메소드나 클래스 어느 것을 통해서 생성해도 무방하나 Bean의 Scope는 Step이나 Job이어야 한다는 것을 알 수 있습니다.

JobParameters를 사용하기 위해선 꼭 **`@StepScope`, `@JobScope`로 Bean을 생성**해야한다는 것을 잊지마세요.



#### JobParameter vs 시스템 변수

앞의 이야기를 보면서 아마 이런 의문이 있을 수 있습니다.

- 왜 꼭 Job Parameter를 써야하지?
- 기존에 Spring Boot에서 사용하던 여러 환경변수 혹은 시스템 변수를 사용하면 되지 않나?
- CommandLineRunner를 사용한다면 `java jar application.jar -D파라미터`로 시스템 변수를 지정하면 되지 않나?

자 그래서 왜 Job Parameter를 써야하는지 설명드리겠습니다.
아래 2가지 코드를 한번 보겠습니다.



#### JobParameter

```java
@Bean
@StepScope
public FlatFileItemReader<Partner> reader(
        @Value("#{jobParameters[pathToFile]}") String pathToFile){
    FlatFileItemReader<Partner> itemReader = new FlatFileItemReader<Partner>();
    itemReader.setLineMapper(lineMapper());
    itemReader.setResource(new ClassPathResource(pathToFile));
    return itemReader;
}
```



#### 시스템 변수

> 여기에서 얘기하는 시스템 변수는 application.properties와 `-D` 옵션으로 실행하는 변수까지 포함합니다.

```java
@Bean
@ConfigurationProperties(prefix = "my.prefix")
protected class JobProperties {

    String pathToFile;

    ...getters/setters
}

@Autowired
private JobProperties jobProperties;

@Bean
public FlatFileItemReader<Partner> reader() {
    FlatFileItemReader<Partner> itemReader = new FlatFileItemReader<Partner>();
    itemReader.setLineMapper(lineMapper());
    String pathToFile = jobProperties.getPathToFile();
    itemReader.setResource(new ClassPathResource(pathToFile));
    return itemReader;
}
```

위 2가지 방식에는 몇 가지 차이점이 있습니다.

일단 첫번째로, 시스템 변수를 사용할 경우 **Spring Batch의 Job Parameter 관련 기능을 못쓰게** 됩니다.
예를 들어, Spring Batch는 **같은 JobParameter로 같은 Job을 두 번 실행하지 않습니다**.
하지만 시스템 변수를 사용하게 될 경우 이 기능이 전혀 작동하지 않습니다.
또한 Spring Batch에서 자동으로 관리해주는 Parameter 관련 메타 테이블이 전혀 관리되지 않습니다.

둘째, Command line이 아닌 다른 방법으로 Job을 실행하기가 어렵습니다.
만약 실행해야한다면 **전역 상태 (시스템 변수 혹은 환경 변수)를 동적으로 계속해서 변경시킬 수 있도록** Spring Batch를 구성해야합니다.
동시에 여러 Job을 실행하려는 경우 또는 테스트 코드로 Job을 실행해야할때 문제가 발생할 수 있습니다.

특히 Job Parameter를 못쓰는 점은 큰 단점인데요.
Job Parameter를 못쓴다는 것은 위에서도 언급한 **Late Binding을 못하게 된다**는 의미입니다.

예를 들어 웹 서버가 있고, 이 웹서버에서 Batch를 수행한다고 가정해봅시다.
외부에서 넘겨주는 파라미터에 따라 Batch가 다르게 작동해야한다면, 이를 시스템 변수로 풀어내는 것은 너무나 어렵습니다.
하지만 아래와 같이 Job Parameter를 이용한다면 아주 쉽게 해결할 수 있습니다.



## Step설정

### Next / Flow / Decider

TODO



### Tasklet 

TODO





### Chunk

Spring Batch에서의 Chunk란 데이터 덩어리로 작업 할 때 **각 커밋 사이에 처리되는 row 수**를 얘기합니다.
즉, Chunk 지향 처리란 **한 번에 하나씩 데이터를 읽어 Chunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션**을 다루는 것을 의미합니다.

여기서 트랜잭션이라는게 중요한데요.
Chunk 단위로 트랜잭션을 수행하기 때문에 **실패할 경우엔 해당 Chunk 만큼만 롤백**이 되고, 이전에 커밋된 트랜잭션 범위까지는 반영이 된다는 것입니다.

Chunk 지향 처리가 결국 Chunk 단위로 데이터를 처리한다는 의미이기 때문에 그림으로 표현하면 아래와 같습니다.

![chunk-process](D:\springbatch\Spring Batch 가이드(Markdown)\images\999A513E5B814C4A12.png)

> [공식 문서의 그림](https://docs.spring.io/spring-batch/4.0.x/reference/html/index-single.html#chunkOrientedProcessing)은 **개별 item이 처리되는 것만** 다루고 있습니다.
> 위 그림은 Chunk 단위까지 다루고 있어 조금 다르니 주의해주세요.

- Reader에서 데이터를 하나 읽어옵니다
- 읽어온 데이터를 Processor에서 가공합니다
- 가공된 데이터들을 별도의 공간에 모은 뒤, Chunk 단위만큼 쌓이게 되면 Writer에 전달하고 Writer는 일괄 저장합니다.

**Reader와 Processor에서는 1건씩 다뤄지고, Writer에선 Chunk 단위로 처리**된다는 것만 기억하시면 됩니다.

Chunk 지향 처리를 Java 코드로 표현하면 아래처럼 될 것 같습니다.

```java
for(int i=0; i<totalSize; i+=chunkSize){ // chunkSize 단위로 묶어서 처리
    List items = new Arraylist();
    for(int j = 0; j < chunkSize; j++){
        Object item = itemReader.read()
        Object processedItem = itemProcessor.process(item);
        items.add(processedItem);
    }
    itemWriter.write(items);
}
```

**chunkSize별로 묶어서 처리**된다는 의미를 도우기위해 Spring Batch 내부 코드를 보면서 알아보겠습니다.



### Page Size vs Chunk Size

기존에 Spring Batch를 사용해보신 분들은 아마 PagingItemReader를 많이들 사용해보셨을 것입니다.
PagingItemReader를 사용하신 분들 중 간혹 Page Size와 Chunk Size를 같은 의미로 오해하시는 분들이 계시는데요.
**Page Size와 Chunk Size는 서로 의미하는 바가 다릅니다**.

**Chunk Size는 한번에 처리될 트랜잭션 단위**를 얘기하며, **Page Size는 한번에 조회할 Item의 양**을 얘기합니다.

자 그럼 이제 2개가 어떻게 다른지 실제 Spring Batch의 ItemReader 코드를 직접 들여다보겠습니다.

PagingItemReader의 부모 클래스인 `AbstractItemCountingItemStreamItemReader`의 `read()` 메소드를 먼저 보겠습니다.

![read1](D:\springbatch\Spring Batch 가이드(Markdown)\images\99CEA6335B814C4C23.png)

보시는것처럼 읽어올 데이터가 있다면 `doRead()`를 호출합니다.

`doRead()`의 코드는 아래와 같습니다.

![read2](D:\springbatch\Spring Batch 가이드(Markdown)\images\99A4433F5B814C4D0E.png)

`doRead()`에서는 현재 읽어올 데이터가 없거나, Page Size를 초과한 경우 `doReadPage()`를 호출합니다.
읽어올 데이터가 없는 경우는 read가 처음 시작할 때를 얘기합니다.
Page Size를 초과하는 경우는 예를 들면 Page Size가 10인데, 이번에 읽어야할 데이터가 11번째 데이터인 경우입니다.
이럴 경우 Page Size를 초과했기 때문에 `doReadPage()` 를 호출한다고 보시면 됩니다.

즉, **Page 단위로 끊어서 조회**하는 것입니다.

> 게시판 만들기에서 페이징 조회를 떠올려보시면 쉽게 이해가 되실것 같습니다.

`doReadPage()`부터는 하위 구현 클래스에서 각자만의 방식으로 페이징 쿼리를 생성합니다.
여기서는 보편적으로 많이 사용하시는 **JpaPagingItemReader** 코드를 살펴보겠습니다.

JpaPagingItemReader의 `doReadPage()`의 코드는 아래와 같습니다.

![read3](D:\springbatch\Spring Batch 가이드(Markdown)\images\99999E405B814C4D24.png)

Reader에서 지정한 Page Size만큼 `offset`, `limit` 값을 지정하여 페이징 쿼리를 생성 (`createQuery()`)하고, 사용 (`query.getResultList()`) 합니다.
쿼리 실행 결과는 `results`에 저장합니다.
이렇게 저장된 `results`에서 `read()` 가 **호출될때마다 하나씩 꺼내서 전달**합니다.

즉, Page Size는 **페이징 쿼리에서 Page의 Size를 지정하기 위한 값**입니다.

만약 2개 값이 다르면 어떻게 될까요?
PageSize가 10이고, ChunkSize가 50이라면 **ItemReader에서 Page 조회가 5번 일어나면 1번 의 트랜잭션이 발생하여 Chunk가 처리**됩니다.

한번의 트랜잭션 처리를 위해 5번의 쿼리 조회가 발생하기 때문에 성능상 이슈가 발생할 수 있습니다. 그래서 Spring Batch의 PagingItemReader에는 클래스 상단에 다음과 같은 주석을 남겨두었습니다.

> Setting a fairly large page size and using a commit interval that matches the page size should provide better performance.
> (상당히 큰 페이지 크기를 설정하고 페이지 크기와 일치하는 커미트 간격을 사용하면 성능이 향상됩니다.)

성능상 이슈 외에도 2개 값을 다르게 할 경우 JPA를 사용하신다면 영속성 컨텍스트가 깨지는 문제도 발생합니다.
(이전에 관련해서 [문제를 정리](http://jojoldu.tistory.com/146)했으니 참고해보세요)

2개 값이 의미하는 바가 다르지만 위에서 언급한 여러 이슈로 **2개 값을 일치시키는 것이 보편적으로 좋은 방법**이니 꼭 2개 값을 일치시키시길 추천드립니다.







## Reader & Processor & Writer 설정

TODO

File,DB(JDBC,JPA),Redis,Kafka,MongoDB



## Multi-Thread & Listener

TODO



## Retry & Tolerant

TODO





















# 별첨



## *Spring Batch Application 전개 전략

온라인, 온라인배치, 일반배치 그림추가



## *Jib 활용 Containerizing

Docker 실행.



## *Batch vs Quartz?

간혹 Spring Batch와 Spring Quartz를 비교하는 글을 보게 되는데요.
둘은 역할이 완전히 다릅니다.
**Quartz는 스케줄러의 역할**이지, Batch 와 같이 **대용량 데이터 배치 처리에 대한 기능을 지원하지 않습니다**.
반대로 Batch 역시 Quartz의 다양한 스케줄 기능을 지원하지 않아서 보통은 Quartz + Batch 또는 Batch Application 에 다양한 Scheduler를 조합해서 사용합니다.
정해진 스케줄마다 Quartz가 Spring Batch를 실행하는 구조라고 보시면 됩니다.



## *ExitCodeGenerator

애플리케이션이 정상적으로 종료되는 것은 꽤 중요하다. 애플리케이션의 갑작스런 죽음과는 달리 특정 상황이 발생했고 그 상황에 맞춰 종료코드를 반환하고 죽는다면 그에 대한 대응하기가 수월해진다. 스프링 부트에서 장애대응을 수월하게 할 수 있도록 특정상황에 따른 종료코드(Exit Code)를 반환하며 애플리케이션을 정상종료 시키는 기능을 제공한다.

스프링 부트 참고문서에 나온 예제는 다음과 같다.

```
@SpringBootApplication
public class ExitCodeApplication {
	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 42;
	}
	public static void main(String[] args) {
		System.exit(SpringApplication
				.exit(SpringApplication.run(ExitCodeApplication.class, args)));
	}
}
```

이와 관련된 것은 아래 두 가지다.

- `ExitCodeGenerator`
- `SpringApplication.exit(ApplicationContext context, ExitCodeGenerator… exitCodeGenerators)`

위 코드는 대략 다음과 같다.

```
ExitCodeGenerator
@FunctionalInterface
public interface ExitCodeGenerator {
    /**
     * Returns the exit code that should be returned from the application.
     * @return the exit code.
     */
    int getExitCode();
}
```

`SpringApplication.exit()` 메서드

```
public static int exit(ApplicationContext context,
        ExitCodeGenerator... exitCodeGenerators) {
    Assert.notNull(context, "Context must not be null");
    int exitCode = 0;
    try {
        try {
            ExitCodeGenerators generators = new ExitCodeGenerators();
            Collection<ExitCodeGenerator> beans = context
                    .getBeansOfType(ExitCodeGenerator.class).values();
            generators.addAll(exitCodeGenerators);
            generators.addAll(beans);
            exitCode = generators.getExitCode();
            if (exitCode != 0) {  // (1)
                context.publishEvent(new ExitCodeEvent(context, exitCode));
            }
        }
        finally {
            close(context);
        }
    }
    catch (Exception ex) {
        ex.printStackTrace();
        exitCode = (exitCode == 0 ? 1 : exitCode);
    }
    return exitCode;
}
```

`exitCode != 0` 조건이 만족하면 `ExitCodeEvent` 이벤트가 발생한다.

`ExitCodeGenerator` 인터페이스를 `SpringApplication.exit()` 전달하면 `SpringApplication.exit()`는 `getExitCode()` 메서드를 호출하여 종료코드를 받는다. 이 때 이 종료코드가 0인 경우에는 일반적인 애플리케이션 종료를 진행한다.

**`0`이 아닌 경우에는 종료코드 발송 이벤트(`ExitCodeEvent`)가 발생한 후 애플리케이션이 종료된다.**



애플리케이션에서 특정 예외발생 시 다음과 같은 형식으로 종료코드 반환 이벤트를 발생시키며 종료시킬 수 있겠다.

```
@Autowired
ExitCodeGenerator exitCodeGenerator;

try {
	// 예외가 발생할 수 있는 상황
} catch (WannabeException e) {
    // 특정조건의 예외가 발생하면 애플리케이션을 안전하게 종료시킬 수도 있겠다.
    //
    SpringApplication.exit(applicationContext, exitCodeGenerator);
}
```

`WannabeException`에 대한 예외처리를 할 수 있는 `@ControllerAdvice` 클래스를 정의해서 `WannabeException`가 발생하면 특정코드를 발생하며 종료되도록 하는 것도 가능하겠다.

```
@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler(WannabeException.class)
    public void handleConflict(WannabeException we) {
        // todo we
        SpringApplication.exit(applicationContext, () -> 810301);
    }
}
```

참고 URL : https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.spring-application.application-exit





## *spring batch 5 적용

Spring Boot 3(=Spring Framework 6)부터 `Spring Batch 5` 버전을 사용하게 업데이트 되었다.
Batch 5에 변경점이 많이 생겨 기존의 4버전과 다른 부분이 많이 생겼다.

1. @EnableBatchProcessing

2. JobBuilderFactory, StepBuilderFactory deprecated

3. JobRepository, TransactionManager 명시적으로 변경



### 주요 변경 사항

### JDK 17 기준선

Spring Batch 5는 최소 버전으로 Java 17이 필요한 Spring Framework 6을 기반으로 합니다. 따라서 Spring Batch 5 애플리케이션을 실행하려면 Java 17+를 사용해야 합니다.

### 종속성 업그레이드

Spring Batch 5는 전반적으로 Spring 종속성을 다음 버전으로 업데이트합니다.

- 스프링 프레임워크 6
- 스프링 통합 6
- 스프링 데이터 3
- 봄 AMQP 3
- Apache Kafka 3용 스프링
- 마이크로미터 1.10

또한 이 버전은 다음으로의 마이그레이션을 표시합니다.

- Jakarta EE 9: 사용하는 모든 EE API에 대해 가져오기 명령문을 에서 `javax.*`로 업데이트하세요 .`jakarta.*`
- Hibernate 6: Hibernate(커서/페이징) 항목 판독기와 기록기가 Hibernate 6.1 API를 사용하도록 업데이트되었습니다(이전에는 Hibernate 5.6 API 사용).

그 외에도:

- `org.springframework:spring-jdbc`이제 필수 종속 항목입니다.`spring-batch-core`
- `junit:junit`는 더 이상 필수 종속성이 아닙니다 `spring-batch-test`.
- `com.fasterxml.jackson.core:jackson-core`이제 선택 사항입니다`spring-batch-core`



### 데이터베이스 스키마 업데이트

#### 신탁

이 버전에서는 이제 Oracle 시퀀스가 주문됩니다. 새로운 애플리케이션을 위해 시퀀스 생성 스크립트가 업데이트되었습니다. 기존 애플리케이션은 의 마이그레이션 스크립트를 사용하여 `org/springframework/batch/core/migration/5.0/migration-oracle.sql`기존 시퀀스를 변경할 수 있습니다.

또한 Oracle용 DDL 스크립트의 이름이 다음과 같이 변경되었습니다.

- `org/springframework/batch/core/schema-drop-oracle10g.sql`이름이 다음으로 변경되었습니다.`org/springframework/batch/core/schema-drop-oracle.sql`
- `org/springframework/batch/core/schema-oracle10g.sql`이름이 다음으로 변경되었습니다.`org/springframework/batch/core/schema-oracle.sql`

#### MS SQL서버

v4까지 MS SQLServer용 DDL 스크립트는 테이블을 사용하여 시퀀스를 에뮬레이트했습니다. 이 버전에서는 이 사용법이 실제 시퀀스로 업데이트되었습니다.

```
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CACHE NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CACHE NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NO CACHE NO CYCLE;
```



새로운 애플리케이션은 수정 없이 제공된 스크립트를 사용할 수 있습니다. 기존 애플리케이션은 v4에서 사용되는 시퀀스 테이블의 마지막 값에서 시퀀스를 시작하도록 위의 코드 조각을 수정하는 것을 고려해야 합니다.

#### 모든 플랫폼

##### `BATCH_JOB_EXECUTION#JOB_CONFIGURATION_LOCATION`컬럼 제거

`JOB_CONFIGURATION_LOCATION`테이블 의 열은 `BATCH_JOB_EXECUTION`더 이상 사용되지 않으며 필요한 경우 사용되지 않은 것으로 표시하거나 삭제할 수 있습니다.

```
ALTER TABLE BATCH_JOB_EXECUTION DROP COLUMN JOB_CONFIGURATION_LOCATION;
```



컬럼 삭제 구문은 데이터베이스 서버 버전에 따라 다를 수 있으므로 컬럼 삭제 구문을 확인하시기 바랍니다. 일부 플랫폼에서는 이 변경으로 인해 테이블 재구성이 필요할 수 있습니다.

❗ 중요 사항 ❗ 이 변경 사항은 주로 이 칼럼을 사용하는 프레임워크의 유일한 부분인 JSR-352 구현 제거와 관련이 있습니다. 결과적으로 해당 필드 `JobExecution#jobConfigurationName`와 이를 사용하는 모든 API(도메인 객체의 생성자 및 getter , 의 `JobExecution`메서드 )가 제거되었습니다.`JobRepository#createJobExecution(JobInstance, JobParameters, String);``JobRepository`

##### 열 변경`BATCH_JOB_EXECUTION_PARAMS`

가 `BATCH_JOB_EXECUTION_PARAMS`다음과 같이 업데이트되었습니다:

```
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
---	TYPE_CD VARCHAR(6) NOT NULL ,
---	KEY_NAME VARCHAR(100) NOT NULL ,
---	STRING_VAL VARCHAR(250) ,
---	DATE_VAL DATETIME(6) DEFAULT NULL ,
---	LONG_VAL BIGINT ,
---	DOUBLE_VAL DOUBLE PRECISION ,
+++	PARAMETER_NAME VARCHAR(100) NOT NULL ,
+++	PARAMETER_TYPE VARCHAR(100) NOT NULL ,
+++	PARAMETER_VALUE VARCHAR(2500) ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);
```



[이는 https://github.com/spring-projects/spring-batch/issues/3960](https://github.com/spring-projects/spring-batch/issues/3960) 에서 다시 설명한 대로 작업 매개변수가 유지되는 방식과 관련이 있습니다 . 마이그레이션 스크립트는 에서 찾을 수 있습니다 `org/springframework/batch/core/migration/5.0`.

##### 열 변경`BATCH_STEP_EXECUTION`

v5에는 새로운 열이 `CREATE_TIME`추가되었습니다. 데이터베이스 서버에 따라 테이블에 다음과 같이 생성해야 합니다.

```
ALTER TABLE BATCH_STEP_EXECUTION ADD CREATE_TIME TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';
```



또한 `NOT NULL`제약 조건이 열에서 삭제되었습니다 `START_TIME`.

```
ALTER TABLE BATCH_STEP_EXECUTION ALTER COLUMN START_TIME DROP NOT NULL;
```



### 인프라 Bean 구성`@EnableBatchProcessing`

#### 작업 저장소/탐색기 구성 업데이트

맵 기반 작업 저장소/탐색기 구현은 v4에서 더 이상 사용되지 않으며 v5에서 완전히 제거되었습니다. 대신 Jdbc 기반 구현을 사용해야 합니다. 사용자 정의 작업 저장소/탐색기 구현을 사용하지 않는 한 주석은 애플리케이션 컨텍스트에서 Bean이 필요한 `@EnableBatchProcessing`Jdbc 기반을 구성합니다 . Bean 은 H2, HSQL 등과 같은 내장형 데이터베이스를 참조하여 메모리 내 작업 저장소와 작동할 수 있습니다.`JobRepository``DataSource``DataSource`

#### 트랜잭션 관리자 빈 노출/구성

버전 4.3까지 `@EnableBatchProcessing`주석은 애플리케이션 컨텍스트에서 트랜잭션 관리자 Bean을 노출했습니다. 이는 많은 경우에 편리했지만 트랜잭션 관리자가 무조건적으로 노출되면 사용자 정의 트랜잭션 관리자를 방해할 수 있습니다. 이번 릴리스에서는 `@EnableBatchProcessing`더 이상 애플리케이션 컨텍스트에 트랜잭션 관리자 Bean을 노출하지 않습니다. [이 변경 사항은 https://github.com/spring-projects/spring-batch/issues/816](https://github.com/spring-projects/spring-batch/issues/816) 문제와 관련이 있습니다 .

[앞서 언급한 문제와 https://github.com/spring-projects/spring-batch/issues/4130](https://github.com/spring-projects/spring-batch/issues/4130) 에서 수정된 트랜잭션 관리자에 관한 XML 및 Java 구성 스타일 간의 불일치 로 인해 이제 태스크릿 단계 정의에서 트랜잭션 관리자를 수동으로 구성해야 합니다. 방법이 `StepBuilderHelper#transactionManager(PlatformTransactionManager)`한 단계 아래인 `AbstractTaskletStepBuilder`.

이와 관련하여 v4에서 v5로의 일반적인 마이그레이션 경로는 다음과 같습니다.

```
// Sample with v4
@Configuration
@EnableBatchProcessing
public class MyStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep() {
        return this.stepBuilderFactory.get("myStep")
                .tasklet(..) // or .chunk()
                .build();
    }

}
```



```
// Sample with v5
@Configuration
@EnableBatchProcessing
public class MyStepConfig {

    @Bean
    public Tasklet myTasklet() {
       return new MyTasklet();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep", jobRepository)
                .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

}
```



이는 태스크릿 단계에만 필요하며, 다른 단계 유형에는 설계상 트랜잭션 관리자가 필요하지 않습니다.

또한 트랜잭션 관리자는 `BatchConfigurer#getTransactionManager`. 트랜잭션 관리자는 의 구현 세부 사항이므로 `JobRepository`와 동일한 수준 `JobRepository`(예: 동일한 인터페이스)에서 구성할 수 없어야 합니다. 이번 릴리스에서는 `BatchConfigurer`인터페이스가 제거되었습니다. 필요한 경우 사용자 정의 트랜잭션 관리자는 의 속성으로 선언적으로 제공되거나 `@EnableBatchProcessing`를 재정의하여 프로그래밍 방식으로 제공될 수 있습니다 `DefaultBatchConfiguration#getTransactionManager()`. [이 변경 사항에 대한 자세한 내용은 https://github.com/spring-projects/spring-batch/issues/3942를](https://github.com/spring-projects/spring-batch/issues/3942) 확인하세요 .

#### JobBuilderFactory 및 StepBuilderFactory 빈 노출/구성

`JobBuilderFactory`더 이상 애플리케이션 컨텍스트에서 빈으로 노출되지 않으며 `StepBuilderFactory`이제 생성한 각 빌더를 사용하기 위해 v5.2에서 제거되지 않습니다.

이와 관련하여 v4에서 v5로의 일반적인 마이그레이션 경로는 다음과 같습니다.

```
// Sample with v4
@Configuration
@EnableBatchProcessing
public class MyJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job myJob(Step step) {
        return this.jobBuilderFactory.get("myJob")
                .start(step)
                .build();
    }

}
```



```
// Sample with v5
@Configuration
@EnableBatchProcessing
public class MyJobConfig {

    @Bean
    public Job myJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("myJob", jobRepository)
                .start(step)
                .build();
    }

}
```



동일한 패턴을 사용하여 더 이상 사용되지 않는 의 사용을 제거할 수 있습니다 `StepBuilderFactory`. [이 변경 사항에 대한 자세한 내용은 https://github.com/spring-projects/spring-batch/issues/4188을](https://github.com/spring-projects/spring-batch/issues/4188) 확인하세요 .

### 데이터 유형 업데이트

- 및 의 메트릭 카운터( `readCount`, `writeCount`등)가 에서 로 변경되었습니다 . 모든 getter 및 setter가 그에 따라 업데이트되었습니다.`org.springframework.batch.core.StepExecution``org.springframework.batch.core.StepContribution``int``long`
- `skipCount`의 매개변수 가 에서 로 `org.springframework.batch.core.step.skip.SkipPolicy#shouldSkip`변경되었습니다 . 이는 이전 요점과 관련이 있습니다.`int``long`
- `startTime`, `endTime`및 `createTime`의 필드 유형이 에서 `lastUpdated`로 `JobExecution`변경 `StepExecution`되었습니다 .`java.util.Date``java.time.LocalDateTime`

### 관측 가능성 업데이트

- 마이크로미터가 1.10 버전으로 업데이트되었습니다.
- 이제 모든 태그 앞에 미터 이름이 붙습니다. 예를 들어, 타이머의 태그 `spring.batch.job`이름 은 버전 4.x입니다 `name`. `status`버전 5에서는 해당 태그의 이름이 각각 `spring.batch.job.name`및 로 지정됩니다 `spring.batch.job.status`.
- 클래스 `BatchMetrics`(내부 전용)가 패키지 `org.springframework.batch.core.metrics`로 이동되었습니다 `org.springframework.batch.core.observability`.

### 실행 컨텍스트 직렬화 업데이트

v5부터 기본값이 에서 으로 `ExecutionContextSerializer`변경되었습니다 . Base64에서 컨텍스트를 직렬화/역직렬화하도록 기본 실행 컨텍스트 직렬 변환기가 업데이트되었습니다.`JacksonExecutionContextStringSerializer``DefaultExecutionContextSerializer`

Jackson에 대한 의존성은 선택 사항이 되었습니다. `JacksonExecutionContextStringSerializer`를 사용하려면 `jackson-core`클래스패스에 를 추가해야 합니다.

### SystemCommandTasklet 업데이트

이번 릴리스에서는 가 `SystemCommandTasklet`다시 검토되어 다음과 같이 변경되었습니다.

- `CommandRunner`태스크릿 실행에서 명령 실행을 분리하기 위해 명명된 새로운 전략 인터페이스가 도입되었습니다. 기본 구현은 API를 `JvmCommandRunner`사용하여 `java.lang.Runtime#exec`시스템 명령을 실행하는 것입니다. 이 인터페이스는 다른 API를 사용하여 시스템 명령을 실행하도록 구현될 수 있습니다.
- 이제 명령을 실행하는 메서드는 `String`명령과 해당 인수를 나타내는 배열을 허용합니다. 더 이상 명령을 토큰화하거나 사전 처리를 수행할 필요가 없습니다. 이러한 변경으로 인해 API가 더욱 직관적이고 오류 발생 가능성이 낮아졌습니다.

### 업데이트를 처리하는 작업 매개변수

#### 작업 매개변수로 모든 유형 지원

이번 변경으로 v4에서와 같이 미리 정의된 4가지 유형(long, double, string, date)뿐만 아니라 모든 유형을 작업 매개변수로 사용할 수 있는 지원이 추가되었습니다. 주요 변경 사항은 다음과 같습니다.

```
---public class JobParameter implements Serializable {
+++public class JobParameter<T> implements Serializable {

---   private Object parameter;
+++   private T value;

---   private ParameterType parameterType;
+++   private Class<T> type;

}
```



이번 개정판에서는 `JobParameter`모든 유형이 가능합니다. 이 변경으로 인해 의 반환 유형 변경 `getType()`, 열거형 제거 등 많은 API 변경이 필요했습니다 `ParameterType`. 이 업데이트와 관련된 모든 변경 사항은 "[사용되지 않음|이동|제거됨] API" 섹션에서 확인할 수 있습니다.

이 변경 사항은 작업 매개변수가 데이터베이스에서 유지되는 방식에 영향을 미쳤습니다(미리 정의된 각 유형에 대해 더 이상 4개의 고유 열이 없습니다). [DDL 변경 사항 은 BATCH_JOB_EXECUTION_PARAMS의 열 변경 사항을](https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide#column-change-in-batch_job_execution_params) 확인하세요 . 이제 매개변수 유형의 정규화된 이름이 `String`매개변수 값뿐만 아니라 로 유지됩니다. 문자열 리터럴은 표준 Spring 변환 서비스를 사용하여 매개변수 유형으로 변환됩니다. 표준 변환 서비스는 사용자 특정 유형을 문자열 리터럴로 변환하는 데 필요한 변환기로 강화될 수 있습니다.

#### 기본 작업 매개변수 변환

v4의 작업 매개변수 기본 표기법은 다음과 같이 지정되었습니다.

```
[+|-]parameterName(parameterType)=value
```



여기서는 `parameterType`[string,long,double,date] 중 하나입니다. 제한적이고 제한적인 것 외에도 이 표기법은 https://github.com/spring-projects/spring-batch/issues/3960 에 설명된 대로 여러 가지 문제를 야기했습니다 .

v5에는 작업 매개변수를 지정하는 두 가지 방법이 있습니다.

##### 기본 표기법

기본 표기법은 다음과 같이 지정됩니다.

```
parameterName=parameterValue,parameterType,identificationFlag
```



`parameterType`매개변수 유형의 완전한 이름은 어디에 있습니까? Spring Batch는 `DefaultJobParametersConverter`이 표기법을 지원하는 를 제공합니다.

##### 확장 표기법

기본 표기법은 대부분의 사용 사례에 적합하지만 예를 들어 값에 쉼표가 포함되어 있으면 불편할 수 있습니다. 이 경우 Spring Boot의 [Json 애플리케이션 속성](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.application-json) 에서 영감을 받아 다음과 같이 지정된 확장 표기법을 사용할 수 있습니다.

```
parameterName='{"value": "parameterValue", "type":"parameterType", "identifying": "booleanValue"}'
```



`parameterType`매개변수 유형의 완전한 이름은 어디에 있습니까? Spring Batch는 `JsonJobParametersConverter`이 표기법을 지원하는 를 제공합니다.

##### 기록 데이터 액세스 영향

작업 매개변수 처리에 대한 이러한 주요 변경으로 인해 배치 메타데이터를 탐색하도록 설계된 일부 API는 v4에서 시작된 작업 인스턴스에 사용되어서는 **안 됩니다.** 예를 들어:

- `JobExplorer#getJobInstances`v4와 v5 사이의 혼합 기록 데이터를 검색할 수 있으며 v4의 작업 매개변수를 로드할 때 실패할 수 있습니다( [#4352](https://github.com/spring-projects/spring-batch/issues/4352) ). 이 특별한 경우에는 v5로 실행된 첫 번째 인스턴스의 인덱스에서 시작해야 합니다.
- `JobExplorer#getJobExecution`전달된 작업 실행 ID가 v4로 실행된 실행 중 하나인 경우 작업 매개변수 검색에 실패할 수 있습니다.

이 변경이 작업 매개변수 로드에 영향을 미치는 또 다른 경우는 실패한 작업 인스턴스를 다시 시작할 때입니다. 실패한 모든 v4 작업 인스턴스는 성공을 위해 다시 시작되거나 v5로의 마이그레이션이 완료되기 *전에* 중단될 것으로 예상됩니다 .


https://europani.github.io/spring/2023/06/26/052-spring-batch-version5.html

참조

```
https://europani.github.io/spring/2023/06/26/052-spring-batch-version5.html
https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide
https://docs.spring.io/spring-batch/docs/current/reference/html/spring-batch-intro.html
https://docs.spring.io/spring-batch/docs/current/reference/html/spring-batch-intro.html
https://loosie.tistory.com/838
https://hesh1232.tistory.com/176
https://hesh1232.tistory.com/177
https://gist.github.com/ihoneymon/a792351ad901f33c31470aa4e4f74acb
https://docs.spring.io/spring-batch/docs/current/reference/html/index.html
https://godekdls.github.io/Spring%20Batch/contents/

scdf https://velog.io/@mnetna/Spring-Data-Flow-using-Kubernetes-%EC%84%A4%EC%B9%98%EC%99%80-Remote-Partition-%EC%A0%81%EC%9A%A9
```



example

```
https://github.com/erenavsarogullari/OTV_SpringBatch_Chunk_Oriented_Processing
https://www.baeldung.com/introduction-to-spring-batch
```





