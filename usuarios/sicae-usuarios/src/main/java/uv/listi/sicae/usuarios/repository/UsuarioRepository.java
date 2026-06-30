package uv.listi.sicae.usuarios.repository;

import uv.listi.sicae.usuarios.model.Usuario;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsuarioRepository {
    
    void registrarUsuario(Usuario usuario);
    void editarUsuario(Usuario usuario);
    
    Usuario buscarPorId(@Param("idUsuario") Integer idUsuario);
    Usuario buscarPorUsuario(@Param("username") String username);
    Usuario buscarPorCorreo(@Param("correo") String correo);
    Usuario buscarPorClave(String claveUsuario);
    
    void actualizarEstatus(@Param("idUsuario") Integer idUsuario, @Param("estatus") String estatus);
    
    void actualizarClaveUsuario(@Param("idUsuario") Integer idUsuario, @Param("claveUsuario") String claveUsuario);

    int contarTotalUsuarios();

    int verificarRolExistente(@Param("idRol") Integer idRol);
    
    int verificarTipoUsuarioExistente(@Param("idTipoUsuario") Integer idTipoUsuario);
    
    int verificarProgramaExistente(@Param("idProgramaEducativo") Integer idProgramaEducativo);
}