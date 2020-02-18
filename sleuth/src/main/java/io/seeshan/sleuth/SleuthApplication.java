package io.seeshan.sleuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SleuthApplication {

    private static Logger log = LoggerFactory.getLogger(SleuthApplication.class);

    @RequestMapping("/")
    public String home() {
        log.info("Handling SleuthApplication home");
        return "Hello SleuthApplication";
    }

    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }

}
