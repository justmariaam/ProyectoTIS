package uv.listi.sicae.estacionamiento.repository;

import uv.listi.sicae.estacionamiento.model.EspacioEstacionamiento;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EspacioRepository {
    void actualizarOcupacion(@Param("idEspacio") Integer idEspacio, @Param("ocupado") boolean ocupado);
    List<EspacioEstacionamiento> obtenerEspaciosDisponibles();
}