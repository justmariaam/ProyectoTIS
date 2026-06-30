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
            throw new IllegalArgumentException("El 'idRol' " + usuario.getIdRol() + " no existe en el sistema.");
        }

        if (usuarioRepository.verificarTipoUsuarioExistente(usuario.getIdTipoUsuario()) == 0) {
            throw new IllegalArgumentException("El 'idTipoUsuario' " + usuario.getIdTipoUsuario() + " no existe en el sistema.");
        }

        if (usuario.getIdProgramaEducativo() != null) {
            if (usuarioRepository.verificarProgramaExistente(usuario.getIdProgramaEducativo()) == 0) {
                throw new IllegalArgumentException("El 'idProgramaEducativo' " + usuario.getIdProgramaEducativo() + " no existe en el sistema.");
            }
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstatus("activo");
        usuario.setTiempoCreacion(LocalDateTime.now());
        usuario.setClaveUsuario("TEMP");

        usuarioRepository.registrarUsuario(usuario);

        String iniciales = (usuario.getNombre().substring(0, 1) + usuario.getApellidoPaterno().substring(0, 2)).toUpperCase();
        String claveGenerada = iniciales + "-" + String.format("%03d", usuario.getIdUsuario());

        if (usuarioRepository.buscarPorClave(claveGenerada) != null) {
            throw new IllegalStateException("Error al generar la clave de usuario: la clave generada ya existe. Por favor, intente registrar al usuario de nuevo.");
        }

        usuarioRepository.actualizarClaveUsuario(usuario.getIdUsuario(), claveGenerada);
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
            throw new IllegalArgumentException("El 'idRol' " + datos.getIdRol() + " no existe en el sistema.");
        }

        if (usuarioRepository.verificarTipoUsuarioExistente(datos.getIdTipoUsuario()) == 0) {
            throw new IllegalArgumentException("El 'idTipoUsuario' " + datos.getIdTipoUsuario() + " no existe en el sistema.");
        }

        if (datos.getIdProgramaEducativo() != null) {
            if (usuarioRepository.verificarProgramaExistente(datos.getIdProgramaEducativo()) == 0) {
                throw new IllegalArgumentException("El 'idProgramaEducativo' " + datos.getIdProgramaEducativo() + " no existe en el sistema.");
            }
        }

        ex.setNombre(datos.getNombre());
        ex.setApellidoPaterno(datos.getApellidoPaterno());
        ex.setCorreo(datos.getCorreo());
        ex.setTelefono(datos.getTelefono());
        ex.setIdRol(datos.getIdRol());
        ex.setIdTipoUsuario(datos.getIdTipoUsuario());
        ex.setIdProgramaEducativo(datos.getIdProgramaEducativo());
        ex.setEstatus(datos.getEstatus());
        ex.setTiempoActualizacion(LocalDateTime.now());

        usuarioRepository.editarUsuario(ex);
    }

    public Usuario obtenerPerfil(Integer idUsuario) {
        Usuario user = usuarioRepository.buscarPorId(idUsuario);
        if (user == null) throw new IllegalArgumentException("Perfil inexistente.");
        user.setPassword(null);
        return user;
    }

    @Transactional
    public void cambiarEstatus(Integer idUsuario, String estatus) {
        String estatusNormalizado = estatus.toLowerCase();
        Usuario usuarioObjetivo = usuarioRepository.buscarPorId(idUsuario);
        if (usuarioObjetivo == null) {
            throw new IllegalArgumentException("Operación rechazada: El usuario objetivo no se encuentra registrado en el sistema.");
        }

        if (idUsuario == 1 && estatus.equalsIgnoreCase("inactivo")) {
            throw new IllegalArgumentException("Operación rechazada: No es posible desactivar la cuenta principal del Administrador del sistema.");
        }

        usuarioRepository.actualizarEstatus(idUsuario, estatusNormalizado);
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