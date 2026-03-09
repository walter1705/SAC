package com.uniquindio.backend.dto.response;

import com.uniquindio.backend.enums.RolUsuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {

    private Long id;
    private String username;
    private RolUsuario rol;
    private Boolean activo;
    private String nombre;
}
