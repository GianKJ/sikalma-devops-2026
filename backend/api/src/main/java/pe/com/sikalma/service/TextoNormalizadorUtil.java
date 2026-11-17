package pe.com.sikalma.service;

import java.util.Locale;
import java.util.Objects;
import java.util.function.ToLongFunction;

import pe.com.sikalma.exception.RegistroDuplicadoException;

public final class TextoNormalizadorUtil {

    private TextoNormalizadorUtil() {
    }

    public static String opcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    public static String opcionalMinuscula(String valor) {
        String normalizado = opcional(valor);
        return normalizado == null ? null : normalizado.toLowerCase(Locale.ROOT);
    }

    public static String opcionalMayuscula(String valor) {
        String normalizado = opcional(valor);
        return normalizado == null ? null : normalizado.toUpperCase(Locale.ROOT);
    }

    public static <T> void validarDuplicado(
            T existente,
            Long idActual,
            ToLongFunction<T> idExtractor,
            String campo,
            String mensaje) {
        if (existente != null && !Objects.equals(idExtractor.applyAsLong(existente), idActual)) {
            throw new RegistroDuplicadoException(campo, mensaje);
        }
    }
}
