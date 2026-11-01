package pe.com.sikalma;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class SikalmaApplicationTests {

    @Test
    void iniciaContextoDeAplicacion() {
        // Verifica que el contexto de Spring Boot inicie correctamente sin lanzar excepciones
        assertDoesNotThrow(() -> {
            // Contexto levantado con exito en entorno de pruebas
        });
    }
}
