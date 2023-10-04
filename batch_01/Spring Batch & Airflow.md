# 시작전에

STS 설치

git 설치

mobaxterm 설치

docker-desktop 설치

git clone example project





# spring batch 개요

## spring batch란?

## Spring Batch Application 전개 전략

온라인, 온라인배치, 일반배치

## spring batch 실습

#### Tasklet

#### Chunk

##### Reader/Processor/Writer

File,DB(JDBC,JPA),Redis,Kafka,MongoDB

Listener

Multi-Thread





#### Jib 활용 Containerizing

Docker 실행.



## *spring batch 5 적용

Spring Boot 3(=Spring Framework 6)부터 `Spring Batch 5` 버전을 사용하게 업데이트 되었다.
Batch 5에 변경점이 많이 생겨 기존의 4버전과 다른 부분이 많이 생겼다.

1. @EnableBatchProcessing

2. JobBuilderFactory, StepBuilderFactory deprecated

3. JobRepository, TransactionManager 명시적으로 변경



# 주요 변경 사항

## JDK 17 기준선

Spring Batch 5는 최소 버전으로 Java 17이 필요한 Spring Framework 6을 기반으로 합니다. 따라서 Spring Batch 5 애플리케이션을 실행하려면 Java 17+를 사용해야 합니다.

## 종속성 업그레이드

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

## 데이터베이스 스키마 업데이트

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



## 인프라 Bean 구성`@EnableBatchProcessing`

### 작업 저장소/탐색기 구성 업데이트

맵 기반 작업 저장소/탐색기 구현은 v4에서 더 이상 사용되지 않으며 v5에서 완전히 제거되었습니다. 대신 Jdbc 기반 구현을 사용해야 합니다. 사용자 정의 작업 저장소/탐색기 구현을 사용하지 않는 한 주석은 애플리케이션 컨텍스트에서 Bean이 필요한 `@EnableBatchProcessing`Jdbc 기반을 구성합니다 . Bean 은 H2, HSQL 등과 같은 내장형 데이터베이스를 참조하여 메모리 내 작업 저장소와 작동할 수 있습니다.`JobRepository``DataSource``DataSource`

### 트랜잭션 관리자 빈 노출/구성

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

### JobBuilderFactory 및 StepBuilderFactory 빈 노출/구성

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

## 데이터 유형 업데이트

- 및 의 메트릭 카운터( `readCount`, `writeCount`등)가 에서 로 변경되었습니다 . 모든 getter 및 setter가 그에 따라 업데이트되었습니다.`org.springframework.batch.core.StepExecution``org.springframework.batch.core.StepContribution``int``long`
- `skipCount`의 매개변수 가 에서 로 `org.springframework.batch.core.step.skip.SkipPolicy#shouldSkip`변경되었습니다 . 이는 이전 요점과 관련이 있습니다.`int``long`
- `startTime`, `endTime`및 `createTime`의 필드 유형이 에서 `lastUpdated`로 `JobExecution`변경 `StepExecution`되었습니다 .`java.util.Date``java.time.LocalDateTime`

## 관측 가능성 업데이트

- 마이크로미터가 1.10 버전으로 업데이트되었습니다.
- 이제 모든 태그 앞에 미터 이름이 붙습니다. 예를 들어, 타이머의 태그 `spring.batch.job`이름 은 버전 4.x입니다 `name`. `status`버전 5에서는 해당 태그의 이름이 각각 `spring.batch.job.name`및 로 지정됩니다 `spring.batch.job.status`.
- 클래스 `BatchMetrics`(내부 전용)가 패키지 `org.springframework.batch.core.metrics`로 이동되었습니다 `org.springframework.batch.core.observability`.

## 실행 컨텍스트 직렬화 업데이트

v5부터 기본값이 에서 으로 `ExecutionContextSerializer`변경되었습니다 . Base64에서 컨텍스트를 직렬화/역직렬화하도록 기본 실행 컨텍스트 직렬 변환기가 업데이트되었습니다.`JacksonExecutionContextStringSerializer``DefaultExecutionContextSerializer`

Jackson에 대한 의존성은 선택 사항이 되었습니다. `JacksonExecutionContextStringSerializer`를 사용하려면 `jackson-core`클래스패스에 를 추가해야 합니다.

## SystemCommandTasklet 업데이트

이번 릴리스에서는 가 `SystemCommandTasklet`다시 검토되어 다음과 같이 변경되었습니다.

