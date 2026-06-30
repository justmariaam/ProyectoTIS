package uv.listi.sicae.estacionamiento.service;

import uv.listi.sicae.estacionamiento.model.Movimiento;
import uv.listi.sicae.estacionamiento.model.EspacioEstacionamiento;
import uv.listi.sicae.estacionamiento.repository.MovimientoRepository;
import uv.listi.sicae.estacionamiento.repository.EspacioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uv.listi.sicae.estacionamiento.client.UsuarioCliente;
import uv.listi.sicae.estacionamiento.dto.UsuarioDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import uv.listi.sicae.estacionamiento.client.VehiculoCliente;
import uv.listi.sicae.estacionamiento.dto.VehiculoDTO;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ParkingService {

    private final MovimientoRepository movimientoRepository;
    private final EspacioRepository espacioRepository;
    private final UsuarioCliente usuarioCliente;
    private final VehiculoCliente vehiculoCliente;

    public ParkingService(MovimientoRepository movimientoRepository, EspacioRepository espacioRepository, UsuarioCliente usuarioCliente,
        VehiculoCliente vehiculoCliente) {
        this.movimientoRepository = movimientoRepository;
        this.espacioRepository = espacioRepository;
        this.usuarioCliente = usuarioCliente;
        this.vehiculoCliente = vehiculoCliente;
    }

    @Transactional
    public Movimiento registrarEntrada(Movimiento entrada) {

        if (entrada.getClaveUsuario() == null || entrada.getClaveUsuario().isEmpty()) {
            throw new IllegalArgumentException("La clave de usuario es obligatoria");
        }
        if (entrada.getPlaca() == null || entrada.getPlaca().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        if (entrada.getTarifaHora() == null) {
            throw new IllegalArgumentException("La tarifa por hora es obligatoria");
        }
        if (entrada.getIdEspacio() == null) {
            throw new IllegalArgumentException("El espacio es obligatorio");
        }

        UsuarioDTO usuario = usuarioCliente.obtenerUsuario(entrada.getClaveUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if (!usuario.getEstatus()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        try {
            VehiculoDTO vehiculo = vehiculoCliente.validarVehiculo(usuario.getIdUsuario(), entrada.getPlaca());
            if (vehiculo == null) {
                throw new IllegalArgumentException("Vehículo no asociado a la cuenta.");
            }
            if (!vehiculo.getEstatus()) {
                throw new IllegalArgumentException("El vehículo con placa " + entrada.getPlaca() + " se encuentra inactivo.");
            }
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("El vehículo con placa '" + entrada.getPlaca() + "' no está asociado a su cuenta.");
        }

        Integer cantidad = movimientoRepository.contarVehiculosDentro(entrada.getClaveUsuario());
        if (cantidad >= 2) {
            throw new IllegalArgumentException("Solo puede tener 2 vehículos dentro");
        }

        entrada.setTiempoEntrada(LocalDateTime.now());
        entrada.setTiempoCreacion(LocalDateTime.now());

        movimientoRepository.registrarEntrada(entrada);
        espacioRepository.actualizarOcupacion(entrada.getIdEspacio(), true);

        return entrada;
    }

    @Transactional
    public Movimiento registrarSalida(String placa, String claveUsuario) {

        if (claveUsuario == null || claveUsuario.isEmpty()) {
            throw new IllegalArgumentException("La clave de usuario es obligatoria");
        }
        if (placa == null || placa.isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }

        UsuarioDTO usuario = usuarioCliente.obtenerUsuario(claveUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if (!usuario.getEstatus()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        try {
            VehiculoDTO vehiculo = vehiculoCliente.validarVehiculo(usuario.getIdUsuario(), placa);
            if (vehiculo == null) {
                throw new IllegalArgumentException("Vehículo no asociado a la cuenta.");
            }
            if (!vehiculo.getEstatus()) {
                throw new IllegalArgumentException("El vehículo con placa " + placa + " se encuentra inactivo.");
            }
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("El vehículo con placa '" + placa + "' no está asociado a su cuenta.");
        }

        Movimiento movimiento = movimientoRepository.buscarActivoPorPlacaYUsuario(placa, claveUsuario);
        if (movimiento == null) {
            throw new IllegalArgumentException("No existe un movimiento de entrada activo");
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