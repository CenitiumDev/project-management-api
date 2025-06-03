package co.cenitiumdev.projectmanagementapi.repository;

import co.cenitiumdev.projectmanagementapi.model.Project;
import co.cenitiumdev.projectmanagementapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwner(User owner);

    Optional<Project> findByIdAndOwner(Long id, User owner);

}
