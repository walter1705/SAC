package com.uniquindio.backend.util.security;

import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.enums.RolUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "SAC_Sistema_Atencion_Clasificacion_SecretKey_2026_UniqueKey");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 3600000L);

        testUsuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .nombreUsuario("jperez")
                .email("jperez@test.com")
                .rol(RolUsuario.GESTOR)
                .activo(true)
                .build();
    }

    @Nested
    @DisplayName("Tests de Generacion de Token")
    class GenerateTokenTests {

        @Test
        @DisplayName("Genera token correctamente")
        void generateToken_conUsuarioValido_retornaToken() {
            String token = jwtUtil.generateToken(testUsuario);

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token.split("\\.")).hasSize(3); // JWT tiene 3 partes
        }

        @Test
        @DisplayName("Genera tokens diferentes para usuarios diferentes")
        void generateToken_conUsuariosDiferentes_retornaTokensDiferentes() {
            Usuario otroUsuario = Usuario.builder()
                    .id(2L)
                    .nombreUsuario("mgarcia")
                    .rol(RolUsuario.ADMINISTRADOR)
                    .build();

            String token1 = jwtUtil.generateToken(testUsuario);
            String token2 = jwtUtil.generateToken(otroUsuario);

            assertThat(token1).isNotEqualTo(token2);
        }
    }

    @Nested
    @DisplayName("Tests de Extraccion de Datos de Token")
    class ExtractFromTokenTests {

        @Test
        @DisplayName("Extrae nombre de usuario del token")
        void getNombreUsuarioFromToken_conTokenValido_retornaNombreUsuario() {
            String token = jwtUtil.generateToken(testUsuario);

            String nombreUsuario = jwtUtil.getNombreUsuarioFromToken(token);

            assertThat(nombreUsuario).isEqualTo("jperez");
        }

        @Test
        @DisplayName("Extrae rol del token")
        void getRolFromToken_conTokenValido_retornaRol() {
            String token = jwtUtil.generateToken(testUsuario);

            String rol = jwtUtil.getRolFromToken(token);

            assertThat(rol).isEqualTo("GESTOR");
        }

        @Test
        @DisplayName("Extrae rol ADMINISTRADOR correctamente")
        void getRolFromToken_conUsuarioAdmin_retornaAdministrador() {
            testUsuario.setRol(RolUsuario.ADMINISTRADOR);
            String token = jwtUtil.generateToken(testUsuario);

            String rol = jwtUtil.getRolFromToken(token);

            assertThat(rol).isEqualTo("ADMINISTRADOR");
        }
    }

    @Nested
    @DisplayName("Tests de Validacion de Token")
    class ValidateTokenTests {

        @Test
        @DisplayName("Valida token correctamente generado")
        void validateToken_conTokenValido_retornaTrue() {
            String token = jwtUtil.generateToken(testUsuario);

            boolean isValid = jwtUtil.validateToken(token);

            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Rechaza token manipulado")
        void validateToken_conTokenManipulado_retornaFalse() {
            String token = jwtUtil.generateToken(testUsuario);
            String tokenManipulado = token + "manipulado";

            boolean isValid = jwtUtil.validateToken(tokenManipulado);

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Rechaza token null")
        void validateToken_conTokenNull_retornaFalse() {
            boolean isValid = jwtUtil.validateToken(null);

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Rechaza token vacio")
        void validateToken_conTokenVacio_retornaFalse() {
            boolean isValid = jwtUtil.validateToken("");

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Rechaza token con formato incorrecto")
        void validateToken_conFormatoIncorrecto_retornaFalse() {
            boolean isValid = jwtUtil.validateToken("token.invalido");

            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Rechaza token expirado")
        void validateToken_conTokenExpirado_retornaFalse() {
            JwtUtil jwtUtilConExpiracionCorta = new JwtUtil();
            ReflectionTestUtils.setField(jwtUtilConExpiracionCorta, "jwtSecret",
                    "SAC_Sistema_Atencion_Clasificacion_SecretKey_2026_UniqueKey");
            ReflectionTestUtils.setField(jwtUtilConExpiracionCorta, "jwtExpiration", -1000L);

            String token = jwtUtilConExpiracionCorta.generateToken(testUsuario);

            boolean isValid = jwtUtil.validateToken(token);

            assertThat(isValid).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests de Consistencia")
    class ConsistencyTests {

        @Test
        @DisplayName("Datos extraidos coinciden con datos del usuario")
        void tokenData_coincideConDatosUsuario() {
            String token = jwtUtil.generateToken(testUsuario);

            String nombreUsuario = jwtUtil.getNombreUsuarioFromToken(token);
            String rol = jwtUtil.getRolFromToken(token);

            assertThat(nombreUsuario).isEqualTo(testUsuario.getNombreUsuario());
            assertThat(rol).isEqualTo(testUsuario.getRol().name());
        }

        @Test
        @DisplayName("Token es valido inmediatamente despues de generarse")
        void tokenRecienGenerado_esValido() {
            String token = jwtUtil.generateToken(testUsuario);

            assertThat(jwtUtil.validateToken(token)).isTrue();
            assertThat(jwtUtil.getNombreUsuarioFromToken(token)).isEqualTo("jperez");
            assertThat(jwtUtil.getRolFromToken(token)).isEqualTo("GESTOR");
        }
    }

    @Nested
    @DisplayName("Tests de Clave Secreta Corta")
    class ShortSecretKeyTests {

        @Test
        @DisplayName("Maneja clave secreta corta correctamente")
        void generateToken_conClaveCorta_funcionaCorrectamente() {
            JwtUtil jwtUtilConClaveCorta = new JwtUtil();
            ReflectionTestUtils.setField(jwtUtilConClaveCorta, "jwtSecret", "short");
            ReflectionTestUtils.setField(jwtUtilConClaveCorta, "jwtExpiration", 3600000L);

            String token = jwtUtilConClaveCorta.generateToken(testUsuario);

            assertThat(token).isNotNull();
            assertThat(jwtUtilConClaveCorta.validateToken(token)).isTrue();
        }
    }
}
