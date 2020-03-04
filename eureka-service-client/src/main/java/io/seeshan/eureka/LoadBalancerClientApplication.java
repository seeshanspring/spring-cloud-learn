package io.seeshan.eureka;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
public class LoadBalancerClientApplication {

    @Autowired
    private ClientService2 clientService2;

    @Bean
    public RestTemplate rest(RestTemplateBuilder builder) {
        return builder.build();
    }

    @RequestMapping("/ping2")
    public String ping2() throws Exception {
        return clientService2.ping();
    }

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerClientApplication.class, args);
    }
}

@Service
class ClientService2 {

    @Autowired
    private LoadBalancerClient loadBalancer;

    private final RestTemplate restTemplate;

    public ClientService2(RestTemplate rest, LoadBalancerClient loadBalancer) {
        this.restTemplate = rest;
        this.loadBalancer = loadBalancer;
    }

    public URI serviceInstance() throws Exception {
        return loadBalancer.choose(EurekaServiceClientApplication.SERVICE_PROVIDER).getUri();
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
