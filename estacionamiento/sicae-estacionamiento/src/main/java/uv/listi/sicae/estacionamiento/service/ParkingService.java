package uv.listi.sicae.estacionamiento.service;

import uv.listi.sicae.estacionamiento.model.Movimiento;
import uv.listi.sicae.estacionamiento.model.EspacioEstacionamiento;
import uv.listi.sicae.estacionamiento.repository.MovimientoRepository;
import uv.listi.sicae.estacionamiento.repository.EspacioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    private final MovimientoRepository movimientoRepository;
    private final EspacioRepository espacioRepository;

    public ParkingService(MovimientoRepository movimientoRepository, EspacioRepository espacioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.espacioRepository = espacioRepository;
    }

    @Transactional
    public Movimiento registrarEntrada(Movimiento entrada) {

        entrada.setTiempoEntrada(LocalDateTime.now());
        entrada.setTiempoCreacion(LocalDateTime.now());
        movimientoRepository.registrarEntrada(entrada);
        
        espacioRepository.actualizarOcupacion(entrada.getIdEspacio(), true);
        
        return entrada;
    }

    @Transactional
    public Movimiento registrarSalida(String placa, String claveUsuario) {
        Movimiento movimiento = movimientoRepository.buscarActivoPorPlacaYUsuario(placa, claveUsuario);
        if (movimiento == null) {
            throw new IllegalArgumentException("No existe un movimiento de entrada activo para los datos proporcionados.");
        }

        LocalDateTime tiempoSalida = LocalDateTime.now();
        movimiento.setTiempoSalida(tiempoSalida);
        movimiento.setTiempoActualizacion(tiempoSalida);

        long minutosTotales = Duration.between(movimiento.getTiempoEntrada(), tiempoSalida).toMinutes();
        movimiento.setMinutosEstacionado((int) minutosTotales);

        BigDecimal horasCobradas = BigDecimal.valueOf(minutosTotales)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.CEILING);
        movimiento.setHorasCobradas(horasCobradas);

        BigDecimal costoTotal = horasCobradas.multiply(movimiento.getTarifaHora()).setScale(2, RoundingMode.HALF_UP);
        movimiento.setCostoTotal(costoTotal);

        movimientoRepository.registrarSalida(movimiento);
        espacioRepository.actualizarOcupacion(movimiento.getIdEspacio(), false);

        return movimiento;
    }

    public List<EspacioEstacionamiento> listarDisponibles() {
        return espacioRepository.obtenerEspaciosDisponibles();
    }
}