package io.seeshan.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableCircuitBreaker
@EnableFeignClients
@EnableHystrixDashboard
public class EurekaServiceClientApplication {

    static final String SERVICE_PROVIDER = "spring-cloud-eureka-service-provider";

    @Value("${server.port}")
    private int port;

    @RequestMapping("/")
    public String home() {
        return "Hello EurekaServiceClientApplication on port: " + port;
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceClientApplication.class, args);
    }

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Ribbon Load Balance to Eureka service provider
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "ribbonCallCallback")
    @GetMapping(value = "/ribbon/callProvider")
    public String ribbonCall() {
        return restTemplate.getForEntity("http://" + SERVICE_PROVIDER + "/ping", String.class).getBody();
    }

    public String ribbonCallCallback() {
        return "Ribbon + hystrix, 提供者服务挂了";
    }

    // Feign Load Balance to Eureka service provider
    @Autowired
    private FeignClientInterface feignClientInterface;

    @GetMapping(value = "/feign/callProvider")
    public String feignCall() {
        return feignClientInterface.ping();
    }

    /**
     * 描述: 指定这个接口所要调用的 提供者服务名称 "spring-cloud-eureka-service-provide"
     **/
    @FeignClient(value = SERVICE_PROVIDER, fallback = FeignClientFallback.class)
    public interface FeignClientInterface {

        @GetMapping("/ping")
        String ping();

    }

    @Component
    class FeignClientFallback implements FeignClientInterface {

        @Override
        public String ping() {
            return "Feign + hystrix, 提供者服务挂了";
        }
    }

}
