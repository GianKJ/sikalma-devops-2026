package pe.com.sikalma.dto;

import java.util.List;

import pe.com.sikalma.entity.Cita;

public record ResumenMetricasDto(
        long totalPacientes,
        long pacientesActivos,
        long pacientesConConsentimiento,
        long totalPsicologos,
        long totalServicios,
        long totalEspecialidades,
        long totalUsuarios,
        long totalCitas,
        long citasHoy,
        long citasPendientes,
        long citasConfirmadas,
        long citasEnSala,
        long citasAtendidas,
        long citasCanceladas,
        int tasaAsistenciaPorcentaje,
        long citasPresenciales,
        long citasVirtuales,
        List<SerieGraficoDto> citasPorEstado,
        List<SerieGraficoDto> citasPorServicio,
        List<SerieGraficoDto> citasPorModalidad,
        List<Cita> proximasCitas) {
}