- `CommandRunner`태스크릿 실행에서 명령 실행을 분리하기 위해 명명된 새로운 전략 인터페이스가 도입되었습니다. 기본 구현은 API를 `JvmCommandRunner`사용하여 `java.lang.Runtime#exec`시스템 명령을 실행하는 것입니다. 이 인터페이스는 다른 API를 사용하여 시스템 명령을 실행하도록 구현될 수 있습니다.
- 이제 명령을 실행하는 메서드는 `String`명령과 해당 인수를 나타내는 배열을 허용합니다. 더 이상 명령을 토큰화하거나 사전 처리를 수행할 필요가 없습니다. 이러한 변경으로 인해 API가 더욱 직관적이고 오류 발생 가능성이 낮아졌습니다.

## 업데이트를 처리하는 작업 매개변수

### 작업 매개변수로 모든 유형 지원

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

### 기본 작업 매개변수 변환

v4의 작업 매개변수 기본 표기법은 다음과 같이 지정되었습니다.

```
[+|-]parameterName(parameterType)=value
```



여기서는 `parameterType`[string,long,double,date] 중 하나입니다. 제한적이고 제한적인 것 외에도 이 표기법은 https://github.com/spring-projects/spring-batch/issues/3960 에 설명된 대로 여러 가지 문제를 야기했습니다 .

v5에는 작업 매개변수를 지정하는 두 가지 방법이 있습니다.

#### 기본 표기법

기본 표기법은 다음과 같이 지정됩니다.

```
parameterName=parameterValue,parameterType,identificationFlag
```



`parameterType`매개변수 유형의 완전한 이름은 어디에 있습니까? Spring Batch는 `DefaultJobParametersConverter`이 표기법을 지원하는 를 제공합니다.

#### 확장 표기법

기본 표기법은 대부분의 사용 사례에 적합하지만 예를 들어 값에 쉼표가 포함되어 있으면 불편할 수 있습니다. 이 경우 Spring Boot의 [Json 애플리케이션 속성](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.application-json) 에서 영감을 받아 다음과 같이 지정된 확장 표기법을 사용할 수 있습니다.

```
parameterName='{"value": "parameterValue", "type":"parameterType", "identifying": "booleanValue"}'
```



`parameterType`매개변수 유형의 완전한 이름은 어디에 있습니까? Spring Batch는 `JsonJobParametersConverter`이 표기법을 지원하는 를 제공합니다.

### 기록 데이터 액세스 영향

작업 매개변수 처리에 대한 이러한 주요 변경으로 인해 배치 메타데이터를 탐색하도록 설계된 일부 API는 v4에서 시작된 작업 인스턴스에 사용되어서는 **안 됩니다.** 예를 들어:

- `JobExplorer#getJobInstances`v4와 v5 사이의 혼합 기록 데이터를 검색할 수 있으며 v4의 작업 매개변수를 로드할 때 실패할 수 있습니다( [#4352](https://github.com/spring-projects/spring-batch/issues/4352) ). 이 특별한 경우에는 v5로 실행된 첫 번째 인스턴스의 인덱스에서 시작해야 합니다.
- `JobExplorer#getJobExecution`전달된 작업 실행 ID가 v4로 실행된 실행 중 하나인 경우 작업 매개변수 검색에 실패할 수 있습니다.

이 변경이 작업 매개변수 로드에 영향을 미치는 또 다른 경우는 실패한 작업 인스턴스를 다시 시작할 때입니다. 실패한 모든 v4 작업 인스턴스는 성공을 위해 다시 시작되거나 v5로의 마이그레이션이 완료되기 *전에* 중단될 것으로 예상됩니다 .


https://europani.github.io/spring/2023/06/26/052-spring-batch-version5.html


# Airflow 개요

## Airflow 란?

## Airflow 실습

## Airflow 다양한 옵션

## Airflow 모니터링





참조

```
https://europani.github.io/spring/2023/06/26/052-spring-batch-version5.html
https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide
https://docs.spring.io/spring-batch/docs/current/reference/html/spring-batch-intro.html
https://docs.spring.io/spring-batch/docs/current/reference/html/spring-batch-intro.html
```



example

```
https://github.com/erenavsarogullari/OTV_SpringBatch_Chunk_Oriented_Processing
https://www.baeldung.com/introduction-to-spring-batch
```





MariaDB  구성

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

INSERT INTO api_route(
path, method, uri)
VALUES ('/get', 'GET', 'https://httpbin.org/get');

```

