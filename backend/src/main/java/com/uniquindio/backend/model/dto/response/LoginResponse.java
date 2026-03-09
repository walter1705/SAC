package com.uniquindio.backend.dto.response;

import com.uniquindio.backend.enums.RolUsuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String tipo;
    private String username;
    private RolUsuario rol;
    private Integer expiraEn;
}
