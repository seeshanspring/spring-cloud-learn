package io.seeshan.eureka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EurekaServiceProviderApplication {

    @Value("${server.port}")
    private int port;

    @RequestMapping("/")
    public String home() {
        return "Hello EurekaServiceProviderApplication on port: " + port;
    }

    @RequestMapping("/ping")
    public String ping() {
        return "pong on port: " + port;
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceProviderApplication.class, args);
    }

}
