package com.uniquindio.backend.controller;

import com.uniquindio.backend.model.dto.request.CambiarEstadoUsuarioRequest;
import com.uniquindio.backend.model.dto.request.CrearUsuarioRequest;
import com.uniquindio.backend.model.dto.request.LoginRequest;
import com.uniquindio.backend.model.dto.response.LoginResponse;
import com.uniquindio.backend.model.dto.response.UsuarioResponse;
import com.uniquindio.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioResponse response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstadoUsuario(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoUsuarioRequest request) {
        UsuarioResponse response = usuarioService.cambiarEstadoUsuario(id, request);
        return ResponseEntity.ok(response);
    }
}
