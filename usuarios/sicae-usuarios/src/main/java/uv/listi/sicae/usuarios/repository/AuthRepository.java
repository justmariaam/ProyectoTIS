package uv.listi.sicae.usuarios.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Mapper
public interface AuthRepository {
    Map<String, Object> buscarUsuarioPorUsername(@Param("username") String username);
}