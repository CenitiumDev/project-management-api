package co.cenitiumdev.projectmanagementapi.DTOs;

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
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "El nombre del proyecto no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre del proyecto debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción del proyecto no puede exceder los 500 caracteres")
    private String description;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate endDate;
}
