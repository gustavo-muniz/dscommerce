package com.munizdev.dscommerce.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munizdev.dscommerce.dto.ProductDTO;
import com.munizdev.dscommerce.dto.ProductMinDTO;
import com.munizdev.dscommerce.factories.Factory;
import com.munizdev.dscommerce.services.ProductService;
import com.munizdev.dscommerce.services.exceptions.DatabaseException;
import com.munizdev.dscommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<ProductMinDTO> page;
    private ProductMinDTO productMinDTO;
    private ProductDTO productDTO;


    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productMinDTO = Factory.createProductMinDTO();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productMinDTO));

        when(service.findAll(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId) , any())).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        this.mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        this.mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        this.mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        this.mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnProductDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        this.mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        this.mockMvc.perform(delete("/products/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/products/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }
}
