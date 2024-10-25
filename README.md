## Logging-Example


### 목표

+ 로그에 traceId와 spanId를 포함하기
+ 로그를 json 형식으로 출력하기
+ GCP 오토 스케일링을 적용하기
+ Cloud Logging을 사용해서 로그를 중앙 집중 관리하기

<br/>

+ 참고 사항 : 클라우드에서는 로그를 전송하기 떄문에 로그 파일 사이즈나 로그 로테이션을 고려하지 않아도 됨

<br>

### 진행 과정

+ [x] 로컬에서 로그를 찍어보기
+ [x] GCE에 애플리케이션을 올려서 Cloud Logging으로 로그가 제대로 전송되는지 확인하기
+ [x] 오토 스케일링을 적용 후 Cloud Logging으로 로그가 제대로 전송되는지 확인하기

<br><br>


### 로그를 json 형식으로 출력하기

+ 로그를 json 형식으로 만들기 위해 build.gradle에 LogstashEncoder를 추가했다.
+ 로컬 환경은 로그가 json 형식으로 나왔다.
+ Cloud Logging에서도 로그가 json 형식으로 나왔다.
+ Cloud Logging에서 찍힌 로그가 로컬과 달라서 혼란을 겪었다.
+ 공식 문서를 보다가 원래 로그가 json 형식으로 찍히는 것을 알게 되었다.
+ LogstashEncoder를 삭제하고 Cloud Logging을 확인했는데 이전과 로그가 똑같이 나왔다.
+ LogstashEncoder는 필요 없는 것으로 판단하고 삭제했다.

#### 교훈 : 처음 기능을 구현할 때는 간단한 예시로 시작하고, 이후에 기능을 추가하면서 점진적으로 개선하기.

<br>

### 로그에 traceId와 spanId를 추가하기
+ build.gradle에 spring-boot-starter-actuator와 micrometer-tracing-bridge-brave 의존성을 추가했다.
+ 위의 의존성으로 MDC에 traceId와 spanId가 자동으로 추가됐다.
+ LoggingAppender는 MDC 값을 자동으로 포함하기에 로그에 traceId와 spanId가 추가되었다.

<br>

#### 예외 로깅 발생 시 겪은 문제

+ 스프링 애플리케이션에서 예외가 발생하면 Cloud Logging에 traceId와 spanId가 포함되지 않았다.
+ 예외 로그에 스택 트레이스까지 전부 포함되서 너무 길다.
+ 문제 원인을 파악하기 위해 몇 가지 시도를 했다.


+ 첫 번째 방법 : LoggingEnhancer 추가하기
  + LoggingAppender를 사용 시 로그에 필드를 추가하는 방법으로 LoggingEnhancer가 있다.
  + LoggingEnhancer를 구현해서 로그에 traceId와 spanId를 추가했지만 실패했다.


+ 두 번째 방법 : Cloud Logging에서 제공하는 추적 기능 확인하기
  + Cloud Logging에서 예외 발생 시 추적 기능을 제공하여 traceId와 spanId를 의도적으로 안 찍을 수도 있다고 생각했다.
  + Cloud Logging에서 요청 흐름을 파악할 수 있는 기능이 있는지 확인했는데 없었다.


+ 세 번째 방법 : 스프링에서 ExceptionHandler를 통해 에러 처리하기
  + 스프링에서 ExceptionHandler를 통해 에러 처리를 하지 않으면 톰캣까지 예외가 전파되고 톰캣에서 기본 예외 처리를 진행한다.
  + 위의 과정에서 요청 처리가 완료된 것으로 간주되기 때문에 MDC에서 traceId와 spanId가 삭제된다.
  + 스프링에서 ExceptionHandler를 추가해서 Cloud Logging에 traceId와 spanId가 포함되게 만들었다.
  + ExceptionHandler에서 필요한 정보만 로깅한다.
  

#### 교훈 : 큰 덩어리로 문제를 바라봐서 원인을 분석하는게 너무 힘들었다. 문제를 분류하고 분석하기.  

<br>

### 프로젝트에서 고려하지 않은 점

+ 로깅에 맥락에 도움되는 정보를 넣지 않았다. 
+ 실제 프로젝트에서는 상황에 맞게 맥락 정보를 추가해야 한다.

<br><br>
