package com.iCommerce.product.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.iCommerce.i_common.model.EntityObject;
import com.iCommerce.i_common.payload.ProductDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "product")
@Data
public class Product extends EntityObject {

	private String name;

	private BigDecimal price;

	private String brand;

	private String color;

	public static ProductDto toDto(Product product) {
		ProductDto dto = new ProductDto();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setPrice(product.getPrice().doubleValue());
		dto.setBrand(product.getBrand());
		dto.setColor(product.getColor());

		return dto;
	}
}
