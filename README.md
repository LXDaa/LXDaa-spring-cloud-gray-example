# spring-cloud-gray-example

#### 介绍
Spring Cloud项目使用Nacos+负载均衡器实现灰度发布，使用核心组件注册中心：
- 注册中心：Nacos
- 网关：SpringCloudGateway
- 负载均衡器：Ribbon （使用SpringCloudLoadBalancer实现也是类似的）
- 服务间RPC调用：OpenFeign

#### 代码设计结构
- spring-cloud-gray-example  // 父工程
    - common // 项目公共模块
    - gateway // 微服务网关
    - order // 订单模块
        - order-app // 订单业务服务
    - starter // 自定义springboot starter模块
        - spring-cloud-starter-gray // 灰度发布starter包 (核心代码都在这里)
    - user // 用户模块
        - user-app // 用户业务服务
        - user-client // 用户client（FeignClient和DTO）