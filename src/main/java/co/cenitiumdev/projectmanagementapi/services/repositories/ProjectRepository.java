package co.cenitiumdev.projectmanagementapi.services.repositories;

import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwner(User owner);

    Optional<Project> findByIdAndOwner(Long id, User owner);

}
