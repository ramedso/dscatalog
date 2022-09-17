package com.atlas.dscatalog.dto;

import com.atlas.dscatalog.entities.Category;
import com.atlas.dscatalog.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Size(min = 5, max = 60, message = "Name must be between 5 up to 60 chars")
    @NotBlank(message = "Mandatory field: name")
    private String name;
    private String description;
    @Positive(message = "The price must be positive")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "The date cannot be future ")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}
