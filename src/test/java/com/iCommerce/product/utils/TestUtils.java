package com.iCommerce.product.utils;

import com.iCommerce.product.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static List<Product> prepareProductList(int count) {
        List<Product> list = new ArrayList<>();
        while (count > 0) {
            list.add(prepareProduct(count));
            count--;
        }

        return list;
    }

    public static Product prepareProduct(int id) {
        Product prod = new Product();
        prod.setId(id);
        prod.setName("prod_" + id);
        prod.setBrand("brand_" + id);
        prod.setColor("color_" + id);
        prod.setPrice(BigDecimal.valueOf(10d * id));

        return prod;
    }
}
