package io.seeshan.springcloudconfigclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringCloudConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientApplication.class, args);
    }

}

@RestController
@RefreshScope
class ContentRestController {

    @Value("${content}")
    String content;

    @RequestMapping("/")
    public String home() {
        return "Hello SpringCloudConfigClientApplication!\nContent: " + content;
    }

}
