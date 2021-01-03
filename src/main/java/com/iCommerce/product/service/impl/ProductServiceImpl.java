package com.iCommerce.product.service.impl;

import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.product.repository.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.iCommerce.i_common.service.impl.GeneralServiceImpl;
import com.iCommerce.product.model.Product;
import com.iCommerce.product.repository.ProductRepository;
import com.iCommerce.product.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl extends GeneralServiceImpl<Product> implements ProductService {

	private final ProductRepository repository;

	public ProductServiceImpl(ProductRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<Product> findAll(ProductSearchDto dto) {
		return repository.findAll(ProductSpecification.build(dto));
	}
}
