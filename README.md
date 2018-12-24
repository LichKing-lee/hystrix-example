- Netflix Hystrix 예제 코드
- 블로그: http://multifrontgarden.tistory.com/238

|||
|---|---|
|supplier|데이터제공 API|
|consumer|supplier 를 호출하는 클라이언트|
|monitor|consumer에 적용된 hystrix를 모니터링하는 모듈|
|eureka|유레카 클라이언트들을 관리하는 유레카 서버|
|turbine|유레카 서버로부터 클라이언트 목록을 받아와 hystrix stream을 집계해서 클러스터링해주는 모듈|