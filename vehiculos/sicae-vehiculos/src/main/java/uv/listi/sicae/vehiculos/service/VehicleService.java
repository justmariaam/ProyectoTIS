package uv.listi.sicae.vehiculos.service;

import uv.listi.sicae.vehiculos.client.UsuarioCliente;
import uv.listi.sicae.vehiculos.dto.UsuarioDTO;
import uv.listi.sicae.vehiculos.model.Vehiculo;
import uv.listi.sicae.vehiculos.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import uv.listi.sicae.vehiculos.exception.UsuarioNoEncontradoException;

@Service
public class VehicleService {

  private final VehiculoRepository vehiculoRepository;
  private final UsuarioCliente usuarioCliente;

  public VehicleService(VehiculoRepository vehiculoRepository, UsuarioCliente usuarioCliente) {
    this.vehiculoRepository = vehiculoRepository;
    this.usuarioCliente = usuarioCliente;
  }

  private UsuarioDTO validarUsuario(Integer idUsuario) {
    try {
      UsuarioDTO usuario = usuarioCliente.obtenerUsuarioPorId(idUsuario);
      if (usuario == null) {
        throw new IllegalArgumentException("El usuario no existe.");
      }
      if (!usuario.getEstatus()) {
        throw new IllegalArgumentException("El usuario está inactivo.");
      }
      return usuario;
    } catch (UsuarioNoEncontradoException e) {
      throw new IllegalArgumentException("El usuario no existe.");
    }
  }

  @Transactional
  public void registrar(Vehiculo vehiculo) {
    validarUsuario(vehiculo.getIdUsuario());

    if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
      throw new IllegalArgumentException("La placa es obligatoria.");
    }
    if (vehiculo.getColor() == null || vehiculo.getColor().trim().isEmpty()) {
      throw new IllegalArgumentException("El color es obligatorio.");
    }
    if (vehiculo.getAnio() == null) {
        throw new IllegalArgumentException("El año es obligatorio.");
    }

    if (vehiculo.getDescripcion() == null || vehiculo.getDescripcion().trim().isEmpty()) {
      throw new IllegalArgumentException("La descripción es obligatoria.");
    }
    if (vehiculo.getIdModelo() == null) {
      throw new IllegalArgumentException("Debe seleccionar un modelo.");
    }
    if (vehiculo.getIdUsuario() == null) {
      throw new IllegalArgumentException("Debe indicar el usuario propietario.");
    }
    if (vehiculo.getPlaca().length() > 10) {
      throw new IllegalArgumentException("La placa excede el tamaño permitido.");
    }
    if (vehiculo.getColor().length() > 50) {
      throw new IllegalArgumentException("El color excede el tamaño permitido.");
    }
    if (vehiculo.getDescripcion().length() > 255) {
      throw new IllegalArgumentException("La descripción excede el tamaño permitido.");
    }

    int anioActual = java.time.Year.now().getValue();
    if (vehiculo.getAnio() < 1900 || vehiculo.getAnio() > anioActual + 1) {
      throw new IllegalArgumentException("El año ingresado no es válido.");
    }

    if (vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca()) != null) {
      throw new IllegalArgumentException("La placa vehicular ya se encuentra registrada en el sistema.");
    }

    int vehiculosActivos = vehiculoRepository.contarActivosPorUsuario(vehiculo.getIdUsuario());
    if (vehiculosActivos >= 4) {
      throw new IllegalStateException("El usuario ha alcanzado el límite máximo de 4 vehículos de forma simultánea.");
    }

    int consecutivo = vehiculoRepository.contarTotalVehiculos() + 1;
    vehiculo.setClaveVehiculo("VEH-" + String.format("%03d", consecutivo));
    vehiculo.setEstatus(true);

    vehiculoRepository.registrarVehiculo(vehiculo);
  }

  @Transactional
  public void editar(Vehiculo datosNuevos, Integer idVehiculo, Integer idUsuarioAutenticado) {
    validarUsuario(idUsuarioAutenticado);

    Vehiculo vehiculoExistente = vehiculoRepository.buscarPorId(idVehiculo);
    if (vehiculoExistente == null) {
      throw new IllegalArgumentException("El vehículo solicitado no existe.");
    }

    if (!vehiculoExistente.getIdUsuario().equals(idUsuarioAutenticado)) {
      throw new SecurityException("No tiene permisos para modificar un vehículo que no está asociado a su cuenta.");
    }

    if (datosNuevos.getPlaca() == null || datosNuevos.getPlaca().trim().isEmpty()) {
      throw new IllegalArgumentException("La placa es obligatoria.");
    }
    if (datosNuevos.getColor() == null || datosNuevos.getColor().trim().isEmpty()) {
      throw new IllegalArgumentException("El color es obligatorio.");
    }
    if (datosNuevos.getDescripcion() == null || datosNuevos.getDescripcion().trim().isEmpty()) {
      throw new IllegalArgumentException("La descripción es obligatoria.");
    }
    if (datosNuevos.getAnio() == null) {
      throw new IllegalArgumentException("El año es obligatorio.");
    }

    if (datosNuevos.getPlaca().length() > 10) {
      throw new IllegalArgumentException("La placa excede el tamaño permitido.");
    }
    if (datosNuevos.getColor().length() > 50) {
      throw new IllegalArgumentException("El color excede el tamaño permitido.");
    }
    if (datosNuevos.getDescripcion().length() > 255) {
      throw new IllegalArgumentException("La descripción excede el tamaño permitido.");
    }

    int anioActual = java.time.Year.now().getValue();
    if (datosNuevos.getAnio() < 1900 || datosNuevos.getAnio() > anioActual + 1) {
      throw new IllegalArgumentException("El año ingresado no es válido.");
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
    validarUsuario(idUsuario);
    return vehiculoRepository.buscarVehiculosPorUsuario(idUsuario);
  }

  public List<Map<String, Object>> listarPorClaveUsuario(String claveUsuario) {
    UsuarioDTO usuario = usuarioCliente.obtenerUsuarioPorClave(claveUsuario);
    if (usuario == null) {
      throw new IllegalArgumentException("El usuario no existe.");
    }
    if (!usuario.getEstatus()) {
      throw new IllegalArgumentException("El usuario está inactivo.");
    }
    return vehiculoRepository.buscarVehiculosPorUsuario(usuario.getIdUsuario());
  }

  @Transactional
  public void cambiarEstatus(Integer idVehiculo, Integer idUsuarioAutenticado, boolean nuevoEstatus) {
    validarUsuario(idUsuarioAutenticado);

    Vehiculo vehiculo = vehiculoRepository.buscarPorId(idVehiculo);
    if (vehiculo == null) {
      throw new IllegalArgumentException("Vehículo no encontrado.");
    }

    if (!vehiculo.getIdUsuario().equals(idUsuarioAutenticado)) {
      throw new SecurityException("Acceso denegado. Este vehículo no le pertenece.");
    }

    vehiculoRepository.actualizarEstatus(idVehiculo, nuevoEstatus);
  }

  public Vehiculo validarVehiculo(Integer idUsuario, String placa) {
    validarUsuario(idUsuario);
    return vehiculoRepository.buscarPorUsuarioYPlaca(idUsuario, placa);
  }
}
