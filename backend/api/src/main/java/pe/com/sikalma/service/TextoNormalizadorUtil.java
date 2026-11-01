package pe.com.sikalma.service;

import java.util.Locale;

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
}
