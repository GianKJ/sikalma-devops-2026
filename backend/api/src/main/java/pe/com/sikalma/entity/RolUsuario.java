package pe.com.sikalma.entity;

public enum RolUsuario {
    ADMINISTRADOR("Administrador", "/admin/inicio", "Panel integral de dirección y calidad"),
    RECEPCIONISTA("Recepcionista", "/recepcion/inicio", "Gestión ágil de agenda y admisión"),
    PSICOLOGO("Psicólogo", "/profesional/inicio", "Agenda clínica y evolución terapéutica"),
    FISIOTERAPEUTA("Fisioterapeuta", "/profesional/inicio", "Agenda clínica y rehabilitación física");

    private final String etiqueta;
    private final String rutaInicio;
    private final String descripcionBienvenida;

    RolUsuario(String etiqueta, String rutaInicio, String descripcionBienvenida) {
        this.etiqueta = etiqueta;
        this.rutaInicio = rutaInicio;
        this.descripcionBienvenida = descripcionBienvenida;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getRutaInicio() {
        return rutaInicio;
    }

    public String getDescripcionBienvenida() {
        return descripcionBienvenida;
    }
}
