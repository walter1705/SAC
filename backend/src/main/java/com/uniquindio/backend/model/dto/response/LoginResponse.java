package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.RolUsuario;

public record LoginResponse(
        String token,
        String tipo,
        String nombreUsuario,
        RolUsuario rol,
        Integer expiraEn
) {}
