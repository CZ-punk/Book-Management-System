# Book-Management-System


msa 구조를 적용하여 책과 리뷰를 관리하는 시스템 입니다.

Eureka 서버를 이용하여 Config Server 와 Gateway, 그리고 각각의 Service Application Server 를 구성하였습니다.

Config Server 에서는 모든 애플리케이션들의 설정 정보를 통합하여 관리합니다.

각각의 Service Application 들은 Feign Client 로 서로 간의 소통이 가능하도록 하였습니다.

Client 는 Gateway 를 통해서 Service Application 을 호출하게 되고 요청에 따른 응답을 처리합니다.
