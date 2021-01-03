package com.iCommerce.product.config;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

public class FakeRibbonConfiguration {

    @LocalServerPort
    int port;

    @Bean
    public ServerList<Server> serverList() {
        return new StaticServerList<>(new Server("localhost", port));
    }
}
