## Actuator

[Baeldung Link](https://www.baeldung.com/spring-boot-actuators)

본질적으로, Actuator는 어플리케이션에 생산준비가 된production-ready 기능들을 제공합니다.

이 의존성을 통해 앱 Monitoring, Metrics 수집, traffic 분석, DB 상태 등을 아주 간단히 처리할 수 있습니다.

이 라이브러리의 주요 이점은 이러한 기능을 직접 구현하지 않고도 운영 수준의 도구를 사용할 수 있다는 것입니다.

작동기는 주로 실행 중인 응용 프로그램에 대한 작동 정보(상태, 메트릭, 정보, 덤프, 환경 등)를 노출하는 데 사용됩니다. HTTP 엔드포인트 또는 JMX bean을 사용하여 상호 작용할 수 있습니다.