package com.iCommerce.product.service;

import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.i_common.service.GeneralService;
import com.iCommerce.product.model.Product;

import java.util.List;

public interface ProductService extends GeneralService<Product>{

    List<Product> findAll(ProductSearchDto dto);
}
