package com.atlas.dscatalog.servicies;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.entities.Category;
import com.atlas.dscatalog.entities.Product;
import com.atlas.dscatalog.repositories.CategoryRepository;
import com.atlas.dscatalog.repositories.ProductRepository;
import com.atlas.dscatalog.servicies.exceptions.DatabaseException;
import com.atlas.dscatalog.servicies.exceptions.ResourceNotFoundException;
import com.atlas.dscatalog.tests.Factory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingID;
    private long nonExistingID;
    private long dependentID;

    private ProductDTO productDTO;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    @BeforeEach
    void beforeEach() {
        existingID = 1L;
        nonExistingID = 2L;
        dependentID = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        when(repository.findAll((Pageable) any())).thenReturn(page);

        when(repository.save(any())).thenReturn(product);

        when(repository.findById(existingID)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingID)).thenReturn(Optional.empty());

        when(repository.find(any(), any(), any())).thenReturn(page);

        when(repository.getById(existingID)).thenReturn(product);
        when(repository.getById(nonExistingID)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getById(existingID)).thenReturn(category);
        when(categoryRepository.getById(nonExistingID)).thenThrow(EntityNotFoundException.class);

        doNothing().when(repository).deleteById(existingID);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingID);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentID);
    }


    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductDTO> result = service.findAllPaged(0L, "", pageable);

        assertNotNull(result);
    }
    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.delete(existingID);
        });

        verify(repository, atLeastOnce()).deleteById(existingID);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
           service.delete(nonExistingID);
        });

        verify(repository, atLeastOnce()).deleteById(nonExistingID);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDepends(){
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentID);
        });

        verify(repository, atLeastOnce()).deleteById(dependentID);
    }

    @Test
    @SneakyThrows
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ProductDTO result = service.findById(existingID);

        assertNotNull(result);
        verify(repository).findById(existingID);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionDtoWhenIdNotExists(){
        assertThrows(ResourceNotFoundException.class, () ->{
            service.findById(nonExistingID);
        });

        verify(repository).findById(nonExistingID);
    }

    @Test
    @SneakyThrows
    public void updateShouldReturnProductDtoWhenIdExists() {
        ProductDTO result = service.update(existingID, productDTO);

        assertNotNull(result);
    }

    @Test
    public void updateIdShouldThrowResourceNotFoundExceptionDtoWhenIdNotExists(){
        assertThrows(ResourceNotFoundException.class, () ->{
            service.update(nonExistingID, productDTO);
        });
    }
}