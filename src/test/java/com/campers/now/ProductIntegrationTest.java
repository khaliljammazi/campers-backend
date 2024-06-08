package com.campers.now;

import com.campers.now.models.Product;
import com.campers.now.models.enums.VendingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
 class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
     void testCreateAndGetProduct() throws Exception {
        Product product = new Product();
        product.setMatricule("PROD001");
        product.setName("Sample Product");
        product.setDescription("This is a sample product.");
        product.setImage("https://example.com/sample-product-image.jpg");
        product.setDiscount(10);
        product.setPrice(99.99F);
        product.setStock(100);
        product.setVendingType(VendingType.valueOf("AVENDRE"));
        product.setActive(true);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Sample Product"));
    }
}
