package com.uniquindio.backend.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.dto.request.CambiarEstadoUsuarioRequest;
import com.uniquindio.backend.model.dto.request.CrearUsuarioRequest;
import com.uniquindio.backend.model.dto.request.LoginRequest;
import com.uniquindio.backend.model.dto.response.LoginResponse;
import com.uniquindio.backend.model.dto.response.UsuarioResponse;
import com.uniquindio.backend.repository.UsuarioRepository;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import com.uniquindio.backend.util.exception.UnauthorizedException;
import com.uniquindio.backend.util.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Nombre de usuario o contraseña inválidos"));

        if (!usuario.getActivo()) {
            throw new UnauthorizedException("La cuenta de usuario está desactivada");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(request.getPassword().toCharArray(), usuario.getPassword());
        if (!result.verified) {
            throw new UnauthorizedException("Nombre de usuario o contraseña inválidos");
        }

        String token = jwtUtil.generateToken(usuario);

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .expiraEn(3600)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya existe: " + request.getUsername());
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, request.getPassword().toCharArray());

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .rol(request.getRol())
                .activo(true)
                .nombre(request.getNombre())
                .build();

        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    @Transactional
    public UsuarioResponse cambiarEstadoUsuario(Long id, CambiarEstadoUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        usuario.setActivo(request.getActivo());
        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .nombre(usuario.getNombre())
                .build();
    }
}
