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
        return user;
    }

    @Transactional
    public void cambiarEstatus(Integer idUsuario, boolean estatus) {
        if (usuarioRepository.buscarPorId(idUsuario) == null) {
            throw new IllegalArgumentException("Usuario no registrado.");
        }
        usuarioRepository.actualizarEstatus(idUsuario, estatus);
    }
}