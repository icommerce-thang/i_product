package com.iCommerce.product.client;

import com.iCommerce.product.config.ApplicationSecurity;
import com.iCommerce.product.config.FakeRibbonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = {
                CartClientTest.FeignConfiguration.class,
                ApplicationSecurity.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartClientTest {

    @Autowired
    private CartClient cartClient;

    @Test
    public void test_cartClient_invoke_success() {
        int count = cartClient.countProductSold(5);
        assertEquals(10, count);
    }

    @EnableFeignClients(clients = CartClient.class)
    @EnableAutoConfiguration
    @RibbonClient(name = "cart", configuration = FakeRibbonConfiguration.class)
    @RestController
    static class FeignConfiguration {
        @GetMapping("/product/count/{id}")
        public int countProductSold(@PathVariable("id") int productId) {
            return productId + 5;
        }
    }
}
