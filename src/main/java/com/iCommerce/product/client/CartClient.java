package com.iCommerce.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart", fallback = CartClient.CartClientFallback.class)
public interface CartClient {

    @GetMapping("/product/count/{id}")
    int countProductSold(@PathVariable("id") int productId);

    @Component
    class CartClientFallback implements CartClient{

        @Override
        public int countProductSold(int productId) {
            return 0;
        }
    }
}
