package co.cenitiumdev.projectmanagementapi.services;

import co.cenitiumdev.projectmanagementapi.models.Project;
import co.cenitiumdev.projectmanagementapi.models.Task;
import co.cenitiumdev.projectmanagementapi.models.User;
import co.cenitiumdev.projectmanagementapi.models.enums.TaskStatus;
import co.cenitiumdev.projectmanagementapi.services.repositories.ProjectRepository;
import co.cenitiumdev.projectmanagementapi.services.repositories.TaskRepository;
import co.cenitiumdev.projectmanagementapi.services.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Task createTask(Long projectId, Task task, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario propietario no encontrado: " + ownerUsername));

        Project project = projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no autorizado para el usuario."));

        task.setProject(project);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByProjectAndOwner(Long projectId, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario propietario no encontrado: " + ownerUsername));

        Project project = projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no autorizado para el usuario."));

        return taskRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    public Optional<Task> getTaskByIdAndProjectAndOwner(Long taskId, Long projectId, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario propietario no encontrado: " + ownerUsername));

        Project project = projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no autorizado para el usuario."));

        return taskRepository.findByIdAndProject(taskId, project);
    }


    @Transactional
    public Task updateTask(Long taskId, Long projectId, Task updatedTask, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario propietario no encontrado: " + ownerUsername));

        Project project = projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no autorizado para el usuario."));

        return taskRepository.findByIdAndProject(taskId, project)
                .map(existingTask -> {
                    existingTask.setName(updatedTask.getName());
                    existingTask.setDescription(updatedTask.getDescription());
                    existingTask.setDueDate(updatedTask.getDueDate());
                    if (updatedTask.getStatus() != null) {
                        existingTask.setStatus(updatedTask.getStatus());
                    }
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada o no pertenece al proyecto/usuario."));
    }

    @Transactional
    public void deleteTask(Long taskId, Long projectId, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario propietario no encontrado: " + ownerUsername));

        Project project = projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado o no autorizado para el usuario."));

        taskRepository.findByIdAndProject(taskId, project)
                .ifPresentOrElse(
                        taskRepository::delete,
                        () -> { throw new RuntimeException("Tarea no encontrada o no pertenece al proyecto/usuario."); }
                );
    }
}
