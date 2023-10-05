

# Airflow 개요

https://github.com/K9Ns/data-pipelines-with-apache-airflow.git



https://atonlee.tistory.com/196



## Airflow 란?

배치 중심 워크플로를 개발, 예약 및 모니터링하기 위한 오픈 소스 플랫폼입니다. Airflow의 확장 가능한 Python 프레임워크를 사용하면 거의 모든 기술과 연결되는 워크플로를 구축할 수 있습니다. 웹 인터페이스는 작업 흐름 상태를 관리하는 데 도움이 됩니다. Airflow는 노트북의 단일 프로세스부터 가장 큰 워크플로를 지원하는 분산 설정까지 다양한 방식으로 배포할 수 있습니다.

Airflow™는 일괄 워크플로 조정 플랫폼입니다. Airflow 프레임워크에는 다양한 기술과 연결하는 연산자가 포함되어 있으며 새로운 기술과 연결하기 위해 쉽게 확장할 수 있습니다. 워크플로의 시작과 끝이 명확하고 정기적으로 실행되는 경우 Airflow DAG로 프로그래밍할 수 있습니다.

클릭보다 코딩을 선호한다면 Airflow가 적합한 도구입니다. 워크플로는 다음을 의미하는 Python 코드로 정의됩니다.

- 이전 버전으로 롤백할 수 있도록 워크플로를 버전 제어에 저장할 수 있습니다.
- 여러 사람이 동시에 워크플로를 개발할 수 있습니다.
- 기능을 검증하기 위해 테스트를 작성할 수 있습니다.
- 구성 요소는 확장 가능하며 다양한 기존 구성 요소 모음을 기반으로 구축할 수 있습니다.

풍부한 스케줄링 및 실행 의미 체계를 통해 정기적으로 실행되는 복잡한 파이프라인을 쉽게 정의할 수 있습니다. 백필을 사용하면 논리를 변경한 후 기록 데이터에 대해 파이프라인을 (재)실행할 수 있습니다. 오류를 해결한 후 부분 파이프라인을 다시 실행하는 기능은 효율성을 극대화하는 데 도움이 됩니다.



### **장점**

#### **Dynamic**

Airflow에서 **Pipeline은 Python으로 정의**할 수 있다.

Python으로 가능한 것이면 Airflow에서 Pipeline내 Task로 실행할 수 있다.

#### **Scalable**

Airflow는 **Scalable**하다.

Arictecture를 어떻게 구성하느냐, Resource가 얼마나 되는지에 따라 얼마든지 **Task를 병렬로 실행할 수 있다.**

Arflow는 Modular Architecture로 구성되며 Message Queue를 사용한다.

#### **User Interface**

Airflow는 편하고 보기에 좋은 **Web Interface를 제공**한다.

Web Appliation을 통해 쉽게 **Pipeline를 모니터링, 관리**할 수 있다.

어렵지 않아 쉽게 이용할 수 있다.

#### **Extensible**

Airflow는 **Extensible 한다.**

필요한 기능에 대해 **plugin 형태로 쉽게 적용 가능**하다.

커스텀 기능을 추가하기도 좋다.

### **구성**



