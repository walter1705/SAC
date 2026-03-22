package com.uniquindio.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.backend.model.dto.request.CambiarEstadoUsuarioRequest;
import com.uniquindio.backend.model.dto.request.CrearUsuarioRequest;
import com.uniquindio.backend.model.dto.request.LoginRequest;
import com.uniquindio.backend.model.dto.response.LoginResponse;
import com.uniquindio.backend.model.dto.response.UsuarioResponse;
import com.uniquindio.backend.model.enums.RolUsuario;
import com.uniquindio.backend.service.UsuarioService;
import com.uniquindio.backend.util.config.JacksonConfig;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import com.uniquindio.backend.util.exception.UnauthorizedException;
import com.uniquindio.backend.util.security.JwtAuthenticationEntryPoint;
import com.uniquindio.backend.util.security.JwtAuthenticationFilter;
import com.uniquindio.backend.util.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import({UsuarioControllerTest.TestSecurityConfig.class, JacksonConfig.class})
@EnableAutoConfiguration
class UsuarioControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UsuarioResponse testUsuarioResponse;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testUsuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .nombreUsuario("jperez")
                .email("jperez@test.com")
                .rol(RolUsuario.GESTOR)
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("Login exitoso retorna 200 OK")
    void login_conCredencialesValidas_retorna200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setNombreUsuario("jperez");
        request.setContrasena("password123");

        LoginResponse response = LoginResponse.builder()
                .token("test-token")
                .tipo("Bearer")
                .nombreUsuario("jperez")
                .rol(RolUsuario.GESTOR)
                .expiraEn(3600)
                .build();

        when(usuarioService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.nombreUsuario").value("jperez"))
                .andExpect(jsonPath("$.rol").value("GESTOR"));
    }

    @Test
    @DisplayName("Login con credenciales invalidas retorna 401")
    void login_conCredencialesInvalidas_retorna401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setNombreUsuario("jperez");
        request.setContrasena("wrongpassword");

        when(usuarioService.login(any(LoginRequest.class)))
                .thenThrow(new UnauthorizedException("Nombre de usuario o contraseña inválidos"));

        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login sin nombre de usuario retorna 400")
    void login_sinNombreUsuario_retorna400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setContrasena("password123");

        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login sin contrasena retorna 400")
    void login_sinContrasena_retorna400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setNombreUsuario("jperez");

        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar usuarios retorna 200 OK")
    void listarUsuarios_retorna200() throws Exception {
        when(usuarioService.listarUsuariosActivos()).thenReturn(List.of(testUsuarioResponse));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombreUsuario").value("jperez"));
    }

    @Test
    @DisplayName("Listar usuarios sin usuarios retorna lista vacia")
    void listarUsuarios_sinUsuarios_retornaListaVacia() throws Exception {
        when(usuarioService.listarUsuariosActivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Crear usuario exitoso retorna 201 CREATED")
    void crearUsuario_conDatosValidos_retorna201() throws Exception {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombreCompleto("Nuevo Usuario");
        request.setNombreUsuario("nusuario");
        request.setContrasena("password123");
        request.setEmail("nusuario@test.com");
        request.setRol(RolUsuario.GESTOR);

        UsuarioResponse response = UsuarioResponse.builder()
                .id(2L)
                .nombreCompleto("Nuevo Usuario")
                .nombreUsuario("nusuario")
                .email("nusuario@test.com")
                .rol(RolUsuario.GESTOR)
                .activo(true)
                .build();

        when(usuarioService.crearUsuario(any(CrearUsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombreUsuario").value("nusuario"));
    }

    @Test
    @DisplayName("Crear usuario con nombre duplicado retorna 400")
    void crearUsuario_conNombreDuplicado_retorna400() throws Exception {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombreCompleto("Nuevo Usuario");
        request.setNombreUsuario("jperez");
        request.setContrasena("password123");
        request.setEmail("nuevo@test.com");
        request.setRol(RolUsuario.GESTOR);

        when(usuarioService.crearUsuario(any(CrearUsuarioRequest.class)))
                .thenThrow(new BadRequestException("El nombre de usuario ya existe: jperez"));

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear usuario sin email retorna 400")
    void crearUsuario_sinEmail_retorna400() throws Exception {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombreCompleto("Nuevo Usuario");
        request.setNombreUsuario("nusuario");
        request.setContrasena("password123");
        request.setRol(RolUsuario.GESTOR);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear usuario con email invalido retorna 400")
    void crearUsuario_conEmailInvalido_retorna400() throws Exception {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombreCompleto("Nuevo Usuario");
        request.setNombreUsuario("nusuario");
        request.setContrasena("password123");
        request.setEmail("email-invalido");
        request.setRol(RolUsuario.GESTOR);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear usuario con contrasena corta retorna 400")
    void crearUsuario_conContrasenaCorta_retorna400() throws Exception {
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setNombreCompleto("Nuevo Usuario");
        request.setNombreUsuario("nusuario");
        request.setContrasena("short");
        request.setEmail("nusuario@test.com");
        request.setRol(RolUsuario.GESTOR);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cambiar estado de usuario retorna 200 OK")
    void cambiarEstadoUsuario_conDatosValidos_retorna200() throws Exception {
        CambiarEstadoUsuarioRequest request = new CambiarEstadoUsuarioRequest();
        request.setActivo(false);

        UsuarioResponse response = UsuarioResponse.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .nombreUsuario("jperez")
                .email("jperez@test.com")
                .rol(RolUsuario.GESTOR)
                .activo(false)
                .build();

        when(usuarioService.cambiarEstadoUsuario(eq(1L), any(CambiarEstadoUsuarioRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/v1/usuarios/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    @DisplayName("Cambiar estado de usuario no encontrado retorna 404")
    void cambiarEstadoUsuario_noEncontrado_retorna404() throws Exception {
        CambiarEstadoUsuarioRequest request = new CambiarEstadoUsuarioRequest();
        request.setActivo(false);

        when(usuarioService.cambiarEstadoUsuario(eq(999L), any(CambiarEstadoUsuarioRequest.class)))
                .thenThrow(new ResourceNotFoundException("Usuario con ID 999 no encontrado"));

        mockMvc.perform(patch("/api/v1/usuarios/999/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
