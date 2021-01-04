package com.iCommerce.product.client;

import com.iCommerce.i_common.payload.ActivityDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = {
                ActivityClientTest.FeignConfiguration.class,
                ApplicationSecurity.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityClientTest {

    @Autowired
    private ActivityClient client;

    @Test
    public void test_activityClient_invoke_success() {
        boolean status = false;
        try {
            client.submitActivity(new ActivityDto(), "test-token");
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        assertEquals(true, status);
    }

    @EnableFeignClients(clients = ActivityClient.class)
    @EnableAutoConfiguration
    @RibbonClient(name = "activity", configuration = FakeRibbonConfiguration.class)
    @RestController
    static class FeignConfiguration {

        @PostMapping("/")
        void submitActivity(ActivityDto activityDto, @RequestHeader("Authorization") String bearToken) {
            return;
        }
    }
}
