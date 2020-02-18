package io.seeshan.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GatewayApplication {

    @RequestMapping("/")
    public String home() {
        return "Hello GatewayApplication";
    }

    @RequestMapping("/foo")
    public String foo(@RequestParam(defaultValue = "foo") String foo) {
        return "Hello " + foo;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // @formatter:off
        return builder.routes()
                .route("baidu2", r -> r.path("/baidu2")
                        .uri("https://www.baidu.com/"))
                .route(r -> r.path("/service-provider2/**")
                         .filters(f -> f.stripPrefix(2)
                                        .addResponseHeader("X-Response-Hello", "World2"))
                         .uri("lb://spring-cloud-eureka-service-provider")
                         .order(0)
                         .id("service-provider2"))
                .build();
        // @formatter:on
    }

}
