package com.atlas.dscatalog.tests;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.entities.Category;
import com.atlas.dscatalog.entities.Product;
import com.atlas.dscatalog.repositories.CategoryRepository;

import java.time.Instant;

public class Factory {

    private CategoryRepository categoryRepository;
    public static Product createProduct(){
        Product product = new Product(1L,"Phone","New Phone", 800.0, "img.com", Instant.parse("2022-05-06T13:56:25Z"));
        product.getCategories().add(new Category(1L, "Electronics"));
        return product;
    }
    public static Category createCategory(){
        return new Category(1L,"Electronics");
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
