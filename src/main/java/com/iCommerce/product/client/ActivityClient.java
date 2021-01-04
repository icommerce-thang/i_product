package com.iCommerce.product.client;

import com.iCommerce.i_common.payload.ActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "activity", fallback = ActivityClient.ActivityClientFallback.class)
public interface ActivityClient {

    @PostMapping("/")
    void submitActivity(ActivityDto activityDto, @RequestHeader("Authorization") String bearToken);

    @Component
    class ActivityClientFallback implements ActivityClient {

        @Override
        public void submitActivity(ActivityDto activityDto, String bearToken) {
            return;
        }
    }
}
