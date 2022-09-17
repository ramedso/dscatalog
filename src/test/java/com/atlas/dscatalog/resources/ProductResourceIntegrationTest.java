package com.atlas.dscatalog.resources;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    @SneakyThrows
    public void findAllShouldReturnSortedPageWhenSortByName() {
        ResultActions resultActions = mockMvc.perform(
                get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        resultActions.andExpect(jsonPath("$.pageable.sort.sorted").value(true));
        resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        resultActions.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        resultActions.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

    }

    @Test
    @SneakyThrows
    public void updateShouldReturnProductDtoWhenIdExists() {

        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();

        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").value(expectedName));
    }

    @Test
    @SneakyThrows
    public void updateShouldReturnNotFoundWhenIdNotExists() {

        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        resultActions.andExpect(status().isNotFound());
    }
}