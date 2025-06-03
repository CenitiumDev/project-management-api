package co.cenitiumdev.projectmanagementapi.DTOs;

import co.cenitiumdev.projectmanagementapi.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "El nombre de la tarea no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre de la tarea debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción de la tarea no puede exceder los 500 caracteres")
    private String description;

    @NotNull(message = "La fecha de vencimiento no puede ser nula")
    private LocalDate dueDate;

    @NotNull(message = "El estado de la tarea no puede ser nulo")
    private TaskStatus status;
}