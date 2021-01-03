package com.iCommerce.product.service.impl;

import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.product.config.TestAuditingConfiguration;
import com.iCommerce.product.model.Product;
import com.iCommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Profile("test")
@SpringBootTest
@Import(TestAuditingConfiguration.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository repository;

    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() {
        productService = new ProductServiceImpl(repository);
        MockitoAnnotations.initMocks(this);

        Product prd1 = new Product();
        prd1.setName("prd1");
        prd1.setPrice(BigDecimal.valueOf(10));
        prd1.setColor("color1");
        prd1.setBrand("brand1");
        productService.save(prd1);

        Product prd2 = new Product();
        prd2.setName("prd2");
        prd2.setPrice(BigDecimal.valueOf(20));
        prd2.setColor("color1");
        prd2.setBrand("brand2");
        productService.save(prd2);

        Product prd3 = new Product();
        prd3.setName("prd3");
        prd3.setPrice(BigDecimal.valueOf(30));
        prd3.setColor("color2");
        prd3.setBrand("brand1");
        productService.save(prd3);

        Product prd4 = new Product();
        prd4.setName("prd4");
        prd4.setPrice(BigDecimal.valueOf(40));
        prd4.setColor("color2");
        prd4.setBrand("brand2");
        productService.save(prd4);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    public void test_findAll_with_empty_search_condition_return_4() {
        ProductSearchDto searchDto = new ProductSearchDto();
        List<Product> list = productService.findAll(searchDto);
        assertEquals(4, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1")),
                hasProperty("name", is("prd2")),
                hasProperty("name", is("prd3")),
                hasProperty("name", is("prd4"))
        ));
    }

    @Test
    public void test_findAll_match_name_like() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("prd");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(4, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1")),
                hasProperty("name", is("prd2")),
                hasProperty("name", is("prd3")),
                hasProperty("name", is("prd4"))
        ));
    }

    @Test
    public void test_findAll_not_match_name_like() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("not-existed");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(0, list.size());
    }

    @Test
    public void test_findAll_match_name_color() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("prd");
        searchDto.setColor("color1");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(2, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1")),
                hasProperty("name", is("prd2"))
        ));
    }

    @Test
    public void test_findAll_match_name_not_color_return_empty() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("prd");
        searchDto.setColor("color3");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(0, list.size());
    }

    @Test
    public void test_findAll_match_name_brand() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("prd");
        searchDto.setColor("color1");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(2, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1")),
                hasProperty("name", is("prd2"))
        ));
    }

    @Test
    public void test_findAll_match_color_brand() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setColor("color1");
        searchDto.setBrand("brand1");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(1, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1"))
        ));
    }

    @Test
    public void test_findAll_match_name_color_brand() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setName("prd");
        searchDto.setColor("color1");
        searchDto.setBrand("brand1");
        List<Product> list = productService.findAll(searchDto);
        assertEquals(1, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1"))
        ));
    }

    @Test
    public void test_findAll_price_less_all_return_0() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setPriceFrom(0);
        searchDto.setPriceTo(5);
        List<Product> list = productService.findAll(searchDto);
        assertEquals(0, list.size());
    }

    @Test
    public void test_findAll_price_over_all_return_0() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setPriceFrom(50);
        searchDto.setPriceTo(100);
        List<Product> list = productService.findAll(searchDto);
        assertEquals(0, list.size());
    }

    @Test
    public void test_findAll_price_match_range_return_2() {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setPriceFrom(10);
        searchDto.setPriceTo(25);
        List<Product> list = productService.findAll(searchDto);
        assertEquals(2, list.size());
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prd1")),
                hasProperty("name", is("prd2"))
        ));
    }
}
