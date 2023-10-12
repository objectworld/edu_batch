

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

### **아키텍처**

## **아키텍처**



![img](https://blog.kakaocdn.net/dn/yu7vN/btrWUcKsFBo/832aExKKaCfItIdTxbzKEk/img.png)https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/overview.html



Airflow는 크게 다음과 같은 컴포넌트들로 구성되어 있습니다.

### **DAG Directory**

- 파이썬으로 작성된 DAG 파일을 저장하는 공간입니다.
  - DAG 파일을 저장하는 공간입니다. dag_folder 혹은 dags_folder 로도 불립니다. 기본적으로 $AIRFLOW_HOME/dags/ 가 DAG Directory로 설정되어 있습니다.

### **Scheduler**

- DAG를 분석하고 현재 시점에서 DAG의 스케줄이 지난 경우 Airflow 워커에 DAG의 태스크를 예약합니다.
- Airflow의 가장 중요한 부분으로, 다양한 DAG Run과 Task들을 스케쥴링 및 오케스트레이션 합니다.
- 또한 하나의 DAG Run이 전체 시스템을 압도하지 않도록 각 DAG Run의 실행 횟수를 제한하기도 합니다.
- DAG 파일 구문 분석, 즉 DAG 파일 읽기, 비트 및 조각 추출, 메타 스토어에 저장합니다.
- 실행할 태스크를 결정하고 이러한 태스크를 대기열에 배치합니다.
  - Scheduler는 DAG 파일을 파싱하고, 모든 Task와 DAG들을 모니터링하며, Task Instance와 Dag Run들의 스케줄링 및 오케스트레이션을 담당합니다
    - Dag Directory에서 파일을 처리하고 결과를 얻는 일
    - DAG Run과 Task Instance의 상태를 변경하고 Executor가 실행시킬 큐에 Task Instance를 넣는 일
    - Executor로 스케줄링 큐에 들어온 Task를 실행시키는 일
- Meta Database에 DAG 정보 및 DAG Run에 대해 저장합니다.
- 스케줄러의 여러 가지 역할
  - DAG 파일을 구문 분석하고 추출된 정보를 데이터베이스에 저장
  - 실행할 준비가 된 태스크를 결정하고 이를 대기 상태로 전환
  - 대기 상태에서 태스크 가져오기 및 실행
- SchedulerJob의 역할
  - DAG 파일을 파싱하고 추출된 정보를 데이터베이스에 저장하는 역할을 수행합니다.
  - DAG 프로세서 : Airflow 스케줄러는 DAG 디렉터리(AIRFLOW__CORE__DAGS__FOLDER에서 설정한 디렉터리)의 파이썬 파일을 주기적으로 처리합니다.
  - 태스크 스케줄러 : 스케줄러는 실행할 태스크 인스턴스를 결정하는 역할을 합니다.

### **Executor**

- Scheduler 내부의 구성 요소입니다.
- Scheduler가 작업을 조정하는 동안 Executor는 실제로 작업을 실행합니다.
- Sequential, Local, Celery, Kubernetes 등 Executor에는 여러 종류가 있습니다. (기본 값은 Sequential Executor입니다.)
- 워크로드를 여러 머신에 분산하려는 경우 CeleryExecutor 및 KubernetesExecutor의 두 가지 옵션 존재하고 단일 시스템의 리소스 제한에 도달하거나 여러 시스템에서 태스크를 실행하여 병렬 실행을 원하거나 태스크를 여러 시스템에 분산하여 작업 속도를 더 빠르게 실행하고자 할 때 사용할 수 있습니다.
- Executor는 Scheduler에서 생성하는 서브 프로세스로 Queue에 들어온 Task Instance를 실제로 실행하는 역할을 합니다.
- **Local Executors** : Task Instance를 Scheduler 프로세스 내부에서 실행합니다.
  - Sequential Executor
    - Airflow 익스큐터 중 가장 단순하게 구성할 수 있는 방법이자, Airflow를 별도의 설정이나 환경 구성 없이 바로 실행시킬 수 있는 방법입니다.
    - 태스크를 순차적으로 하도록(한 번에 하나씩) 구성되어 있습니다,
    - 주로 테스트 및 데모 목적으로 사용되는 쪽으로 많이 선호합니다.
    - 작업 처리 속도가 상대적으로 느리며 단일 호스트 환경에서만 작동합니다.
  - Local Executor
    - 한 번에 하나의 태스크로 제한되지 않고 여러 태스크로 병렬로 실행할 수 있습니다.
    - 익스큐터 내부적으로 워커 프로세스가 FIFO(First in, First out) 적용 방식을 통해 대기열에서 실행할 태스크를 등록합니다.
    - 기본적으로 최대 32개의 병렬 프로세스를 실행합니다.
- **Remote Executors** : Task Instance를 Scheduler 프로세스 외부에서 실행합니다.
  - **Celery Executor** 



![img](https://blog.kakaocdn.net/dn/lA0pE/btsglPvIT6W/KG92WFz0HX8TCJL50rgZ3k/img.png)https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/executor/celery.html



- Celery Executor
  - 내부적으로 Celery를 이용하여 실행할 태스크들에 대해 대기열을 등록합니다.
  - 워커가 대기열에 등록된 태스크를 읽어와 개별적으로 처리합니다.
  - 사용자 관점에서 볼 때 태스크를 대기열로 보내고 워커가 대기열에서 처리할 태스크를 개별적으로 읽어와 처리하는 과정은 LocalExecutor와 유사합니다.
  - LocalExecutor와 가장 큰 차이점은 모든 구성요소가 서로 다른 호스트에서 실행되기 때문에 작업 자체에 대한 부하가 LocalExecutor에 비해 낮습니다.
  - Celery는 대기열 메커니즘(Celery에서 처리할 때는 Broker라고 지칭)을 위해 RabbitMQ, Redis 또는 AWS SQS를 지원합니다.
  - 멀티스레드 싱클톤(singleton) 스케줄러 서비스를 구현합니다. 작업을 호출하는 메시지는 RabbitMQ 또는 Redis 데이터베이스에서 대기열에 추가되고 작업은 여러 Celery 작업자에게 분배됩니다.
  - Celery의 모니터링을 위해 Flower라는 모니터링 도구를 함께 제공합니다.
  - Celery는 파이썬 라이브러리 형태로 제공되므로 Airflow 환경에 적용하기 편리합니다.

- Kubernetes Executor
  - ![img](https://blog.kakaocdn.net/dn/mDZmi/btrWRWO6Siw/EXDfundjff4dKIFIfr5aL0/img.png)https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/executor/kubernetes.html
  - ![img](https://blog.kakaocdn.net/dn/bxzeiU/btrW4Fj0gnI/tkKahEdDhpiCjvbErjU9Rk/img.png)https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/executor/kubernetes.html
  - 쿠버네티스에서 워크로드를 실행합니다.
  - Airflow를 실행하려면 쿠버네티스 클러스터의 설정 및 구성이 필요하며 익스큐터는 Airflow 태스크를 배포하기 위해 쿠버네티스 API와 통합됩니다.
  - 쿠버네티스는 컨테이너화된 워크로드를 실행하기 위한 사실상의 표준 솔루션 입니다.

- Airflow에서는 익스큐터 유형에 따라 다양한 설치 환경을 구성할 수 있습니다. 

| 익스큐터                  | 분산   | 환경 설치 난이도 | 사용에 따른 적합한 황경                    |
| ------------------------- | ------ | ---------------- | ------------------------------------------ |
| SequentialExcutor(기본값) | 불가능 | 매우 쉬움        | 시연 / 테스트                              |
| LocalExcutor              | 불가능 | 쉬움             | 단일 호스트 환경 권장                      |
| CeleryExecutor            | 가능   | 보통             | 멀티 호스트 확장 고려 시                   |
| KubernetesExecutor        | 불가능 | 어려움           | 쿠버네티스 기반 컨테이너 환경 구성 고려 시 |

- 익스큐터 설치
  - SequentalExecutor 설정
    - 스케줄러의 태스크 오퍼레이터 부분은 단일 하위 프로세스에서 실행되고 이 단일 하위 프로세스 내에서 작업은 순차적으로 하나씩 실행되므로 익스큐터 종류 중 가장 느린 실행 방법입니다.
    - 구성 절차가 필요하지 않기 때문에 테스트 시점에 매우 편리하게 사용할 수 있습니다.
  - LocalExecutor 설정
    - 아키텍처는 SequentialExecutor와 유사하지만, 여러 하위 프로세스가 있어 병렬로 태스크를 실행할 수 있으므로 SequentialExecutor에 비해 빠르게 수행할 수 있습니다.
    - 각 하위 프로세스는 하나의 태스크를 실행할 수 있으며, 하위 프로세스는 병렬로 실행할 수 있습니다.
    - 모든 구성 요소를 별도의 컴퓨터에서 실행할 수 있으며, 스케줄러에 의해 생성된 하위 프로세스는 모두 하나의 단일 시스템에서 실행됩니다.
  - CeleryExecutor 설정
    - Celery는 대기열 시스템을 통해 워커에게 메시지를 배포하기 위한 프레임워크를 제공합니다.
    - 태스크가 Celery worker를 실행하는 여러 컴퓨터로 분배하고 워커는 태스크가 대기열에 도착할 때까지 기다립니다.
    - Celery에서는 대기열을 브로커라합니다.
    - Airflow webserver 실행
    - Airflow scheduler 실행
    - Airflow Celery worker 실행
  - KubernetesExecutor 설정
    - 모든 태스크가 쿠버네티스의 파드(pod)에서 실행됩니다.
    - 쿠버네티스에서 웹 서버, 스케줄러 및 데이터베이스를 실행할 필요는 없지만, KubernetesExecutor를 사용할 때 쿠버네티스에서 다른 서비스들이 함께 실행되는 것이 관리하기 좀 더 수월합니다.
    - 파드가 쿠버네티스에서 가장 작은 작업 단위이며 하나 이상의 컨테이너를 실행할 수 있습니다.
    - 다른 익스큐터는 작업 중인 워커의 정확한 위치를 항상 알 수 있으며, 쿠버네티스를 사용하면 모든 프로세스 파드에서 실행되며, 파드는 동일한 시스템에서 실행될 수도 있지만 여러 호스트에 분산되어 실행될 수 있습니다.
    - 사용자의 관점에서 볼 때 프로세스는 파드에서 실행되며 사용자는 실행하는 프로세스가 어떤 호스트에서 실행되는지 명확하게 바로 알 수는 없습니다.
- Airflow 프로세스 간에 DAG파일을 배포하는 방법을 결정
  - PersistentVolume을 사용하여 포드 간에 DAG 공유
  - Git-sync init container를 사용해 리포지토리의 최신 DAG 코드 가져오기
  - Docker 이미지에 DAG 빌드

### **Worker**

- 예약된 태스크를 선택하고 실행합니다.
- Executor에 의해 만들어지며 Task를 실제로 실행하는 프로세스입니다.
- Executor의 종류에 따라 Worker는 쓰레드, 프로세스, 파드가 될 수 있습니다.

### **Meta Database**

- DAG, 해당 실행 및 사용자, 역할 및 연결과 같은 기타 Airflow 구성에 대한 메타데이터를 저장합니다.
  - Meta Database는 Airflow의 DAG, DAG Run, Task Instance, Variables, Connections 등 여러 컴포넌트에서 사용해야하는 데이터를 저장합니다. Webserver, Scheduler, Worker 모두 Meta Database와 통신하기 때문에 Meta Database는 Scheduler와 더불어 매우 중요한 컴포넌트입니다.
  - Airflow를 위한 메타스토어 설정
    - 메타스토어(metastore) : Airflow에서 일어나는 모든 일은 데이터베이스에 등록되며 이를 Airflow에서 칭합니다.
    - 워크플로 스크립트 : 스케줄러를 통해 작업 내역을 분석 및 관리하는 역할을 수행하며 메타스토어에 그 해석된 내용을 저장하는 등의 여러 컴포넌트로 구성되어 있습니다.
    - Airflow는 Python ORM(Object Relational Mapper) 프레임워크인 SQLAlchemy를 사용하여 모든 데이터베이스 태스크를 수행하며 SQL 쿼리를 수동으로 작성하는 대신, 직접 데이터베이스에 직접 편리하게 작성할 수 있습니다.

- Webserver
  - 웹 서버는 파이프라인이 현재 상태에 대한 정보를 시각적으로 표시하고 사용자가 DAG 트리거와 같은 특정 태스크를 수행할 수 있도록 관리하는 역할을 수행합니다.
  - 스케줄러에서 분석한 DAG를 시각화하고 DAG 실행과 결과를 확인할 수 있는 주요 인터페이스를 제공함
  - Airflow의 Web UI 입니다.
  - Meta Database로 부터 DAG 정보를 읽어와 DAG 정보 및 DAG Run의 상태를 확인하고 실행할 수 있습니다.
    - Webserver는 Meta Database와 통신하며 DAG, DAG Runs, Task Instance, Variables, Connections 등의 데이터를 가져와 웹에서 보여주고 유저와 상호작용 할 수 있게 합니다.
- 모든 Airflow 프로세스의 로그 확인
  - 웹 서버 로그 : 웹 활동에 대한 정보, 즉 웹 서버로 전송되는 요청에 대한 정보를 보관합니다.
  - 스케줄러 로그 : DAG 구문분석, 예약 작업 등을 포함한 모든 스케줄러 활동에 대한 정보를 보관합니다.
  - 태스크 로그 : 각 로그 파일에는 단일 태스크 인스턴스의 로그가 보관됩니다.



## **Dag의 구조**

- DAG 명세서
  - Dummy DAGS (Start, End 계열) 일부 제외
    - DummyOperator는 아무 실행도 하지 않는 Operator입니다. 간혹 Task 간 의존성 흐름 내 필요한 경우에 사용됨
  - ExternalTaskSensor 계열은 무조건 후속작업이 있으며, 자신이 감지하고 있는 Task가 끝날 때까지 계속 작동
  - SSHOperator : 일반적인 작업 수행 DAG로 해당 Job이 끝나면 Success를 찍고 끝남
  - TriggerDagOperator : 다른 DAG를 시작시키는 Trigger 역할
  - BranchPythonOperator : 조건에 따라 다른 DAG를 실행해야 할 경우 어느 DAG로 분기해야 할지 DAG 명을 리턴해주는 역할
    - BranchPythonOperator는 특정 조건에 따라 의존성 흐름에 분기를 줄 수 있는 Operator
  - ShortCircuitOperator : BranchPythonOperator와 비슷하나 조건이 False 가 나오면 흐름을 무조건 끊고 다음 작업들을 전부 Skip 시킴
  - PythonOperator : Deploy 서버 내부에서 실행되는 파이썬 코드
  - BashOperator는 bash 커맨드를 실행하는 Operator
  - EmailOperator는 Email을 보내는 Operator
  - Custom Operator : Airflow Operator는 직접 Custom 하게 작성할 수 있음
  - 파이썬 코드 실행 옵션
    - PythonOperator를 사용하는 대신 BashOperator를 사용하여 파이썬 스크립트를 실행함, PythonOperator로 파이썬 코드를 실행하려면 코드를 DAG 정의 파일에 작성하거나 DAG 정의 파일로 가져와야 함, 오케스트레이션과 이 오케스트레이션이 실행하는 프로세스의 로직을 더 많이 분리하고 싶었음, 에어플로우와 내가 실행하려는 코드 간에 호환되지 않는 파이썬 라이브러리 버전의 잠재적인 문제를 피할 수 있음, 프로젝트(및 Git 저장소)를 분리하여 데이터 인프라 전반에 걸쳐 로직을 유지 관리하는 것이 더 쉬움
- 태스크와 오퍼레이터 차이점
  - 오퍼레이터(operator) : 단일 태스크를 나타냅니다.
    - 단일 작업 수행 역할
    - PythonOperator : 파이썬 함수를 실행하는 데 사용됨
    - EmailOperator : 이메일 발송에 사용됨
    - Simple HttpOperator : HTTP 엔드포인트 호출
  - DAG는 오퍼레이터 집합에 대한 실행을 오케스트레이션(orchestration - 조정, 조율)하는 역할을 함, 오퍼레이터의 시작과 정지, 오퍼레이터가 완료되면 연속된 다음 태스크의 시작, 그리고 오퍼레이터 간의 의존성 보장이 포함됨
  - Airflow에서 태스크는 작업의 올바른 실행을 보장하기 위한 오퍼레이터의 래퍼(wrapper) 또는 매니저(manager)로 생각해 볼 수 있음
  - 사용자는 오퍼레이터를 활용해 수행할 작업에 집중할 수 있으며, Airflow는 태스크를 통해 작업을 올바르게 실행할 수 있음
  - DAG와 오퍼레이터는 Airflow 사용자가 이용함, 태스크는 오퍼레이터의 상태를 관리하고 사용자에게 상태 변경(예:시작/완료)을 표시하는 Airflow의 내장 컴포넌트
- 다양한 오퍼레이터를 사용할 때는 다양한 종속성을 위한 많은 모듈이 설치되어야 하기 때문에 잠재적인 충돌이 발생하고 환경 설정 및 유지 관리가 상당히 복잡해짐(많은 패키지를 설치하면 잠재적인 보안 위험은 말할 것도 없이 높아짐), 파이썬은 동일한 환경에 동일한 패키지의 여러 버전을 설치할 수 없기 때문에 문제가 됨

------

## **스케줄링**



![img](https://blog.kakaocdn.net/dn/b7fpLJ/btrW38T6uSz/pH6H641M9q4ztN679gAdm1/img.png)https://airflow.apache.org/docs/apache-airflow/stable/core-concepts/dag-run.html



- Cron 기반의 스케줄 간격 설정하기
  - 더 복잡한 스케줄 간격 설정을 지원하기 위해서 cron(macOS 및 리눅스와 같은 유닉스 기반 OS에서 사용하는 시간 기반 작업 스케줄러)과 동일한 구문을 사용해 스케줄러 간격을 정의함
- 빈도 기반의 스케줄 간격 설정하기 : timedelta(표준 라이브러리인 datatime 모듈에 포함된)인스터스를 사용하면 됨
- execution_date : DAG가 실행되는 날짜와 시간을 나타냄
  - DAG를 시작하는 시간의 특정 날짜가 아니라 스케줄 간격으로 실행되는 시작 시간을 나타내는 타임스탬프
  - 스케줄 간격의 종료 시간은 next_execution_date라는 매개변수를 사용
  - 과거의 스케줄 간격의 시작을 정의하는 previous_execution_date 매개변수를 제공
- Airflow는 날짜 시간에 Pendulum 라이브러리를 사용하며 execution_date는 이러한 Pendulum의 datetime 객체

------

## **태스크 간 의존성 정의**

- XCom : DAG 실행에서 서로 다른 작업 간에 데이터를 전달할 수 있음
- 다양한 태스크 의존성 패턴
  - 태스크의 선형 체인(linear chain) 유형 : 연속적으로 실행되는 작업
  - 팬아웃/팬인(fan-out/fan-in) 유형 : 하나의 태스크가 여러 다운스트림 태스크에 연결되거나 그 반대의 동작을 수행하는 유형
    - 팬아웃 : 여러 개의 입력 태스크 연결수 제한
    - 팬아웃 종속성 : 한 태스크를 여러 다운스트림 태스크에 연결하는 것
    - 팬인 구조 : 하나의 태스크가 여러 업스트림 태스크에 영향을 받는 구조는 단일 다운스트림 태스크가 여러 업스트림 태스크에 의존성을 가짐
      - [a , b ] >> c

------

## **워크플로 트리거**

- 센서를 사용한 폴링 조건
  - Airflow 오퍼레이터의 특수 타입(서브 클래스)인 센서(sensor)의 도움을 받을 수 있음
- DAG 간의 공유 종속성에 대한 필요성을 감안할 때 에어플로우 작업은 Sensor라고 하는 특별한 유형의 연산자를 구현할 수 있다, 에어플로우 Sensor는 일부 외부 작업 또는 프로세스의 상태를 확인한 다음 확인 기준이 충족되면 DAG에서 다운스트림 종속성을 계속 실행하도록 설계됨
- 두 개의 서로 다른 에어플로우 DAG를 조정해야 하는 경우 ExternalTaskSensor를 사용하여 다른 DAG의 작업 상태 또는 다른 DAG의 전체 상태를 확인할 수 있음
  - 센서는 특정 조건이 true인지 지속적으로 확인하고 true라면 성공, 만약 false인 경우 센서는 상태가 true가 될 때까지 또는 타임아웃이 될 때까지 계속 확인함
  - FileSensor : 파일위치에 파일이 존재하는지 확인하고 파일이 있으면 true를 반환하고, 그렇지 않으면 false를 반환한 후 해당 센서는 지정된 시간(기본값은 60초) 동안 대기했다가 다시 시도함
  - Poking : 센서를 실행하고 센서 상태를 확인하기 위해 Airflow에서 사용하는 이름
  - 사용자 지정 조건 폴링
    - PythonSensor
      - PythonOperator와 유사하게 파이썬 콜러블(callable 함수, 메서드 등)을 지원
      - PythonSensor 콜러블은 성공적으로 조건이 충족됐을 경우 true를, 실패했을 경우 false로 부울(Boolean) 값을 반환하는 것으로 제한됨
  - 센서 데드록 : 실해중인 태스크 조건이 true가 될 때까지 다른 태스크가 대기하게 되므로 모든 슬롯이 데드록 상태가 됨
  - TriggerDagRunOperator : 워크플로가 분리된 경우 이 오퍼레이터를 통해 다른 DAG를 트리거할 수 있음
    - DAG에서 태스크를 삭제하면 이전에 트리거 된 해당 DAG 실행을 지우는 대신에 새 DAG 실행이 트리거 됨
- 다른 DAG의 상태를 폴링 하기
  - ExternalTaskSensor : 다른 DAG의 태스크를 지정하여 해당 태스크의 상태를 확인하는 것





## **Concept**

### **DAG**(Directed Acyclic Graph)





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



## AIrflow 구성



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

