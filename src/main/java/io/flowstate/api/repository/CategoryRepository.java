package io.flowstate.api.repository;

import io.flowstate.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserId(UUID userId);
    boolean existsByNameAndUserId(String name, UUID userId);
}
