package com.iCommerce.product.event.handler;

import com.iCommerce.i_common.payload.ActivityDto;
import com.iCommerce.product.client.ActivityClient;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class ProductActivityEventHandler {

    @Qualifier("activity")
    private final ActivityClient activityClient;

    @EventListener
    @Async("asyncTaskExecutor")
    public void onProductActivity(ActivityDto event) {
        log.debug("onProductActivity {}", event);
        try {
            activityClient.submitActivity(event, event.getToken());
        } catch (Exception e) {
            log.error(e);
        }
    }
}
