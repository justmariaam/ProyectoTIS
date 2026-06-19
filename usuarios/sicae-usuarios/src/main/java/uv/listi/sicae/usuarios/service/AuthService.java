package uv.listi.sicae.usuarios.service;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uv.listi.sicae.usuarios.model.AuthResponse;
import uv.listi.sicae.usuarios.repository.AuthRepository;
import uv.listi.sicae.usuarios.security.JwtUtil;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AuthRepository authRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(String username, String password) {
        Map<String, Object> userDB = authRepository.buscarUsuarioPorUsername(username);
        if (userDB == null) {
            throw new IllegalArgumentException("Contraseña y/o nombre de usuario incorrectos.");
        }

        Object estatusObj = userDB.get("estatus");
        boolean isActivo = estatusObj instanceof Boolean ? (Boolean) estatusObj : "1".equals(String.valueOf(estatusObj));
        
        if (!isActivo) {
            throw new IllegalStateException("El usuario se encuentra inactivo en el sistema.");
        }

        String hashDB = (String) userDB.get("password");
        if (!passwordEncoder.matches(password, hashDB)) {
            throw new SecurityException("Contraseña y/o nombre de usuario incorrectos.");
        }

        String token = jwtUtil.generarToken(username, (String) userDB.get("rol"), (Integer) userDB.get("idUsuario"));
        String nombreCompleto = userDB.get("nombre") + " " + userDB.get("apellidoPaterno");

        return new AuthResponse(
            (Integer) userDB.get("idUsuario"),
            (Integer) userDB.get("idRol"),
            (String) userDB.get("rol"),
            username,
            nombreCompleto,
            (Integer) userDB.get("idTipoUsuario"),
            (String) userDB.get("tipoUsuario"),
            token
        );
    }
}