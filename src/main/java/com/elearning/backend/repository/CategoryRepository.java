package com.elearning.backend.repository;

import com.elearning.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

///
/// CategoryRepository
/// params: None
/// return: None
/// description: This interface extends JpaRepository to provide CRUD operations for Category entities.
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
