package uv.listi.sicae.usuarios.service;

import uv.listi.sicae.usuarios.repository.AuthRepository;
import uv.listi.sicae.usuarios.security.JwtUtil;
import uv.listi.sicae.usuarios.model.AuthResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

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
            throw new IllegalArgumentException("El nombre de usuario no existe.");
        }

        Object estatusObj = userDB.get("estatus");
        boolean isActivo = estatusObj instanceof Boolean ? (Boolean) estatusObj : "1".equals(String.valueOf(estatusObj));
        
        if (!isActivo) {
            throw new IllegalStateException("El usuario se encuentra inactivo en el sistema.");
        }

        String hashDB = (String) userDB.get("password");
        if (!passwordEncoder.matches(password, hashDB)) {
            throw new SecurityException("Contraseña incorrecta.");
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