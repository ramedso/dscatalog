package com.atlas.dscatalog.dto;

import com.atlas.dscatalog.entities.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class CategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5576335041391555097L;

    private Long id;
    private String name;

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
