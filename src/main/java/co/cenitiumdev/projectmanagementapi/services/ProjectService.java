package co.cenitiumdev.projectmanagementapi.services;

import co.cenitiumdev.projectmanagementapi.exceptions.ResourceNotFoundException;
import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.models.User;
import co.cenitiumdev.projectmanagementapi.services.repositories.ProjectRepository;
import co.cenitiumdev.projectmanagementapi.services.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Project createProject(Project project, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario del proyecto no encontrado: " + ownerUsername));
        project.setOwner(owner);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByOwner(String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario del proyecto no encontrado: " + ownerUsername));
        return projectRepository.findByOwner(owner);
    }

    @Transactional(readOnly = true)
    public Optional<Project> getProjectByIdAndOwner(Long projectId, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario del proyecto no encontrado: " + ownerUsername));
        return projectRepository.findByIdAndOwner(projectId, owner);
    }

    @Transactional
    public Project updateProject(Long projectId, Project updatedProject, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario del proyecto no encontrado: " + ownerUsername));

        return projectRepository.findByIdAndOwner(projectId, owner)
                .map(existingProject -> {
                    existingProject.setName(updatedProject.getName());
                    existingProject.setDescription(updatedProject.getDescription());
                    existingProject.setStartDate(updatedProject.getStartDate());
                    existingProject.setEndDate(updatedProject.getEndDate());
                    return projectRepository.save(existingProject);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado o no autorizado para el usuario con ID: " + projectId));
    }

    @Transactional
    public void deleteProject(Long projectId, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario del proyecto no encontrado: " + ownerUsername));

        projectRepository.findByIdAndOwner(projectId, owner)
                .ifPresentOrElse(
                        projectRepository::delete,
                        () -> { throw new ResourceNotFoundException("Proyecto no encontrado o no autorizado para el usuario con ID: " + projectId); }
                );
    }
}
