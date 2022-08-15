package com.atlas.dscatalog.repositories;

import com.atlas.dscatalog.dto.CategoryDTO;
import com.atlas.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}