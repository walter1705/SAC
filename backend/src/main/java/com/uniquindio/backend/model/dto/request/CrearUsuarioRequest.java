package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearUsuarioRequest {

    @NotBlank(message = "El nombre completo es requerido")
    @Size(min = 2, max = 100, message = "El nombre completo debe tener entre 2 y 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String contrasena;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotNull(message = "El rol es requerido")
    private RolUsuario rol;
}
