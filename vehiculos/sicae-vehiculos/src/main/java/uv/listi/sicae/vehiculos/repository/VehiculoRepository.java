package uv.listi.sicae.vehiculos.repository;

import uv.listi.sicae.vehiculos.model.Vehiculo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface VehiculoRepository {
    void registrarVehiculo(Vehiculo vehiculo);
    void editarVehiculo(Vehiculo vehiculo);
    Vehiculo buscarPorId(@Param("idVehiculo") Integer idVehiculo);
    Vehiculo buscarPorPlaca(@Param("placa") String placa);
    int contarActivosPorUsuario(@Param("idUsuario") Integer idUsuario);
    int contarTotalVehiculos();
    List<Map<String, Object>> buscarVehiculosPorUsuario(@Param("idUsuario") Integer idUsuario);
    void actualizarEstatus(@Param("idVehiculo") Integer idVehiculo, @Param("estatus") boolean estatus);
}