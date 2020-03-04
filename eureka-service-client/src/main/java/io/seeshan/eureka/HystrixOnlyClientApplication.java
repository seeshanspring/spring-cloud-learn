package io.seeshan.eureka;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author Johnson Xu(I326232)
 */
@SpringBootApplication
@EnableCircuitBreaker
@RestController
public class HystrixOnlyClientApplication {

    @Autowired
    private ClientService2 clientService;

    @Bean
    public RestTemplate rest(RestTemplateBuilder builder) {
        return builder.build();
    }

    @RequestMapping("/ping")
    public String ping() throws Exception {
        return clientService.ping();
    }

    public static void main(String[] args) {
        SpringApplication.run(HystrixOnlyClientApplication.class, args);
    }

}

@Service
class ClientService {

    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public ClientService(RestTemplate rest, DiscoveryClient discoveryClient) {
        this.restTemplate = rest;
        this.discoveryClient = discoveryClient;
    }

    public URI serviceInstance() throws Exception {
        return discoveryClient.getInstances(EurekaServiceClientApplication.SERVICE_PROVIDER).stream().findAny().map(
                (ServiceInstance::getUri)).orElseThrow(Exception::new);
    }

    @HystrixCommand(fallbackMethod = "reliable")
    public String ping() throws Exception {
        URI uri = serviceInstance().resolve("/ping");

        return restTemplate.getForObject(uri, String.class);
    }

    public String reliable() {
        return "Fallback to reliable!";
    }
}
