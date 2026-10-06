package pe.com.sikalma.entity;

public enum TipoDocumento {
    DNI("DNI"),
    CARNET_EXTRANJERIA("Carné de extranjería"),
    PASAPORTE("Pasaporte"),
    OTRO("Otro");

    private final String etiqueta;

    TipoDocumento(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
