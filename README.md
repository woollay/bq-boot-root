# bq-boot-root组件的使用说明
- 本组件基于springboot二次封装，主要是为了简化和固化常规的业务场景，当然也支持灵活的配置扩展：
- 本组件引入方法：
    ```xml
    <dependency>
        <groupId>com.biuqu</groupId>
        <artifactId>bq-boot-root</artifactId>
        <version>1.0.2</version>
    </dependency>
    ```

## 1. 为什么要写bq-boot-root组件

- SpringBoot大体上有2种类型的类型的服务（SpringBoot中内聚Web容器）：一种是基于Servlet的，另一种是基于Netty的；前者以BIO为主，后者以NIO为主。前者比较典型的例子是基于Tomcat的SpringBoot服务（大多数服务属于此类），后者比较典型的是SpringCloud-Gateway。当然二者的分界也并不是绝对的。
- `bq-boot-root`就是为了兼容如上2种场景而存在的基础服务依赖jar包，因为上述2种服务都有部分相同的需求，比如对配置文件加密、redis配置注入、 定时器配置注入、Http负载均衡注入、断路器注入等，避免代码重复或者臃肿；
- "一千个人就有一千个哈姆雷特"，本人在从业过程中，非常注意这种实战经验的积累，因此也沉淀出了这个非常基础的仅依赖SpringBoot的代码框架,算作抱砖引玉吧，期望大家能够一起把基础框架做得更精致，以便让更多的人即学即用，如有深入研究的兴趣，还可以翻看源码和文档；

## 2. 使用bq-boot-root组件有什么好处

- 闭环了加密机的自动注入，自动实现了使用加密机对Jasypt秘钥加密，再使用Jasypt对配置文件加密；
- 闭环了安全加密器的自动注入，可支持对接口加密和内部交互接口跳过加密，既保证安全又兼顾效率；
- 自动注入了自定义的带负载均衡的Http客户端；
- 自动注入了池化的线程池；
- 分别实现了基于eureka/nacos的服务注册服务发现，当下默认为nacos，可非常方便的切换成eureka；
- 支持Sentinel和CircuitBreaker服务降级，当下默认为Sentinel；

## 3. bq-boot-root最佳实践
- bq-boot-root最佳实践是配合SpringCloud-Gateway/SpringBoot一起使用，bq-boot-root中的能力在[bq-service-gateway](https://github.com/woollay/bq-service-gateway) 和[bq-boot-base](https://github.com/woollay/bq-boot-base) 中有更好的体现；


