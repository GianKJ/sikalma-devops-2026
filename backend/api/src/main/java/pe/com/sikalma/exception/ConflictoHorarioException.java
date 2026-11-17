package pe.com.sikalma.exception;

import java.io.Serial;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConflictoHorarioException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<LocalTime> horariosAlternativos;

    public ConflictoHorarioException(String message, List<LocalTime> horariosAlternativos) {
        super(message);
        this.horariosAlternativos = new ArrayList<>(horariosAlternativos);
    }

    public List<LocalTime> getHorariosAlternativos() {
        return List.copyOf(horariosAlternativos);
    }
}
