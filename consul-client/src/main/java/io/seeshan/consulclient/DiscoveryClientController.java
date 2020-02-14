package io.seeshan.consulclient;

import java.net.URI;
import java.util.Optional;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class DiscoveryClientController {

    public static final String SERVICE_ID = "consul-service-discovery";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancer;

    public Optional<URI> serviceUrl() {
        return discoveryClient.getInstances(SERVICE_ID).stream().map(ServiceInstance::getUri).findFirst();
    }

    @GetMapping("/discoveryService")
    public String discoveryPing() throws RestClientException, ServiceUnavailableException {
        URI service = serviceUrl().map(s -> s.resolve("/ping")).orElseThrow(ServiceUnavailableException::new);
        return restTemplate.getForEntity(service, String.class).getBody();
    }

    /**
     * 获取所有服务
     */
    @RequestMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("consul-service-discovery");
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @RequestMapping("/discover")
    public Object discover() {
        return loadBalancer.choose("consul-service-discovery").getUri().toString();
    }

    @RequestMapping("/callService")
    public String call() {
        ServiceInstance serviceInstance = loadBalancer.choose("consul-service-discovery");
        System.out.println("服务地址：" + serviceInstance.getUri());
        System.out.println("服务名称：" + serviceInstance.getServiceId());

        // call service discovery
        String callServiceResult = restTemplate.getForObject(serviceInstance.getUri().toString() + "/ping",
                String.class);
        System.out.println(callServiceResult);
        return callServiceResult;
    }

}
