package com.iCommerce.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart", path = "/cart")
public interface CartClient {

    @GetMapping("/product/count/{id}")
    int countProductSold(@PathVariable("id") int productId);
}
