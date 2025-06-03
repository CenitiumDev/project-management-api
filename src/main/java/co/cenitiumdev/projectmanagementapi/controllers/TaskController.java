package co.cenitiumdev.projectmanagementapi.controllers;

import co.cenitiumdev.projectmanagementapi.models.Task;
import co.cenitiumdev.projectmanagementapi.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@PathVariable Long projectId,
                                           @Valid @RequestBody Task task,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Task createdTask = taskService.createTask(projectId, task, currentUser.getUsername());
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId,
                                                        @AuthenticationPrincipal UserDetails currentUser) {
        try {
            List<Task> tasks = taskService.getTasksByProjectAndOwner(projectId, currentUser.getUsername());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long projectId,
                                            @PathVariable Long taskId,
                                            @AuthenticationPrincipal UserDetails currentUser) {
        return taskService.getTaskByIdAndProjectAndOwner(taskId, projectId, currentUser.getUsername())
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId,
                                           @Valid @RequestBody Task task,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        try {
            Task updated = taskService.updateTask(taskId, projectId, task, currentUser.getUsername());
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        try {
            taskService.deleteTask(taskId, projectId, currentUser.getUsername());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