![img](https://blog.kakaocdn.net/dn/dfDjfj/btrw8Omey1B/ZsMp6KMDkfJdPt9iFwnozk/img.png)



- **WebServer**
  - **Web Interface를 제공하며 [Flask](https://flask.palletsprojects.com/en/2.0.x/), [gunicorn](https://gunicorn.org/)으로 구성**
- **Scheduler**
  - **Daemon Service로 Task의 Scheduling 담당**
  - **Task 모니터링, 관리**
- **Metastore**
  - **Airflow에 관련된 모든 Data를 저장하는 Database**
  - **[SqlAlchemy](https://docs.sqlalchemy.org/en/13/)를 통해 Metastore와 Interact**
  - **Default는 SQLite이며, PostgreSQL, MySQL로 가능**
- **Executor**
  - **Task 수행 방식 정의(the mechanism by which task instances get run)**
  - **종류**
    - **Local -> Debug, Local, Sequential Executor**
    - **Remote -> Celery, CeleryKubernetes, Dask, Kubernetes Executor**
- **Worker**
  - **실제로 Task를 수행하는 주체**

### **Concept**

#### **DAG**

**Directed Acyclic Graph**



![img](https://blog.kakaocdn.net/dn/9zni0/btrw8PyGv8Y/5IgDkdfkbLiTX1qZhnv1s1/img.png)



DAG는 **방향을 가진 비순환 Graph**이다.

위 그래프는 DAG이다.

순환하지 않고 일방향 성만 가진다.

**Airflow Pipeline은 DAG 형태로 구성**된다.

#### **Operator**

[**Operator**](https://airflow.apache.org/docs/apache-airflow/stable/concepts/operators.html)는 **Task의 Template 역할**을 한다.

DAG내에서 정의할 수 있다.

```
with DAG("my-dag") as dag:
    ping = SimpleHttpOperator(endpoint="http://example.com/update/")
    email = EmailOperator(to="admin@example.com", subject="Update complete")

    ping >> email
```

Operator는 아래와 같이 3가지 종류가 있다.

- **Action Operator**
  - **Function이나 Command 실행**
  - **BashOperator, PythonOperator ...**
- **Transfer Operator**
  - **Data를 Source -> Target에 Transfer**
- **Sensor**
  - **특정 조건을 만족했을 때 실행**

Operator의 종류는 많다.

기본적으로 설치가 되지 않는 Operator는 추가적인 설치가 필요하다.([참고링크](https://airflow.apache.org/docs/apache-airflow-providers/index.html))

#### **Task / Task Instance**

Data Pipeline이 trigger에 의해 실행되면 Pipeline 내 정의된 Task가 실행된다.

이렇게 실행된 Task가 Task Instance이다.

**OOP관점에서 Task는 Class이며 Task Instance는 Object이다.**

 

Airflow는 **Data Streaming Solution도 Data Proessing Framewokr도 아니다.**

그런 빅데이터 처리는 Spark에서 해야 한다.

Airflow는 **Orchestrator**이다.

## Airflow 아키텍처

*Airflow는 워크플로를* 구축하고 실행할 수 있는 플랫폼입니다 . 워크플로는 [DAG](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html) (방향성 비순환 그래프) 로 표시되며 [작업](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/tasks.html) 이라는 개별 작업 조각을 포함하며 종속성과 데이터 흐름을 고려하여 정렬됩니다.

![그래프로 렌더링된 Airflow DAG 예시](https://airflow.apache.org/docs/apache-airflow/stable/_images/edge_label_example.png)

DAG는 작업 간의 종속성과 작업 실행 및 재시도 실행 순서를 지정합니다. 작업 자체는 데이터 가져오기, 분석 실행, 다른 시스템 트리거 등 수행할 작업을 설명합니다.

Airflow 설치는 일반적으로 다음 구성 요소로 구성됩니다.

- [예약](https://airflow.apache.org/docs/apache-airflow/stable/administration-and-deployment/scheduler.html) 된 워크플로 트리거와 실행할 실행 프로그램에 [작업](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/tasks.html) 제출을 모두 처리하는 스케줄러 입니다.
- [실행](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/executor/index.html) 중인 작업을 처리하는 실행기 입니다. 기본 Airflow 설치에서는 스케줄러 *내부의 모든 것이 실행되지만 대부분의 프로덕션에 적합한 실행자는 실제로 작업 실행을* *작업자* 에게 푸시합니다 .
- DAG 및 작업의 동작을 검사, 트리거 및 디버그하기 위한 편리한 사용자 인터페이스를 제공하는 웹 *서버 입니다.*
- 스케줄러와 실행자(및 실행자가 보유한 모든 작업자)가 읽는 *DAG 파일* 폴더
- 상태를 저장하기 위해 스케줄러, 실행기 및 웹 서버에서 사용하는 메타 *데이터 데이터베이스입니다 .*

![../_images/arch-diag-basic.png](https://airflow.apache.org/docs/apache-airflow/stable/_images/arch-diag-basic.png)

대부분의 실행자는 일반적으로 작업자와 통신할 수 있도록 다른 구성요소도 도입합니다(예: 작업 대기열). 하지만 여전히 실행자와 해당 작업자를 전체 Airflow에서 실제 작업 실행을 처리하는 단일 논리적 구성요소로 생각할 수 있습니다.

Airflow 자체는 실행 중인 항목에 구애받지 않습니다. Airflow는 공급자 중 하나의 높은 수준의 지원을 받거나 셸 또는 Python Operators를 사용하는 명령으로 직접 모든 것을 조정하고 실행 [합니다](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/operators.html) .

### 워크로드

DAG는 일련의 [작업을](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/tasks.html) 통해 실행되며 다음과 같은 세 가지 일반적인 작업 유형이 있습니다.

- [연산자](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/operators.html) - DAG의 대부분을 구축하기 위해 빠르게 함께 연결할 수 있는 사전 정의된 작업입니다.
- 외부 이벤트가 발생하기를 전적으로 기다리는 Operator의 특수 하위 클래스인[ 센서입니다 .](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/sensors.html)
- [TaskFlow](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/taskflow.html) 장식 - `@task`Task로 패키지된 사용자 정의 Python 함수입니다.

내부적으로 이들은 모두 실제로 Airflow의 하위 클래스 `BaseOperator`이며 Task와 Operator의 개념은 어느 정도 상호 교환이 가능하지만 별도의 개념으로 생각하는 것이 유용합니다. 기본적으로 Operator와 Sensor는 템플릿이며 *DAG* 파일에서 하나를 호출하면 당신은 작업을 만들고 있습니다.

### 제어 흐름

[DAG는](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html) 여러 번 실행되도록 설계되었으며 여러 번 실행될 수 있습니다. DAG는 항상 "실행"되는 간격([ 데이터 간격](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dag-run.html#data-interval) )을 포함하지만 다른 선택적 매개변수도 포함하여 매개변수화됩니다.

[작업에는](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/tasks.html) 서로 선언된 종속성이 있습니다. `>>`and사용하면 DAG에서 이를 확인할 수 있습니다`<<`.

```
first_task >> [second_task, third_task]
fourth_task << third_task
```



또는 `set_upstream`및 `set_downstream`메소드를 사용하여:

```
first_task.set_downstream([second_task, third_task])
fourth_task.set_upstream(third_task)
```



이러한 종속성은 그래프의 "가장자리"를 구성하는 요소이며 Airflow가 작업을 실행할 순서를 결정하는 방식입니다. 기본적으로 작업은 실행되기 전에 모든 업스트림 작업이 성공할 때까지 기다립니다. [Branching](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html#concepts-branching) , [RecentOnly](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html#concepts-latest-only) 및 [Trigger Rules](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html#concepts-trigger-rules) 와 같은 기능을 사용하여 사용자 정의합니다 .

작업 간에 데이터를 전달하려면 다음 세 가지 옵션이 있습니다.

- [XComs](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/xcoms.html) ("교차 통신")는 작은 비트의 메타데이터를 푸시하고 풀할 수 있는 시스템입니다.
- 스토리지 서비스(사용자가 실행하는 서비스 또는 퍼블릭 클라우드의 일부)에서 대용량 파일 업로드 및 다운로드
- [TaskFlow API는 암시적 XCom을](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/xcoms.html) 통해 작업 간에 자동으로 데이터를 전달합니다.

Airflow는 공간이 확보되면 작업자에서 실행할 작업을 전송하므로 DAG의 모든 작업이 동일한 작업자 또는 동일한 머신에서 실행된다는 보장은 없습니다.

DAG를 구축하면 매우 복잡해질 수 있으므로 Airflow는 이를 보다 지속 가능하게 만들기 위한 여러 메커니즘을 제공합니다. [SubDAG를](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html#concepts-subdags) 사용하면 다른 DAG에 삽입할 수 있는 '재사용 가능한' DAG를 만들 수 있고 [TaskGroup을](https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dags.html#concepts-taskgroups) 사용하면 작업을 시각적으로 그룹화할 수 있습니다. UI.

[연결 및 후크](https://airflow.apache.org/docs/apache-airflow/stable/authoring-and-scheduling/connections.html) 형식으로 데이터 저장소와 같은 중앙 리소스에 대한 액세스를 쉽게 사전 구성하고 [풀을](https://airflow.apache.org/docs/apache-airflow/stable/administration-and-deployment/pools.html) 통해 동시성을 제한할 수 있는 기능도 있습니다 .

### 사용자 인터페이스

Airflow에는 DAG와 해당 작업이 수행되는 작업을 확인하고, DAG 실행을 트리거하고, 로그를 보고, DAG 문제에 대한 제한된 디버깅 및 해결을 수행할 수 있는 사용자 인터페이스가 함께 제공됩니다.

![../_images/dags.png](https://airflow.apache.org/docs/apache-airflow/stable/_images/dags.png)

이는 일반적으로 Airflow 설치 상태를 전체적으로 확인하고 개별 DAG를 살펴보고 레이아웃, 각 작업 상태 및 각 작업의 로그를 확인하는 가장 좋은 방법입니다.



## **How Airflow works**

Airflow가 어떻게 동작하는지 알아보려 한다.

Single Node, Multi Nodes에서 각각 어떻게 동작하는지 알아보려 한다.

### **Single Node Architecture**



![img](https://blog.kakaocdn.net/dn/SPrcU/btrxahC6s58/tHtZvvwl4DOScitzIYB0yk/img.png)



**Single Node Architecture**에서는 **모든 Airflow Componet가 단일 Machine에서 동작**한다.

(WebServer, Metastore, Scheduler, Executor)

WebServer는 Metastore에서 Data를 가져와 Web Application에서 보여준다.

Scheduler는 Metastore와 통신하고 실행한 Task가 있을 시 Executor에게 요청한다.

Executor는 Metstore에서 Data를 확인하고 실행할 Task를 실행한다.

Executor내부에 Queue가 존재한다.

**모든 Component는 Metastore을 통해 동작**한다.

### **Multi Nodes Architecture**



![img](https://blog.kakaocdn.net/dn/bcEq2s/btrxcxSwDoM/KPKj0yCIdV2Vd0unJ2BU6k/img.png)



**Multi Nodes Architecture**에서는 **Componete들이 다른 Machine에서 배치되어 동작**한다.

WebServer, Scheduler는 Metastore와 분리되었지만 동작은 똑같이 Metastore와 통신하며 동작한다.

내부에 있던 **Executor Queue는 외부로 분리되어진다.**

Executor Queue는 RabbitMQ, Redis와 같은 3rd party Tool로 구성된다.

**수행할 Task가 있으면 Executor가 해당 Task를 Queue에게 Push 한다.**

다수의 Worker는 Queue를 Push 된 Task를 수행한다.

### **동작**

DAG 파일 생성부터 Pipeline 진행이 어떻게 진행되는 과정은 아래와 같다.

1. **/dags 폴더에 Python으로 정의한 DAG 파일을 생성**
2. WebServer와 Scheduler가 /dag 폴더에 정의한 DAG 파일 Parse
   - **Web Application에 Data 노출**
   - **Scheduler는 Metastore에 DAGRun Object 생성**
3. **해당 Pipeline Trigger**
4. **Scheduler 해당 DAGRun Object 상태 Running으로 변경**
5. **Metastore에 TaskInstance 생성**
6. **Executor가 TaskInstance 실행/종료 상태 변경**
7. Scheduler는 지속적으로 Pipeline의 모든 Task 종료 여부 체크
   - **모든 Task 종료되면 DAGRun Object 상태 Completed로 변경**
8. **WebServer UI 해당 DAG 상태 변경**



## 언제 Airflow를 사용해야 할까?

Airflow를 선택하는 이유

Airflow가 배치 지향 (Batch-oriented) 데이터 파이프라인을 구현하는데 적합한 이유는

- 파이썬 코드를 이용해 파이프라인을 구현할 수 있기 때문에 파이썬 언어에서 구현할 수 있는 대부분의 방법을 사용하여 복잡한 커스텀 파이프 라인을 만들 수 있다
- 파이선 기반의 Airfllow는 쉽게 확장이 가능하고 다양한 시스템과 통합이 가능하다. 실제로 Airflow 커뮤니티에서 다양한 유형의 데이터 베이서,, 클라우드 서비스 등과 통합할 수 있는 수 많은 애드온이 존재한다
- 수많은 스케줄링 기법은 파이프라인을 정기적으로 실행하고 점진적(증분 : incremental) 처리를 통해 전체 파이프라인을 재실행할 필요 없는 효율적인 파이프라인 구축이 가능한다
- 백필 기능을 사용하면 과거 데이터를 손쉽게 재처리할 수 있기 때문에 코드를 변경한 후 재생성이 필요한 데이터 재처리가 가능한다
- Airflow의 훌륭한 웹 인터페이스는 파이프라인 실행 결과를 모니터링할 수 있고 오류를 디버깅하기 위한 편리한 뷰를 제공한다
- 또 다른 장점은 Airflow는 오픈 소스라는 것이다. 때문에 특정 벤더에 종속되지 않고 Airflow를 사용할 수 있다. 또한 몇몇 회사에서는 Airflow를 설치 관리 및 실행에 대한 유연성을 제공하는 관리형 (managed) Airflow 솔루션 또한 제공하고 있다.

Airflow가 적합하지 않는 경우

- Airflow는 반복적이거나 배치 태스트에 적합하여, 스트리밍(실시간 데이터 처리) 워크플로 및 해당 파이프라인 처리에 적합하지 않을 수 있다
- 추가 및 삭제 태스트가 빈번한 동적 파이프라인의 경우에는 적합하지 않을 수 있다 (동적 태스크를 구현할 수 있지만, 웹 인터페이스는 DAG의 가장 최근 실행 버전에 대한 정의만 표현, 따라서 airflow는 실행되는 동안 구조가 변경되지 않은 파이프라인에 좀 더 적합)
- 파이썬 언어로만 구현 되어있고, DAG를 구현
- 파이썬 코드로 DAG를 작성하는 것은 파이프라인 규모가 커지면 광장히 복잡해 질 수 있다. 때문에 장기적으로 Airflow DAG를 유지 관리 위해서는 초기 사용 시점에서부터 엄격한 관리가 필요하다



## AIrflow 기본 개념

### Operator의 특성

- single task를 정의 해야한다.
- 멱등성을 유지해야한다. 다른 작업간에 겹침 현상을 제거하여 오류제거를 위함이다.
- 자동 재시도를 작성하여 자동으로 재시도 해줄수 있다.
- 하나의 task는 하나의 Operator 클래스로 구성되어 생성이 되어진다.

### Operator 종류

Operator는 어떤 작업을 할지 정의 해주는 것이기 때문에 많은 종류의 Operator들이 있다. BashOperators, pythonOperator, EmailOperator, MySqlOperator,SqliteOperator, PostgreOperator 등등

아래 링크에서 확인 가능하다 이것을 보고 DAG를 작성하면 된다.

[airflow.operators - Airflow Documentation](https://airflow.apache.org/docs/apache-airflow/stable/_api/airflow/operators/index.html)

### Operator 타입

모든 Operator는 BaseOperator를 상속받아 구성된다. 그리고 밑에 3가지 경우로 상속받아 재구성되어 사용되어진다.

- Action operators - 실제 연산을 수행
- Transfer operators - 데이터를 옮김
- sensor operators - 태스크를 언제 실행시킬 트리거를 기다림

 

### Task란?

Task는 airflow의 기본 실행 단위. 작업은 DAG로 정렬된 다음 실행해야 하는 순서를 표현하기 위해 작업간 스트림 및 다운스트림 종속성을 설정

Task 타입 세가지

- Operators : DAG의 대부분 구축되어있는 모듈로써 작업 템플릿 거의 이것으로 다 가능
- Sensors : 전적으로 외부 이벤트가 발생하기를 기다리는 연산자의 특수 하위 클래스
- TaskFlow : 장식 @task 된, 태스크로 패키지된 사용자 정의 python함수

기본적으로 BaseOperator를 상속받아 진행 하기 때문에 상요 교환 및 운용이 가능

### 관계(Relationship)

```
# 비트 시프트로 표시
first_task >> second_task >> [third_task, fourth_task]

# 함수로 표시
first_task.set_downstream(second_task)
third_task.set_upstream(second_task)
```

### Task instance

DAG실행 될때 마다 Task Instance를 생성하여 Executor로 넘긴다음 해당 작업을 실행한다. 그리고 그 Task instance를 다시 Metadata로 보내서 상태를 업데이트 하며, Task Instance가 작업이 아직 남아 있으면 사디 Executor로 보내진다. 작업이 완료가 되면 Scheduler에게 보내지는데 가운데의 상태를 잘 알아야 scheduler의 다음 동작을 잘 알수 있다.

### 상태

- none : 태스크가 아직 실행을 위한 큐가 없는 상태
- scheduled : 스케쥴러가 작업의 종속성이 충족되고 실행 되어야 한다고 결정
- queued : 작업이 Executor에 할당되었으며 작업자를 기다리고 있음
- running : 작업이 작업자에서 실행 중
- success : 작업이 오류 없이 성공
- shutdown : 태스트가 실행 중일 때 종료 하도록 외부적으로 요청이 됨
- restarting : 작업이 실행 중 일 때 다시 시작하도록 외부에서 요청한 작업
- failed : 작업을 실행하는 동안 오류가 발생하여 실행하지 못했음
- skipped : 분기, LatestOnly 등으로 인해 작업을 건너뛰었음
- upstream_failed : 업스트림 작업이 실패했고 트리거 규칙이 필요하다고 말함
- up_for_retry : 작업이 실패했지만 재시도 횟수가 남았고 일정이 다시 잡힘
- up_fro_reschedule : reschdule안에 있는 sensor 역할을 함
- sensing : 과제에 Smart Sensor
- deferred : 작업이 트리거로 인해 연기 되었음
- removed : 실행이 시작된 후 작업이 DAG에서 사라졌음

### Task- workflow



![img](https://blog.kakaocdn.net/dn/dQfvJm/btrFoPyPMOQ/jPOtjmg6CEkAiQpvxgXsz1/img.png)



### 시간 초과(Time Out)

Task가 최대 런타임을 가지는 속성을 정희하는 것이다. execution_timeout 속성을 datetime.timedelta 최대 허용 런타임 값으로 설정하면 된다.

SFTPSensor를 예로 들면

- 센서가 SFTP서버에 요청 할 때마다 최대 60초가 걸릴수 있음.
- 센서가 SFTP서버에 요청이 60초 이상 걸리면 AirflowTaskTimeout이 올라감. 그리고 센서는 retries를 하고 최대 재시도 2번까지 함
- 첫 번째 실행 시작부터 결국 성공할 때까지 센서는 에 정의된 대로 최대 3600초 동안 허용됨. 즉, 3600초 이내에 파일이 SFTP 서버에 나타나지 않으면 센서가 AirflowSensorTimeout. 이 오류가 발생하면 다시 시도하지 않음

```
sensor = SFTPSensor(
	task_id="sensor",
	path="/root/test",
	execution_timeout=timedelta(seconds=60),
	timeout=3600,
	retries=2,
	mode="reschedule",
)
```



## Airflow 실습

참고

```
https://developnote-blog.tistory.com/176
https://developnote-blog.tistory.com/124
https://todaycodeplus.tistory.com/52
```







우선 설치를 하기위해서 Airflow 공식 Helm설치 홈페이지를 참고합니다.

링크 : https://airflow.apache.org/docs/helm-chart/stable/index.html

[ Helm Chart for Apache Airflow — helm-chart Documentation airflow.apache.org](https://airflow.apache.org/docs/helm-chart/stable/index.html)

step 1. install helm chart Airflow

```
helm repo add apache-airflow https://airflow.apache.org
helm upgrade --install airflow apache-airflow/airflow --namespace airflow --create-namespace
```

1번 라인에서 Helm Chart를 등록하고 2번 라인에서 Helm Chart를 통해서 설치를 진행합니다.

설치된 Helm의 이름은 airflow이며 이 Helm Chart를 통해서 설치되는 위치는 새로 생성한 airflow namespace에 설치됩니다.

 

## github access token 생성하기

github 로그인설정하는방법은 여러가지가있는거같은데 (helm configuration?같은 설정도있었는데..암튼패스)
제일 쉬운 access token을 통해 접근하는방법을 사용해보겠다.
우선 github access token을 아래 절차에 따라 발급받는다.

1. github > user profile누르면 나오는 settings 에 들어간다.
2. 좌측 메뉴 맨 아래에 Developer settings 에 들어간다.
3. 좌측 메뉴 맨 아래에 Personal access tokens 들어간다.
4. Generate new token 눌러서 access token 생성하고 복사해놓는다. (권한은 repo 권한만 주면됨)

생성하고 다른페이지가면 바로 감춰지니까 미리미리 잘 복사해놓자.

## helm 설정파일 수정하기

이제 helm chart 설정파일의 git sync 부분을 수정해준다. url 부분이 핵심이고, 아래 양식으로 써주면된다.
`http://{내 githubid} : {내 access token} @ github.com / {내 githubid} / {github repository명}`

```
## Configure Git repository to fetch DAGs
  git:
    ## berrrrr는 제 id입니다. (각자 자기 github id로 변경하세요)
    ## url to clone the git repository (access token은 가립니다). 
    ## airflow-test repository에 DAG를 넣는다고 가정합니다. 
    url: https://berrrrr:accesstoken넣으세요@github.com/berrrrr/airflow-test
    ##
    ## branch name, tag or sha1 to reset to
    ref: master
    ## pre-created secret with key, key.pub and known_hosts file for private repos
    secret: 
    ## The host of the repo so for example if a github repo put github.com (Only need if using ssh not https git sync)
    repoHost:
    ## The name of the private key in your git sync secret (Only need if using ssh not https git sync)
    privateKeyName:
    gitSync:
      ## Turns on the side car container
      enabled: true
      ## Image for the side car container
      image:
        ## docker-airflow image (공식 docker이미지입니다. 아래 주소 그대로 써주세요)
        repository: k8s.gcr.io/git-sync
        ## image tag (버전은 이게 최신은아닐거같지만..ㅋ)
        tag: v3.1.1
        ## Image pull policy
        ## values: Always or IfNotPresent
        pullPolicy: IfNotPresent
      ## The amount of time in seconds to git pull dags (sync 맞출 간격입니다. 저는 1분간격으로 땡겨오게했습니다)
      refreshTime: 60 
  initContainer:
    ## Fetch the source code when the pods starts
    enabled: true
    ## Image for the init container (any image with git will do)
    image:
      ## docker-airflow image
      repository: k8s.gcr.io/git-sync 
      ## image tag
      tag: v3.1.1
      ## Image pull policy
      ## values: Always or IfNotPresent
      pullPolicy: IfNotPresent
    ## install requirements.txt dependencies automatically
    installRequirements: true
```

일케해서 배포하면 git-sync용 컨테이너가 따로생겨서 계속 github에 있는 dag파일들을 땡겨온다.





step 2. 설치 확인하기

```
kubectl get po -n airflow
```

 

kubectl 명령어를 통해서 방금 생성한 airflow namespace에 pod를 조회해 보자.

 



![img](https://blog.kakaocdn.net/dn/JWPQE/btrAAbtxJOM/iNSTWB9WuN5kj92hOzsLz1/img.png)



다음과 비슷한 내용을 얻었다면 

설치 성공!

 

step 3. 접속 Airflow

```
kubectl get svc -n airflow
```

airflow webserver의 SVC 즉 service 이름을 찾아 냅니다.

저는 그냥 airflow-webserver 로 되어있더군요 

 

```
kubectl port-forward svc/airflow-webserver 8080:8080 -n airflow
```

포트포워딩을 통해서 내부에만 존재하는 Airflow의 포트를 노출시켜 줍니다. 



![img](https://blog.kakaocdn.net/dn/dKXslM/btrAAZTYOEK/ILoDjsZU3fGEp10rWsBeN0/img.png)



요런식으로 나왔다면 웹브라우저를 열어서 localhost:8080 으로 접속을 합니다.



![img](https://blog.kakaocdn.net/dn/cwgaMU/btrACLOsnFu/cF5qP2x6V2KsfJxKqTkrKK/img.png)



이렇게 접속이 되는 모습을 확인 가능합니다.





## Airflow 다양한 옵션

### 1. using Preset

 

| **Preset**   | **Meaning**** **                          |
| ------------ | ----------------------------------------- |
| **@once**    | 한 번 실행                                |
| **@hourly**  | 한 시간에 한 번, 한 시각이 시작할 때 실행 |
| **@daily**   | 하루에 한번, 자정에 실행                  |
| **@weekly**  | 일주일에 한 번, 일요일 자정에 실행        |
| **@monthly** | 한 달에 한 번, 그 달의 첫 날 자정에 실행  |
| **@yearly**  | 1년에 한번씩 1월 1일 자정에 실행          |

 

아래 예시로 조금 더 자세히 알아보겠습니다.

 

```
dag = DAG(
    dag_id="02_daily_schedule",
    schedule_interval="@daily",             ❶
    start_date=dt.datetime(2019, 1, 1),     ❷
    ...
)
```

 

❶ 매일 자정에 해당 DAG를 실행시키기 위한 스케줄 설정

❷ DAG 스케줄링을 시작할 일시

 

 

특정 간격을 주기적으로 DAG를 실행하는 것을 알아보았는데요.

그렇다면, 조금 더 세밀한 조정을 통해 실행하고 싶다면 어떻게 할 수 있을까요?

 

 

 

### 2. Cron-based intervals

DAG를 실행하고자 할 때, 가령 "매주 토요일 23시 45분" 과 같이 정밀한 시기를 지정하고 싶어질 때가 있습니다.

이렇게 좀 더 복잡한 스케줄링을 위해, cron과 같은 스케줄링을 위한 정규 표현식을 사용할 수 있습니다.

 

cron은 macOS나 Linux와 같은 유닉스 계열 컴퓨터 운영 체제에서 사용되는 시간 기반 작업 스케줄러입니다.

cron은 다섯 가지 구성 요소로 아래와 같이 정의됩니다.

 

```
# ┌─────── minute (0 - 59)
# │ ┌────── hour (0 - 23)
# │ │ ┌───── day of the month (1 - 31)
# │ │ │ ┌───── month (1 - 12)
# │ │ │ │ ┌──── day of the week (0 - 6) (Sunday to Saturday;
# │ │ │ │ │      7 is also Sunday on some systems)
# * * * * *
```

 

cron 작업은 시간/날짜 필드가 현재 시스템 시간/날짜와 일치할 때 실행됩니다.

특정 시기를 명시하기 원하지 않는 필드를 정의하기 위해 숫자 대신 별표(*)를 사용할 수 있는데,

해당 필드의 값을 신경 쓰지 않는다는 것을 의미합니다.

cron 표현식이 처음에는 복잡하다고 느껴질 수 있겠지만, 시간 간격을 유연하게 정의할 수 있습니다.

예를 들어, 아래와 같은 cron 식을 표현해서 시간 간격, 일 간격 및 주 간격 등을 정의할 수 있습니다.


`0 0 * * *` : daily (자정 실행)
`0 0 * 0` : 매주 (일요일 자정에 실행)

`0 0 1 * *` : 매월 1일 자정

`45 23 * * MON, SAT` : 매주 월요일, 토요일 23:45

`0 0 * * MON-FRI` : 주중 평일 자정에 실행

 

 

 

### 3. Frequency-based intervals

"5분에 한 번" 혹은 "3일에 한 번" 등과 같이 특정 빈도를 기반으로 스케줄링을 작성하고 싶어질 때도 있습니다.

 

빈도 기반의 스케줄을 설정하기 위해,

Airflow는 상대적인 시간 간격으로 스케줄 간격을 정의하도록 지원합니다.

 

빈도 기반 스케줄을 사용하기 위해서는 표준 라이브러리인 `datetime` 모듈에서 `timedelta` 인스턴스를 스케줄 간격으로 전달할 수 있습니다.

 

```
dag = DAG(
    dag_id="04_time_delta",
    schedule_interval=dt.timedelta(days=3),              ❶
    start_date=dt.datetime(year=2019, month=1, day=1),
    end_date=dt.datetime(year=2019, month=1, day=5),
)
```

 

❶ timedelta 를 통해 빈도 기반의 스케줄링을 사용할 수 있음

 

 

 

 

 

그럼 지금까지 Apache Airflow의 Scheduling 방식에 대해 알아보았습니다.



## Airflow 모니터링



참고

```
https://gngsn.tistory.com/263
https://atonlee.tistory.com/196
https://berrrrr.github.io/programming/2020/07/04/airflow-github-sync/
https://magpienote.tistory.com/193
```

