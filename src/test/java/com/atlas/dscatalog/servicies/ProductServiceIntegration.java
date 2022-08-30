package com.atlas.dscatalog.servicies;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.repositories.ProductRepository;
import com.atlas.dscatalog.servicies.exceptions.ResourceNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceIntegration {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository repository;
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void beforeEach() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    @SneakyThrows
    public void deleteShouldDeleteResourceWhenIdExists() {

        productService.delete(existingId);

        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    @SneakyThrows
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists() {

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });

    }

    @Test
    @SneakyThrows
    public void findAllPagedShouldReturnPageWhenPageZeroSizeTen() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    @SneakyThrows
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {

        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    @SneakyThrows
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    @SneakyThrows
    public void findAllShouldReturnSortedPageWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}