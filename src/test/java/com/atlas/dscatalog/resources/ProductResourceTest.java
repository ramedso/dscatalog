package com.atlas.dscatalog.resources;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.entities.Product;
import com.atlas.dscatalog.servicies.ProductService;
import com.atlas.dscatalog.servicies.exceptions.ResourceNotFoundException;
import com.atlas.dscatalog.tests.Factory;
import com.atlas.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;
    private Product product;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existingId;
    private Long nonExistingId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long dependentId;
    private String username;
    private String password;

    @Autowired
    private TokenUtil tokenUtil;

    @BeforeEach
    @SneakyThrows
    void setUp(){
        username = "maria@gmail.com";
        password = "123456";

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(productService.findAllPaged(any(), any(), any())).thenReturn(page);

        when(productService.findById(existingId)).thenReturn(productDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(productService.update(eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(productService.insert(any())).thenReturn(productDTO);

        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(productService).delete(dependentId);

    }

    @Test
    @SneakyThrows
    public void findAllShouldReturnPage() {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId)
                .accept(APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }
    @Test
    @SneakyThrows
    public void findByIdShouldThrowExceptionWhenIdNotExists(){
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void updateShouldReturnProductDtoWhenIdExists(){
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        );

        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }

    @Test
    @SneakyThrows
    public void updateShouldNotOFoundWhenIdNotExists(){
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void insertShouldReturnCreated(){
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = objectMapper.writeValueAsString(product);

        ResultActions resultActions =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                );

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }

    @Test
    @SneakyThrows
    public void deleteShouldReturnNoContentWhenIdExists(){

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void deleteShouldReturnNotFoundWhenIdNotExists(){

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken));

        resultActions.andExpect(status().isNotFound());
    }
}