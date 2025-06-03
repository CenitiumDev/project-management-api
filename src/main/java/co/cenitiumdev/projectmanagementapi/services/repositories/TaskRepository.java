package co.cenitiumdev.projectmanagementapi.services.repositories;

import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);

    Optional<Task> findByIdAndProject(Long id, Project project);
}