package pe.com.sikalma.entity;

public enum ModalidadAtencion {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual"),
    AMBAS("Presencial y virtual");

    private final String etiqueta;

    ModalidadAtencion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
