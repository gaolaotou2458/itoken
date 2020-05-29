package com.funtl.itoken.web.posts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.funtl.itoken")
@EnableDiscoveryClient  //服务消费者
@EnableFeignClients     //feign客户端

public class WebPostsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebPostsApplication.class, args);
    }
}
