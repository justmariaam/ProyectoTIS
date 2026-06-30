package uv.listi.sicae.usuarios.controller;

import uv.listi.sicae.usuarios.model.Usuario;
import uv.listi.sicae.usuarios.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(
    origins = "*", 
    allowedHeaders = "*", 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final UserService userService;
    
    private static final String REGEX_CORREO = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";

    public UsuarioController(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<?> validarCampos(Usuario usuario, Boolean edicion) {
        if (edicion != null && edicion) {
            if (usuario.getUsername() != null || usuario.getPassword() != null || usuario.getClaveUsuario() != null) {
                Map<String, String> errorCampos = new HashMap<>();
                errorCampos.put("mensaje", "No se permite la edición directa de usuario, contraseña ni clave de usuario.");
                return ResponseEntity.badRequest().body(errorCampos);
            }
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return construirError("El campo 'nombre' es obligatorio.");
        }
        if (usuario.getNombre().length() > 50) {
            return construirError("El campo 'nombre' no puede exceder los 50 caracteres.");
        }

        if (usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno().trim().isEmpty()) {
            return construirError("El campo 'apellidoPaterno' es obligatorio.");
        }
        if (usuario.getApellidoPaterno().length() > 50) {
            return construirError("El campo 'apellidoPaterno' no puede exceder los 50 caracteres.");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            return construirError("El campo 'correo' es obligatorio.");
        }
        if (usuario.getCorreo().length() > 255) {
            return construirError("El campo 'correo' no puede exceder los 255 caracteres.");
        }

        if (edicion == null || !edicion) {
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                return construirError("El campo 'username' es obligatorio.");
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return construirError("El campo 'password' es obligatorio.");
            }
            if (usuario.getUsername().length() > 30) {
                return construirError("El campo 'username' no puede exceder los 30 caracteres.");
            }
        }

        if (usuario.getIdRol() == null || usuario.getIdTipoUsuario() == null) {
            return construirError("Los campos 'idRol' y 'idTipoUsuario' son obligatorios.");
        }

        if (usuario.getCorreo() == null || !usuario.getCorreo().matches(REGEX_CORREO)) {
            return construirError("El formato del correo electrónico es inválido (Ej: usuario@uv.mx).");
        }

        if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) {
            if (!usuario.getTelefono().trim().matches(REGEX_TELEFONO)) {
                return construirError("El teléfono debe contener exactamente 10 dígitos numéricos.");
            }
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            ResponseEntity<?> errorResponse = validarCampos(usuario, false);
            if (errorResponse != null) {
                return errorResponse;
            }

            userService.registrar(usuario);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Usuario registrado correctamente.");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return construirError(e.getMessage());
        } catch (Exception e) {
            return construirErrorServidor(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable("id") Integer id) {
        try {
            ResponseEntity<?> errorResponse = validarCampos(usuario, true);
            if (errorResponse != null) {
                return errorResponse;
            }

            userService.editar(usuario, id);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Información de usuario modificada correctamente.");
            return ResponseEntity.ok(res);
            
        } catch (IllegalArgumentException e) {
            return construirError(e.getMessage());
        } catch (Exception e) {
            return construirErrorServidor(e.getMessage());
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> verPerfil(@PathVariable("id") Integer id) {
        try {
            Usuario user = userService.obtenerPerfil(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return construirError(e.getMessage());
        } catch (Exception e) {
            return construirErrorServidor(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstatus(@PathVariable("id") Integer id, @RequestBody Map<String, Object> payload) {
        try {
            String estatus = (String) payload.get("estatus");
            
            if (estatus == null || estatus.trim().isEmpty()) {
                return mandarErrorJson("El campo 'estatus' es obligatorio.");
            }

            if (!estatus.equalsIgnoreCase("activo") && !estatus.equalsIgnoreCase("inactivo")) {
                return mandarErrorJson("El campo 'estatus' debe ser 'activo' o 'inactivo'.");
            }

            userService.cambiarEstatus(id, estatus);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Estatus del usuario actualizado a '" + estatus.toLowerCase() + "' correctamente.");
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return construirError(e.getMessage());
        } catch (Exception e) {
            return construirErrorServidor(e.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> mandarErrorJson(String mensaje) {
        Map<String, String> err = new HashMap<>();
        err.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(err);
    }

    private ResponseEntity<?> construirError(String msg) {
        Map<String, String> err = new HashMap<>();
        err.put("error", msg);
        return ResponseEntity.badRequest().body(err);
    }

    private ResponseEntity<?> construirErrorServidor(String msg) {
        Map<String, String> err = new HashMap<>();
        err.put("mensaje", "Fallo interno en el microservicio: " + msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
    
    @GetMapping("/clave/{clave}")
    public ResponseEntity<?> buscarPorClave(
            @PathVariable("clave") String clave) {

        try {
            Usuario usuario = userService.buscarPorClave(clave);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("idUsuario", usuario.getIdUsuario());
            respuesta.put("claveUsuario", usuario.getClaveUsuario());
            respuesta.put("estatus", "activo".equalsIgnoreCase(usuario.getEstatus()));
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return construirError(e.getMessage());
        }
    }
}