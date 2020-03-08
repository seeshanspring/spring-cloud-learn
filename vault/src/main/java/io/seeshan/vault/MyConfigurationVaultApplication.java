package io.seeshan.vault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MyConfiguration.class)
public class MyConfigurationVaultApplication implements CommandLineRunner {

    private final MyConfiguration configuration;

    public MyConfigurationVaultApplication(MyConfiguration configuration) {
        this.configuration = configuration;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyConfigurationVaultApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Logger logger = LoggerFactory.getLogger(MyConfigurationVaultApplication.class);

        logger.info("----------------------------------------");
        logger.info("Configuration properties");
        logger.info("   example.username is {}", configuration.getUsername());
        logger.info("   example.password is {}", configuration.getPassword());
        logger.info("----------------------------------------");
    }
}
