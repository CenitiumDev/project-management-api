package co.cenitiumdev.projectmanagementapi.controllers;

import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project,
                                                 @AuthenticationPrincipal UserDetails currentUser) {
        Project createdProject = projectService.createProject(project, currentUser.getUsername());
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getMyProjects(@AuthenticationPrincipal UserDetails currentUser) {
        List<Project> projects = projectService.getProjectsByOwner(currentUser.getUsername());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails currentUser) {
        return projectService.getProjectByIdAndOwner(id, currentUser.getUsername())
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @Valid @RequestBody Project project,
                                                 @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Project updated = projectService.updateProject(id, project, currentUser.getUsername());
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails currentUser) {
        try {
            projectService.deleteProject(id, currentUser.getUsername());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
