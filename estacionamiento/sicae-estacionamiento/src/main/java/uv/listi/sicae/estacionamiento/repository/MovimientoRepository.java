package uv.listi.sicae.estacionamiento.repository;

import uv.listi.sicae.estacionamiento.model.Movimiento;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovimientoRepository {
    void registrarEntrada(Movimiento movimiento);
    Movimiento buscarActivoPorPlacaYUsuario(@Param("placa") String placa, @Param("claveUsuario") String claveUsuario);
    void registrarSalida(Movimiento movimiento);
    Integer contarVehiculosDentro(@Param("claveUsuario") String claveUsuario);
}