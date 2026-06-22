package uv.listi.sicae.usuarios.service;

import uv.listi.sicae.usuarios.model.Usuario;
import uv.listi.sicae.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrar(Usuario usuario) {
        if (usuarioRepository.buscarPorUsuario(usuario.getUsername()) != null) {
            throw new IllegalArgumentException("El username ya existe en la plataforma.");
        }
        if (usuarioRepository.buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        if (usuarioRepository.verificarRolExistente(usuario.getIdRol()) == 0) {
            throw new IllegalArgumentException("Operación rechazada: El 'idRol' " + usuario.getIdRol() + " no corresponde a ningún catálogo registrado.");
        }

        if (usuarioRepository.verificarTipoUsuarioExistente(usuario.getIdTipoUsuario()) == 0) {
            throw new IllegalArgumentException("Operación rechazada: El 'idTipoUsuario' " + usuario.getIdTipoUsuario() + " no existe en el sistema.");
        }

        if (usuario.getIdProgramaEducativo() != null) {
            if (usuarioRepository.verificarProgramaExistente(usuario.getIdProgramaEducativo()) == 0) {
                throw new IllegalArgumentException("Operación rechazada: El 'idProgramaEducativo' " + usuario.getIdProgramaEducativo() + " es inválido.");
            }
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstatus(true);
        usuario.setTiempoCreacion(LocalDateTime.now());

        String iniciales = (usuario.getNombre().substring(0, 1) + usuario.getApellidoPaterno().substring(0, 2)).toUpperCase();
        int consecutivo = usuarioRepository.contarTotalUsuarios() + 1;
        usuario.setClaveUsuario(iniciales + "-" + consecutivo);

        usuarioRepository.registrarUsuario(usuario);
    }

    @Transactional
    public void editar(Usuario datos, Integer idUsuario) {
        Usuario ex = usuarioRepository.buscarPorId(idUsuario);
        if (ex == null) throw new IllegalArgumentException("Usuario no encontrado.");

        Usuario checkCorreo = usuarioRepository.buscarPorCorreo(datos.getCorreo());
        if (checkCorreo != null && !checkCorreo.getIdUsuario().equals(idUsuario)) {
            throw new IllegalArgumentException("El correo ya está en uso por otro usuario.");
        }

        if (usuarioRepository.verificarRolExistente(datos.getIdRol()) == 0) {
            throw new IllegalArgumentException("Operación rechazada: El 'idRol' " + datos.getIdRol() + " no corresponde a ningún catálogo registrado.");
        }

        if (usuarioRepository.verificarTipoUsuarioExistente(datos.getIdTipoUsuario()) == 0) {
            throw new IllegalArgumentException("Operación rechazada: El 'idTipoUsuario' " + datos.getIdTipoUsuario() + " no existe en el sistema.");
        }

        if (datos.getIdProgramaEducativo() != null) {
            if (usuarioRepository.verificarProgramaExistente(datos.getIdProgramaEducativo()) == 0) {
                throw new IllegalArgumentException("Operación rechazada: El 'idProgramaEducativo' " + datos.getIdProgramaEducativo() + " es inválido.");
            }
        }

        ex.setNombre(datos.getNombre());
        ex.setApellidoPaterno(datos.getApellidoPaterno());
        ex.setCorreo(datos.getCorreo());
        ex.setTelefono(datos.getTelefono());
        ex.setIdRol(datos.getIdRol());
        ex.setIdTipoUsuario(datos.getIdTipoUsuario());
        ex.setIdProgramaEducativo(datos.getIdProgramaEducativo());
        ex.setTiempoActualizacion(LocalDateTime.now());

        usuarioRepository.editarUsuario(ex);
    }

    public Usuario obtenerPerfil(Integer idUsuario) {
        Usuario user = usuarioRepository.buscarPorId(idUsuario);
        if (user == null) throw new IllegalArgumentException("Perfil inexistente.");
        user.setPassword(null); 
        user.setEstatusWord(user.isEstatus() ? "ACTIVO" : "INACTIVO");
        return user;
    }

    @Transactional
    public void cambiarEstatus(Integer idUsuario, Integer idRol, boolean estatus) {
        if (idUsuario == null || idRol == null) {
            throw new IllegalArgumentException("El identificador de usuario y el rol son datos obligatorios.");
        }

        if (idRol != 1) {
            throw new IllegalArgumentException("Operación rechazada: Solo los usuarios con rol de administrador pueden cambiar el estatus.");
        }

        if (usuarioRepository.buscarPorId(idUsuario) == null) {
            throw new IllegalArgumentException("El usuario que intenta modificar no se encuentra registrado.");
        }

        usuarioRepository.actualizarEstatus(idUsuario, estatus);
    }
    
    public Usuario buscarPorClave(String clave) {
    Usuario usuario = usuarioRepository.buscarPorClave(clave);
    if(usuario == null){
        throw new IllegalArgumentException(
            "Usuario no encontrado");
    }
    return usuario;
    }
}