package com.iCommerce.product.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cart")
public class FakeCartClientRestService {

    @GetMapping("/product/count/{id}")
    public int countProductSold(@PathVariable("id") int productId) {
        return productId + 5;
    }
}
