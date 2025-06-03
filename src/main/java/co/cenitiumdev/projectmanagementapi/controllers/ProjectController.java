package co.cenitiumdev.projectmanagementapi.controllers;

import co.cenitiumdev.projectmanagementapi.DTOs.ProjectDTO;
import co.cenitiumdev.projectmanagementapi.exceptions.ResourceNotFoundException;
import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private ProjectDTO convertToDto(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        return dto;
    }

    private Project convertToEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        return project;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO,
                                                    @AuthenticationPrincipal UserDetails currentUser) {
        Project project = convertToEntity(projectDTO);
        Project createdProject = projectService.createProject(project, currentUser.getUsername());
        return new ResponseEntity<>(convertToDto(createdProject), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getMyProjects(@AuthenticationPrincipal UserDetails currentUser) {
        List<Project> projects = projectService.getProjectsByOwner(currentUser.getUsername());
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserDetails currentUser) {
        Project project = projectService.getProjectByIdAndOwner(id, currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado o no autorizado para el usuario con ID: " + id));

        return new ResponseEntity<>(convertToDto(project), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id,
                                                    @Valid @RequestBody ProjectDTO projectDTO,
                                                    @AuthenticationPrincipal UserDetails currentUser) {
        Project projectToUpdate = convertToEntity(projectDTO);
        Project updated = projectService.updateProject(id, projectToUpdate, currentUser.getUsername());
        return new ResponseEntity<>(convertToDto(updated), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails currentUser) {
        projectService.deleteProject(id, currentUser.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
