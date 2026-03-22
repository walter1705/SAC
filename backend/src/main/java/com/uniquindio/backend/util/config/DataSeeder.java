package com.uniquindio.backend.util.config;

import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.enums.RolUsuario;
import com.uniquindio.backend.repository.UsuarioRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
            System.out.println("====== USUARIO ADMIN CREADO (admin / admin123) ======");
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
            System.out.println("====== USUARIO GESTOR CREADO (gestor / gestor123) ======");
        }
    }
}
