package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.RolUsuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {

    private Long id;
    private String nombreCompleto;
    private String nombreUsuario;
    private String email;
    private RolUsuario rol;
    private Boolean activo;
}
