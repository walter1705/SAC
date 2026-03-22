package com.uniquindio.backend.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.dto.request.CambiarEstadoUsuarioRequest;
import com.uniquindio.backend.model.dto.request.CrearUsuarioRequest;
import com.uniquindio.backend.model.dto.request.LoginRequest;
import com.uniquindio.backend.model.dto.response.LoginResponse;
import com.uniquindio.backend.model.dto.response.UsuarioResponse;
import com.uniquindio.backend.model.enums.RolUsuario;
import com.uniquindio.backend.repository.UsuarioRepository;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import com.uniquindio.backend.util.exception.UnauthorizedException;
import com.uniquindio.backend.util.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario testUsuario;
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        hashedPassword = BCrypt.withDefaults().hashToString(12, "password123".toCharArray());
        testUsuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .nombreUsuario("jperez")
                .contrasena(hashedPassword)
                .email("jperez@test.com")
                .rol(RolUsuario.GESTOR)
                .activo(true)
                .build();
    }

    @Nested
    @DisplayName("Tests de Login")
    class LoginTests {

        @Test
        @DisplayName("Login exitoso con credenciales validas")
        void login_conCredencialesValidas_retornaToken() {
            LoginRequest request = new LoginRequest();
            request.setNombreUsuario("jperez");
            request.setContrasena("password123");

            when(usuarioRepository.findByNombreUsuario("jperez")).thenReturn(Optional.of(testUsuario));
            when(jwtUtil.generateToken(testUsuario)).thenReturn("test-jwt-token");

            LoginResponse response = usuarioService.login(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("test-jwt-token");
            assertThat(response.getTipo()).isEqualTo("Bearer");
            assertThat(response.getNombreUsuario()).isEqualTo("jperez");
            assertThat(response.getRol()).isEqualTo(RolUsuario.GESTOR);
            assertThat(response.getExpiraEn()).isEqualTo(3600);
        }

        @Test
        @DisplayName("Login falla con usuario no encontrado")
        void login_conUsuarioNoEncontrado_lanzaUnauthorizedException() {
            LoginRequest request = new LoginRequest();
            request.setNombreUsuario("noexiste");
            request.setContrasena("password123");

            when(usuarioRepository.findByNombreUsuario("noexiste")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> usuarioService.login(request))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("Nombre de usuario o contraseña inválidos");
        }

        @Test
        @DisplayName("Login falla con contrasena incorrecta")
        void login_conContrasenaIncorrecta_lanzaUnauthorizedException() {
            LoginRequest request = new LoginRequest();
            request.setNombreUsuario("jperez");
            request.setContrasena("wrongpassword");

            when(usuarioRepository.findByNombreUsuario("jperez")).thenReturn(Optional.of(testUsuario));

            assertThatThrownBy(() -> usuarioService.login(request))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("Nombre de usuario o contraseña inválidos");
        }

        @Test
        @DisplayName("Login falla con usuario inactivo")
        void login_conUsuarioInactivo_lanzaUnauthorizedException() {
            testUsuario.setActivo(false);
            LoginRequest request = new LoginRequest();
            request.setNombreUsuario("jperez");
            request.setContrasena("password123");

            when(usuarioRepository.findByNombreUsuario("jperez")).thenReturn(Optional.of(testUsuario));

            assertThatThrownBy(() -> usuarioService.login(request))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessage("La cuenta de usuario está desactivada");
        }
    }

    @Nested
    @DisplayName("Tests de Listar Usuarios Activos")
    class ListarUsuariosActivosTests {

        @Test
        @DisplayName("Lista usuarios activos correctamente")
        void listarUsuariosActivos_retornaListaDeUsuarios() {
            Usuario usuario2 = Usuario.builder()
                    .id(2L)
                    .nombreCompleto("Maria Garcia")
                    .nombreUsuario("mgarcia")
                    .email("mgarcia@test.com")
                    .rol(RolUsuario.ADMINISTRADOR)
                    .activo(true)
                    .build();

            when(usuarioRepository.findByActivoTrue()).thenReturn(List.of(testUsuario, usuario2));

            List<UsuarioResponse> usuarios = usuarioService.listarUsuariosActivos();

            assertThat(usuarios).hasSize(2);
            assertThat(usuarios.get(0).getNombreUsuario()).isEqualTo("jperez");
            assertThat(usuarios.get(1).getNombreUsuario()).isEqualTo("mgarcia");
        }

        @Test
        @DisplayName("Lista vacia cuando no hay usuarios activos")
        void listarUsuariosActivos_sinUsuarios_retornaListaVacia() {
            when(usuarioRepository.findByActivoTrue()).thenReturn(List.of());

            List<UsuarioResponse> usuarios = usuarioService.listarUsuariosActivos();

            assertThat(usuarios).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests de Crear Usuario")
    class CrearUsuarioTests {

        @Test
        @DisplayName("Crea usuario exitosamente")
        void crearUsuario_conDatosValidos_retornaUsuarioResponse() {
            CrearUsuarioRequest request = new CrearUsuarioRequest();
            request.setNombreCompleto("Nuevo Usuario");
            request.setNombreUsuario("nusuario");
            request.setContrasena("password123");
            request.setEmail("nusuario@test.com");
            request.setRol(RolUsuario.GESTOR);

            when(usuarioRepository.existsByNombreUsuario("nusuario")).thenReturn(false);
            when(usuarioRepository.existsByEmail("nusuario@test.com")).thenReturn(false);
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
                Usuario saved = invocation.getArgument(0);
                saved.setId(3L);
                return saved;
            });

            UsuarioResponse response = usuarioService.crearUsuario(request);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(3L);
            assertThat(response.getNombreCompleto()).isEqualTo("Nuevo Usuario");
            assertThat(response.getNombreUsuario()).isEqualTo("nusuario");
            assertThat(response.getEmail()).isEqualTo("nusuario@test.com");
            assertThat(response.getRol()).isEqualTo(RolUsuario.GESTOR);
            assertThat(response.getActivo()).isTrue();

            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Falla al crear usuario con nombre de usuario duplicado")
        void crearUsuario_conNombreUsuarioDuplicado_lanzaBadRequestException() {
            CrearUsuarioRequest request = new CrearUsuarioRequest();
            request.setNombreUsuario("jperez");
            request.setEmail("nuevo@test.com");

            when(usuarioRepository.existsByNombreUsuario("jperez")).thenReturn(true);

            assertThatThrownBy(() -> usuarioService.crearUsuario(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("El nombre de usuario ya existe: jperez");
        }

        @Test
        @DisplayName("Falla al crear usuario con email duplicado")
        void crearUsuario_conEmailDuplicado_lanzaBadRequestException() {
            CrearUsuarioRequest request = new CrearUsuarioRequest();
            request.setNombreUsuario("nuevouser");
            request.setEmail("jperez@test.com");

            when(usuarioRepository.existsByNombreUsuario("nuevouser")).thenReturn(false);
            when(usuarioRepository.existsByEmail("jperez@test.com")).thenReturn(true);

            assertThatThrownBy(() -> usuarioService.crearUsuario(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("El email ya existe: jperez@test.com");
        }
    }

    @Nested
    @DisplayName("Tests de Cambiar Estado Usuario")
    class CambiarEstadoUsuarioTests {

        @Test
        @DisplayName("Desactiva usuario exitosamente")
        void cambiarEstadoUsuario_desactivar_retornaUsuarioInactivo() {
            CambiarEstadoUsuarioRequest request = new CambiarEstadoUsuarioRequest();
            request.setActivo(false);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UsuarioResponse response = usuarioService.cambiarEstadoUsuario(1L, request);

            assertThat(response).isNotNull();
            assertThat(response.getActivo()).isFalse();
            verify(usuarioRepository).save(testUsuario);
        }

        @Test
        @DisplayName("Activa usuario exitosamente")
        void cambiarEstadoUsuario_activar_retornaUsuarioActivo() {
            testUsuario.setActivo(false);
            CambiarEstadoUsuarioRequest request = new CambiarEstadoUsuarioRequest();
            request.setActivo(true);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UsuarioResponse response = usuarioService.cambiarEstadoUsuario(1L, request);

            assertThat(response).isNotNull();
            assertThat(response.getActivo()).isTrue();
        }

        @Test
        @DisplayName("Falla al cambiar estado de usuario no encontrado")
        void cambiarEstadoUsuario_usuarioNoEncontrado_lanzaResourceNotFoundException() {
            CambiarEstadoUsuarioRequest request = new CambiarEstadoUsuarioRequest();
            request.setActivo(false);

            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(999L, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Usuario con ID 999 no encontrado");
        }
    }

    @Nested
    @DisplayName("Tests de FindById")
    class FindByIdTests {

        @Test
        @DisplayName("Encuentra usuario por ID")
        void findById_conIdValido_retornaUsuario() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUsuario));

            Usuario usuario = usuarioService.findById(1L);

            assertThat(usuario).isNotNull();
            assertThat(usuario.getId()).isEqualTo(1L);
            assertThat(usuario.getNombreUsuario()).isEqualTo("jperez");
        }

        @Test
        @DisplayName("Falla al buscar usuario por ID no existente")
        void findById_conIdInvalido_lanzaResourceNotFoundException() {
            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> usuarioService.findById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Usuario con ID 999 no encontrado");
        }
    }

    @Nested
    @DisplayName("Tests de FindByNombreUsuario")
    class FindByNombreUsuarioTests {

        @Test
        @DisplayName("Encuentra usuario por nombre de usuario")
        void findByNombreUsuario_conNombreValido_retornaUsuario() {
            when(usuarioRepository.findByNombreUsuario("jperez")).thenReturn(Optional.of(testUsuario));

            Usuario usuario = usuarioService.findByNombreUsuario("jperez");

            assertThat(usuario).isNotNull();
            assertThat(usuario.getNombreUsuario()).isEqualTo("jperez");
        }

        @Test
        @DisplayName("Falla al buscar usuario por nombre no existente")
        void findByNombreUsuario_conNombreInvalido_lanzaResourceNotFoundException() {
            when(usuarioRepository.findByNombreUsuario("noexiste")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> usuarioService.findByNombreUsuario("noexiste"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Usuario no encontrado: noexiste");
        }
    }
}
