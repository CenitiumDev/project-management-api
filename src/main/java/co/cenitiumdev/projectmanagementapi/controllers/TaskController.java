package co.cenitiumdev.projectmanagementapi.controllers;

import co.cenitiumdev.projectmanagementapi.DTOs.TaskDTO;
import co.cenitiumdev.projectmanagementapi.models.Task;
import co.cenitiumdev.projectmanagementapi.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private TaskDTO convertToDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        return dto;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());
        return task;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@PathVariable Long projectId,
                                              @Valid @RequestBody TaskDTO taskDTO,
                                              @AuthenticationPrincipal UserDetails currentUser) {
        Task task = convertToEntity(taskDTO);
        Task createdTask = taskService.createTask(projectId, task, currentUser.getUsername());
        return new ResponseEntity<>(convertToDto(createdTask), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksByProject(@PathVariable Long projectId,
                                                           @AuthenticationPrincipal UserDetails currentUser) {
        List<Task> tasks = taskService.getTasksByProjectAndOwner(projectId, currentUser.getUsername());
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long projectId,
                                               @PathVariable Long taskId,
                                               @AuthenticationPrincipal UserDetails currentUser) {
        return taskService.getTaskByIdAndProjectAndOwner(taskId, projectId, currentUser.getUsername())
                .map(this::convertToDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @Valid @RequestBody TaskDTO taskDTO,
                                              @AuthenticationPrincipal UserDetails currentUser) {
        Task taskToUpdate = convertToEntity(taskDTO);
        Task updated = taskService.updateTask(taskId, projectId, taskToUpdate, currentUser.getUsername());
        return new ResponseEntity<>(convertToDto(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        taskService.deleteTask(taskId, projectId, currentUser.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
