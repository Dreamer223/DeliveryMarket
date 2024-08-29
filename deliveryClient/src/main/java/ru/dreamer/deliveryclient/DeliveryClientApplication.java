package ru.dreamer.deliveryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliveryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryClientApplication.class, args);
    }

}
