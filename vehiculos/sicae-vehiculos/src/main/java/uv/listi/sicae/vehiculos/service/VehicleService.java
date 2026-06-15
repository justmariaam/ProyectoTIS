package uv.listi.sicae.vehiculos.service;

import uv.listi.sicae.vehiculos.model.Vehiculo;
import uv.listi.sicae.vehiculos.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class VehicleService {

    private final VehiculoRepository vehiculoRepository;

    public VehicleService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional
    public void registrar(Vehiculo vehiculo) {
        if (vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("La placa vehicular ya se encuentra registrada en el sistema.");
        }

        int vehiculosActivos = vehiculoRepository.contarActivosPorUsuario(vehiculo.getIdUsuario());
        if (vehiculosActivos >= 4) {
            throw new IllegalStateException("El usuario ha alcanzado el límite máximo de 4 vehículos de forma simultánea.");
        }

        int consecutivo = vehiculoRepository.contarTotalVehiculos() + 1;
        vehiculo.setClaveVehiculo("VEH-" + String.format("%03d", consecutivo));

        vehiculoRepository.registrarVehiculo(vehiculo);
    }

    @Transactional
    public void editar(Vehiculo datosNuevos, Integer idVehiculo, Integer idUsuarioAutenticado) {
        Vehiculo vehiculoExistente = vehiculoRepository.buscarPorId(idVehiculo);
        if (vehiculoExistente == null) {
            throw new IllegalArgumentException("El vehículo solicitado no existe.");
        }

        if (!vehiculoExistente.getIdUsuario().equals(idUsuarioAutenticado)) {
            throw new SecurityException("No tiene permisos para modificar un vehículo que no está asociado a su cuenta.");
        }

        Vehiculo vehiculoConMismaPlaca = vehiculoRepository.buscarPorPlaca(datosNuevos.getPlaca());
        if (vehiculoConMismaPlaca != null && !vehiculoConMismaPlaca.getIdVehiculo().equals(idVehiculo)) {
            throw new IllegalArgumentException("La nueva placa ya pertenece a otro vehículo registrado.");
        }

        vehiculoExistente.setIdModelo(datosNuevos.getIdModelo());
        vehiculoExistente.setPlaca(datosNuevos.getPlaca());
        vehiculoExistente.setColor(datosNuevos.getColor());
        vehiculoExistente.setAnio(datosNuevos.getAnio());
        vehiculoExistente.setDescripcion(datosNuevos.getDescripcion());

        vehiculoRepository.editarVehiculo(vehiculoExistente);
    }

    public List<Map<String, Object>> listarPorUsuario(Integer idUsuario) {
        return vehiculoRepository.buscarVehiculosPorUsuario(idUsuario);
    }

    @Transactional
    public void cambiarEstatus(Integer idVehiculo, Integer idUsuarioAutenticado, boolean nuevoEstatus) {
        Vehiculo vehiculo = vehiculoRepository.buscarPorId(idVehiculo);
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehículo no encontrado.");
        }

        if (!vehiculo.getIdUsuario().equals(idUsuarioAutenticado)) {
            throw new SecurityException("Acceso denegado. Este vehículo no le pertenece.");
        }

        vehiculoRepository.actualizarEstatus(idVehiculo, nuevoEstatus);
    }
}