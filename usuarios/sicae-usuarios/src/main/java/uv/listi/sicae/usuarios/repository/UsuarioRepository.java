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
    void actualizarEstatus(@Param("idUsuario") Integer idUsuario, @Param("estatus") boolean estatus);
    int contarTotalUsuarios();
}