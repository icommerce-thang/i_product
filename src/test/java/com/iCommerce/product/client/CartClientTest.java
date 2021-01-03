package com.iCommerce.product.client;

import com.iCommerce.product.config.ApplicationSecurity;
import com.iCommerce.product.config.FakeCartClientRestService;
import com.iCommerce.product.config.FakeFeignConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = {
                FakeFeignConfiguration.class,
                FakeCartClientRestService.class,
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
}
