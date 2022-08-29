package com.atlas.dscatalog.servicies;

import com.atlas.dscatalog.repositories.ProductRepository;
import com.atlas.dscatalog.servicies.exceptions.DatabaseException;
import com.atlas.dscatalog.servicies.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingID;
    private long nonExistingID;
    private long dependentID;

    @BeforeEach
    void beforeEach() {
        existingID = 1L;
        nonExistingID = 100L;
        dependentID = 4L;

        doNothing().when(repository).deleteById(existingID);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingID);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentID);
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

}