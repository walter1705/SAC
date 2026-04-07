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
        Usuario usuario = usuarioRepository.findByNombreUsuario(request.nombreUsuario())
                .orElseThrow(() -> new UnauthorizedException("Nombre de usuario o contraseña inválidos"));

        if (!usuario.getActivo()) {
            throw new UnauthorizedException("La cuenta de usuario está desactivada");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(request.contrasena().toCharArray(), usuario.getContrasena());
        if (!result.verified) {
            throw new UnauthorizedException("Nombre de usuario o contraseña inválidos");
        }

        String token = jwtUtil.generateToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getNombreUsuario(),
                usuario.getRol(),
                (int) (jwtUtil.getExpiration() / 1000)
        );
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByNombreUsuario(request.nombreUsuario())) {
            throw new BadRequestException("El nombre de usuario ya existe: " + request.nombreUsuario());
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BadRequestException("El email ya existe: " + request.email());
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, request.contrasena().toCharArray());

        Usuario usuario = Usuario.builder()
                .nombreCompleto(request.nombreCompleto())
                .nombreUsuario(request.nombreUsuario())
                .contrasena(hashedPassword)
                .email(request.email())
                .rol(request.rol())
                .activo(true)
                .build();

        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    @Transactional
    public UsuarioResponse cambiarEstadoUsuario(Long id, CambiarEstadoUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        usuario.setActivo(request.activo());
        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + nombreUsuario));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getActivo()
        );
    }
}
