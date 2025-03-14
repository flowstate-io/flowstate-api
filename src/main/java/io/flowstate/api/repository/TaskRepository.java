package io.flowstate.api.repository;

import io.flowstate.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserId(UUID userId);
    List<Task> findByUserIdAndCompletedOrderByPriorityDesc(UUID userId, boolean completed);
    List<Task> findByUserIdAndCategoryIdOrderByPriorityDesc(UUID userId, UUID categoryId);
}
