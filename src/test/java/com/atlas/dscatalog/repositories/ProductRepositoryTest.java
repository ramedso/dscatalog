package com.atlas.dscatalog.repositories;

import com.atlas.dscatalog.entities.Product;
import com.atlas.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest

public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);

        Optional<Product> r = repository.findById(existingId);

        Assertions.assertFalse(r.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);
        Optional<Product> result = repository.findById(product.getId());

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnOptionalProductNotEmptyWhenIdExists(){

        Optional<Product> r = repository.findById(existingId);

        Assertions.assertTrue(r.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalProductEmptyWhenIdDoesNotExist(){

        Optional<Product> r = repository.findById(nonExistingId);

        Assertions.assertTrue(r.isEmpty());
    }
}
