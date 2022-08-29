package com.atlas.dscatalog.tests;

import com.atlas.dscatalog.dto.ProductDTO;
import com.atlas.dscatalog.entities.Category;
import com.atlas.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct(){
        Product product = new Product(1L,"Phone","New Phone", 800.0, "img.com", Instant.parse("2022-05-06T13:56:25Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
