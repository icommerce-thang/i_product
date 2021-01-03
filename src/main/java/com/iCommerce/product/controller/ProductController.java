package com.iCommerce.product.controller;

import com.iCommerce.i_common.controller.AbstractController;
import com.iCommerce.i_common.exception.NotFoundException;
import com.iCommerce.i_common.model.enums.UserAction;
import com.iCommerce.i_common.payload.ProductDto;
import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.product.client.CartClient;
import com.iCommerce.product.model.Product;
import com.iCommerce.product.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Log4j2
public class ProductController extends AbstractController {

    private final ProductService productService;

    private final CartClient cartClient;

    public ProductController(ProductService productService, ApplicationEventPublisher eventPublisher,
                             CartClient cartClient) {
        super(eventPublisher, "product");
        this.productService = productService;
        this.cartClient = cartClient;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> finalAll(ProductSearchDto payload,
                                                     Principal principal) {
        log.debug("Enter findAll product by {}", principal);
        logEvent(principal.getName(), UserAction.PRODUCT_LIST, payload.toString());

        List<ProductDto> items = productService.findAll(payload)
                .stream()
                .map(product -> {
                    ProductDto productDto = Product.toDto(product);
                    updateProductOrderCount(productDto);

                    return productDto;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") int id, Principal principal) {
        log.debug("Enter findById product {}, by {}", id, principal);
        logEvent(principal.getName(), UserAction.PRODUCT_DETAIL, "" + id);

        return productService.findById(id)
                .map(product -> {
                    ProductDto productDto = Product.toDto(product);
                    updateProductOrderCount(productDto);

                    return productDto;
                })
                .map(item -> ResponseEntity.ok().body(item))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") int id,
                                         @RequestBody ProductDto payload, Principal principal) {
        log.debug("updatePrice of {} by {} value {}", id, principal.getName(), payload);
        logEvent(principal.getName(), UserAction.PRODUCT_UPDATE, "update " + payload.getId() + " to " + payload.getPrice());

        Product product = productService.findById(id)
                .orElseThrow(() -> new NotFoundException("Product is not found with id: " + id));
        product.setPrice(BigDecimal.valueOf(payload.getPrice()));
        productService.save(product);

        return ResponseEntity.ok().build();
    }

    private void updateProductOrderCount(ProductDto productDto) {
        int count = cartClient.countProductSold(productDto.getId());
        productDto.setOrdered(count);
    }
}
