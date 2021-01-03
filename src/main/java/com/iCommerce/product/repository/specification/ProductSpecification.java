package com.iCommerce.product.repository.specification;

import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.product.model.Product;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private static Specification<Product> hasNameLike(String name) {
        return ((prod, query, cb) -> cb.like(prod.get("name"), "%" + name + "%"));
    }

    private static Specification<Product> hasPrice(double priceFrom, double priceTo) {
        return ((prod, query, cb) -> cb.between(prod.get("price"), priceFrom, priceTo));
    }

    private static Specification<Product> hasBrand(String brand) {
        return ((prod, query, cb) -> cb.equal(prod.get("brand"), brand));
    }

    private static Specification<Product> hasColor(String color) {
        return ((prod, query, cb) -> cb.equal(prod.get("color"), color));
    }

    public static Specification<Product> build(ProductSearchDto dto) {
        Specification<Product> specification = null;

        List<Specification<Product>> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getName())) {
            list.add(hasNameLike(dto.getName()));
        }
        if (dto.getPriceTo() > 0) {
            list.add(hasPrice(dto.getPriceFrom(), dto.getPriceTo()));
        }
        if (StringUtils.isNotEmpty(dto.getBrand())) {
            list.add(hasBrand(dto.getBrand()));
        }
        if (StringUtils.isNotEmpty(dto.getColor())) {
            list.add(hasColor(dto.getColor()));
        }
        if (list.isEmpty()) {
            return null;
        }

        specification = Specification.where(list.get(0));
        if (list.size() == 1) {
            return specification;
        }
        for(int i = 1; i < list.size(); i++) {
            specification = specification.and(list.get(i));
        }

        return specification;
    }
}
