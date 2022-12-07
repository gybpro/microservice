# microservice

#### 介绍

微服务学习

## 微服务

- 微服务是一种思想，微服务是为了满足业务功能越来越复杂的需求，将单一服务拆分成多个服务的一种设计思想。
- 微服务目前并没有严格统一的定义，但微服务一词的发明者Martin Fowler对微服务的定义具有指导意义：
  - 简而言之，微服务架构的风格，就是将单一程序开发成一个微服务，每个微服务运行在自己的进程中，并使用**轻量级机制通信**
    ，通常是HTTP RESTFUL API。这些服务围绕业务能力来划分构建的，并通过完全自动化部署机制来独立部署这些服务可以使用不同的编程语言，以及不同数据存储技术，以保证最低限度的集中式管理。

### SpringCloud

- Spring Cloud作为Java语言的微服务框架，它依赖于Spring Boot，具有快速开发、持续交付和容易部署等特点。Spring
  Cloud的首要目标就是通过提供一系列开发组件和框架，帮助开发者搭建一个分布式的微服务系统。Spring
  Cloud组件众多，包括服务注册发现中心、配置中心、熔断器、远程调用、智能路由、微代理、控制总线、全局锁、分布式会话等。
-

SpringCloud和SpringCloudAlibaba都是基于SpringBoot构建，所以SpringBoot和SpringCloud、SpringCloudAlibaba之间的版本关系十分重要，如果不进行版本控制，那么可能会出现许多难以查找的错误。版本控制环境搭建如下：

  ```xml
  <properties>
      <maven.compiler.source>11</maven.compiler.source>
      <maven.compiler.target>11</maven.compiler.target>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
      <maven-compiler-plugin.version>3.8.1</maven-compiler-plugin.version>
      <spring-boot.version>2.3.12.RELEASE</spring-boot.version>
      <spring-cloud.version>Hoxton.SR12</spring-cloud.version>
      <spring-cloud-alibaba.version>2.2.8.RELEASE</spring-cloud-alibaba.version>
      <mybatis-plus.version>3.4.2</mybatis-plus.version>
  </properties>
  ```

#### Eureka

-

Eureka一词源于古希腊，意为”发现了“。在软件邻域，Eureka是Netflix公司开源的一个服务注册发现组件，与其他Netflix公司的服务组件（如负载均衡、熔断器、网关等）一起，被SpringCloud社区整合为SpringCloudNetflix模块。

- CAP原则/定理：在分布式系统中，CAP三要素最多只能同时实现两点，不可能三者兼顾
  - 一致性（Consistency）
  - 可用性（Availability）
  - 分区容错性（Partition tolerance）
- Eureka与Zookeeper：
  - Zookeeper更注重数据的一致性，CP（zk是注册中心，配置中心，协调中心）
  - Eureka更注重服务的可用性，AP（eureka是注册中心）
