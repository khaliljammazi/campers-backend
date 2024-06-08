package com.campers.now;

import com.campers.now.controllers.ProductController;
import com.campers.now.models.Product;
import com.campers.now.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
 class ProductTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(productController).build();
    }

    @Test
    void testAddProduct() throws Exception {
        Product product = new Product();

        when(productService.add(any(Product.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
     void testUpdateProduct() throws Exception {
        Product product = new Product(); // create a product instance

        when(productService.update(1, product)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
     void testGetOneProduct() throws Exception {
        Product product = new Product();

        when(productService.getById(1)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
     void testGetAllProducts() throws Exception {
        when(productService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
     void testDeleteProduct() throws Exception {
        Product product = new Product();

        when(productService.delete(1)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
