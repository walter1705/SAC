package com.uniquindio.backend.util.config;

import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.enums.RolUsuario;
import com.uniquindio.backend.repository.UsuarioRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByNombreUsuario("admin").isEmpty()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, "admin123".toCharArray());

            Usuario admin = Usuario.builder()
                    .nombreUsuario("admin")
                    .contrasena(hashedPassword)
                    .rol(RolUsuario.ADMINISTRADOR)
                    .nombreCompleto("Administrador del Sistema")
                    .email("admin@sac.edu.co")
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
            log.info("Usuario ADMIN creado (admin / admin123)");
        }

        if (usuarioRepository.findByNombreUsuario("gestor").isEmpty()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, "gestor123".toCharArray());

            Usuario gestor = Usuario.builder()
                    .nombreUsuario("gestor")
                    .contrasena(hashedPassword)
                    .rol(RolUsuario.GESTOR)
                    .nombreCompleto("Gestor de Solicitudes")
                    .email("gestor@sac.edu.co")
                    .activo(true)
                    .build();

            usuarioRepository.save(gestor);
            log.info("Usuario GESTOR creado (gestor / gestor123)");
        }
    }
}
