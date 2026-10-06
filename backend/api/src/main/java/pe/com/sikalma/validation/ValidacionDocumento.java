package pe.com.sikalma.validation;

import org.springframework.validation.Errors;

import pe.com.sikalma.entity.TipoDocumento;

public final class ValidacionDocumento {

    private ValidacionDocumento() {
    }

    public static void validarDni(TipoDocumento tipoDocumento, String numeroDocumento, Errors errores) {
        if (tipoDocumento == TipoDocumento.DNI
                && (numeroDocumento == null || !numeroDocumento.matches("\\d{8}"))) {
            errores.rejectValue("numeroDocumento", "documento.dni", "El DNI debe tener exactamente 8 dígitos.");
        }
    }
}
