package com.apbg.webstore.productservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("h2")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/all-products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(12)));
    }

    @Test
    public void getPage() throws Exception {
        mockMvc.perform(get("/products?page=0&size=8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content", hasSize(8)));
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/products/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Blackhawk Backpack")));
    }

    @Test
    public void updatePrices() throws Exception {
        List<Double> prices = new ArrayList<>();
        prices.add(1.0);
        prices.add(2.0);
        prices.add(3.0);
        prices.add(4.0);
        prices.add(5.0);
        prices.add(6.0);
        prices.add(7.0);
        prices.add(8.0);
        prices.add(9.0);
        prices.add(10.0);
        prices.add(11.0);
        prices.add(12.0);

        mockMvc.perform(post("/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(prices)))
                .andExpect(status().isOk());

    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"New Product\"," +
                        "\"image\":\"https://placehold.it/250/250\"," +
                        "\"price\":9.99," +
                        "\"description\":\"Product Description\"" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(13)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(9.99)))
                .andExpect(jsonPath("$.description", is("Product Description")));

    }

    @Test
    public void updateById() throws Exception {
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\":1," +
                        "\"name\":\"48\\\" Umbrella LED Flashlight\"," +
                        "\"image\":\"assets/img/umbrella-led-flashlight.jpeg\"," +
                        "\"price\":14.95," +
                        "\"description\":\"Auto-open umbrella has a pongee polyester shell. Rubber spray handle holds swivel LED flashlight with red hazard light. Fiberglass ribs. Protective sleeve. Batteries included. Ground ship only. 44\\\" arc. Import. Screen Print. Black.\"" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("48\" Umbrella LED Flashlight")));
    }

    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}