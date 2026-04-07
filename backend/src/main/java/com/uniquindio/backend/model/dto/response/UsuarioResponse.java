package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.RolUsuario;

public record UsuarioResponse(
        Long id,
        String nombreCompleto,
        String nombreUsuario,
        String email,
        RolUsuario rol,
        Boolean activo
) {}
