package com.iCommerce.product.client;

import com.iCommerce.i_common.payload.ActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("activity")
public interface ActivityClient {

    @PostMapping("/")
    void submitActivity(ActivityDto activityDto);
}
