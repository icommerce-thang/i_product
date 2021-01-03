package com.iCommerce.product.config;

import com.iCommerce.product.client.CartClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = CartClient.class)
@EnableAutoConfiguration
@RibbonClient(
        name = "cart",
        configuration = FakeRibbonConfiguration.class)
public class FakeFeignConfiguration {
}
