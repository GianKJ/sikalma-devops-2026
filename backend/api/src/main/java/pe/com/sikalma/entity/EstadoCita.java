package pe.com.sikalma.entity;

public enum EstadoCita {
    PENDIENTE("Pendiente", "status--pending"),
    CONFIRMADA("Confirmada", "status--confirmed"),
    EN_SALA("En sala", "status--room"),
    ATENDIDA("Atendida", "status--active"),
    CANCELADA("Cancelada", "status--inactive");

    private final String etiqueta;
    private final String claseCss;

    EstadoCita(String etiqueta, String claseCss) {
        this.etiqueta = etiqueta;
        this.claseCss = claseCss;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getClaseCss() {
        return claseCss;
    }
}
