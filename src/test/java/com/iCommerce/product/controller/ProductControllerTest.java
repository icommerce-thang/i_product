package com.iCommerce.product.controller;

import com.iCommerce.i_common.model.enums.UserAction;
import com.iCommerce.i_common.payload.ActivityDto;
import com.iCommerce.i_common.payload.ProductDto;
import com.iCommerce.i_common.payload.ProductSearchDto;
import com.iCommerce.product.client.CartClient;
import com.iCommerce.product.model.Product;
import com.iCommerce.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.iCommerce.i_common.util.Utils.fromClassToString;
import static com.iCommerce.i_common.util.Utils.fromStringToClass;
import static com.iCommerce.product.utils.TestUtils.prepareProduct;
import static com.iCommerce.product.utils.TestUtils.prepareProductList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CartClient cartClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductController productController;

    private static final String PRINCIPAL_NAME = "me";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .build();
    }

    private RequestBuilder buildRequest(HttpMethod method, String url, String... content) {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("me");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .request(method, url)
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method)) {
            requestBuilder.content(content[0]);
        }

        return requestBuilder;
    }

    @Test
    public void test_findAll_have_record_return_200_and_send_log() throws Exception {
        // given
        List<Product> findAllResult = prepareProductList(2);
        ProductSearchDto productSearchDto = new ProductSearchDto();
        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_LIST);
        activityDto.setValue(productSearchDto.toString());

        // when
        when(productService.findAll(productSearchDto)).thenReturn(findAllResult);

        // execute
        MvcResult resultActions = mockMvc.perform(buildRequest(HttpMethod.GET, "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // verify
        String strResponse = resultActions.getResponse().getContentAsString();
        List<ProductDto> list = Arrays.asList(fromStringToClass(strResponse, ProductDto[].class));
        assertThat(list, containsInAnyOrder(
                hasProperty("name", is("prod_1")),
                hasProperty("name", is("prod_2"))
        ));
        verify(productService, times(1)).findAll(any(ProductSearchDto.class));
        verify(cartClient, times(1)).countProductSold(1);
        verify(cartClient, times(1)).countProductSold(2);
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void test_findAll_empty_record_return_200_and_send_log() throws Exception {
        // given
        List<Product> findAllResult = prepareProductList(0);
        ProductSearchDto productSearchDto = new ProductSearchDto();
        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_LIST);
        activityDto.setValue(productSearchDto.toString());

        // when
        when(productService.findAll(productSearchDto))
                .thenReturn(findAllResult);

        // execute
        MvcResult resultActions = mockMvc.perform(buildRequest(HttpMethod.GET, "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // verify
        String strResponse = resultActions.getResponse().getContentAsString();
        List<ProductDto> list = Arrays.asList(fromStringToClass(strResponse, ProductDto[].class));
        assertThat(list.size(), is(0));
        verify(productService, times(1)).findAll(any(ProductSearchDto.class));
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verifyNoInteractions(cartClient);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void test_findById_return_product_found() throws Exception {
        // given
        Product result = prepareProduct(5);
        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_DETAIL);
        activityDto.setValue("5");

        // when
        when(productService.findById(5)).thenReturn(Optional.of(result));

        // execute
        MvcResult resultActions = mockMvc.perform(buildRequest(HttpMethod.GET, "/" + 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // verify
        String strResponse = resultActions.getResponse().getContentAsString();
        ProductDto productDto = fromStringToClass(strResponse, ProductDto.class);
        assertEquals(productDto, Product.toDto(result));
        verify(productService, times(1)).findById(5);
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verify(cartClient, times(1)).countProductSold(5);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void test_findById_return_404_product_not_found() throws Exception {
        // given
        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_DETAIL);
        activityDto.setValue("5");

        // when
        when(productService.findById(5)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(buildRequest(HttpMethod.GET, "/" + 5))
                .andExpect(status().isNotFound())
                .andReturn();

        // verify
        verify(productService, times(1)).findById(5);
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verifyNoInteractions(cartClient);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);

    }

    @Test
    public void test_updatePrice_return_product_found() throws Exception {
        // given
        Product result = prepareProduct(5);
        ProductDto payload = Product.toDto(result);
        payload.setPrice(100);

        Product updatedProduct = prepareProduct(5);
        updatedProduct.setPrice(BigDecimal.valueOf(100d));

        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_UPDATE);
        activityDto.setValue("update " + payload.getId() + " to " + payload.getPrice());

        // when
        when(productService.findById(5)).thenReturn(Optional.of(result));

        // execute
        mockMvc.perform(buildRequest(HttpMethod.PUT, "/" + 5, fromClassToString(payload)))
                .andExpect(status().isOk())
                .andReturn();

        // verify
        verify(productService, times(1)).findById(5);
        verify(productService, times(1)).save(updatedProduct);
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void test_updatePrice_return_404_product_not_found() throws Exception {
        // given
        Product result = prepareProduct(5);
        ProductDto payload = Product.toDto(result);

        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor(PRINCIPAL_NAME);
        activityDto.setKey("product");
        activityDto.setAction(UserAction.PRODUCT_UPDATE);
        activityDto.setValue("update " + payload.getId() + " to " + payload.getPrice());

        // when
        when(productService.findById(5)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(buildRequest(HttpMethod.PUT, "/" + 5, fromClassToString(result)))
                .andExpect(status().isNotFound())
                .andReturn();

        // verify
        verify(productService, times(1)).findById(5);
        verify(productService, times(0)).save(any(Product.class));
        verify(eventPublisher, times(1)).publishEvent(activityDto);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(eventPublisher);
    }
}
