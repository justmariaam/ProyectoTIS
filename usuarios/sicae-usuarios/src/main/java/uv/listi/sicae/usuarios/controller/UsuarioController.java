package uv.listi.sicae.usuarios.controller;

import uv.listi.sicae.usuarios.model.Usuario;
import uv.listi.sicae.usuarios.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final UserService userService;
    
    private static final String REGEX_CORREO = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String REGEX_TELEFONO = "^\\d{10}$";

    public UsuarioController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty() ||
                usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno().trim().isEmpty() ||
                usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty() ||
                usuario.getUsername() == null || usuario.getUsername().trim().isEmpty() ||
                usuario.getPassword() == null || usuario.getPassword().trim().isEmpty() ||
                usuario.getIdRol() == null || usuario.getIdTipoUsuario() == null) {
                
                return construirError("Los campos obligatorios para el registro no pueden estar vacíos.");
            }

            if (usuario.getCorreo() == null || !usuario.getCorreo().matches(REGEX_CORREO)) {
                return construirError("Operación rechazada: El formato del correo electrónico es inválido (Ej: usuario@uv.mx).");
            }

            if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) {
                if (!usuario.getTelefono().trim().matches(REGEX_TELEFONO)) {
                    return construirError("Operación rechazada: El teléfono debe contener exactamente 10 dígitos numéricos.");
                }
            }

            if (usuario.getEstatusWord() != null && 
                !usuario.getEstatusWord().equalsIgnoreCase("ACTIVO") && 
                !usuario.getEstatusWord().equalsIgnoreCase("INACTIVO")) {
                return construirError("El estatus debe ser estrictamente la palabra 'ACTIVO' o 'INACTIVO'.");
            }

            userService.registrar(usuario);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Usuario registrado exitosamente.");
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
            if (usuario.getUsername() != null || usuario.getPassword() != null || usuario.getClaveUsuario() != null) {
                Map<String, String> errorCampos = new HashMap<>();
                errorCampos.put("mensaje", "Operación rechazada: No se permite la edición directa de usuario, contraseña ni clave de usuario.");
                return ResponseEntity.badRequest().body(errorCampos);
            }

            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty() ||
                usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno().trim().isEmpty() ||
                usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty() ||
                usuario.getIdRol() == null || usuario.getIdTipoUsuario() == null) {
                
                return construirError("Los campos modificables obligatorios no pueden estar vacíos.");
            }

            if (usuario.getCorreo() == null || !usuario.getCorreo().matches(REGEX_CORREO)) {
                return construirError("Operación rechazada: El formato del correo electrónico es inválido (Ej: usuario@uv.mx).");
            }

            if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) {
                if (!usuario.getTelefono().trim().matches(REGEX_TELEFONO)) {
                    return construirError("Operación rechazada: El teléfono debe contener exactamente 10 dígitos numéricos.");
                }
            }

            if (usuario.getEstatusWord() != null && 
                !usuario.getEstatusWord().equalsIgnoreCase("ACTIVO") && 
                !usuario.getEstatusWord().equalsIgnoreCase("INACTIVO")) {
                return construirError("El estatus modificado debe ser la palabra 'ACTIVO' o 'INACTIVO'.");
            }

            userService.editar(usuario, id);
            
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Información de usuario modificada correctamente.");
            return ResponseEntity.ok(res);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> errorNegocio = new HashMap<>();
            errorNegocio.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorNegocio);
            
        } catch (Exception e) {
            Map<String, String> errorServidor = new HashMap<>();
            errorServidor.put("mensaje", "No se pudo completar la operación debido a un fallo interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorServidor);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> verPerfil(@PathVariable("id") Integer id) {
        try {
            Usuario user = userService.obtenerPerfil(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return construirError(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstatus(@PathVariable("id") Integer id, @RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("idRol") || payload.get("idRol") == null) {
                return construirError("El campo 'idRol' es obligatorio para autorizar esta operación.");
            }
            if (!payload.containsKey("estatus") || payload.get("estatus") == null) {
                return construirError("El campo 'estatus' es obligatorio.");
            }
            
            Integer idRol = (Integer) payload.get("idRol");
            String estatusWord = payload.get("estatus").toString().trim();

            if (!estatusWord.equalsIgnoreCase("ACTIVO") && !estatusWord.equalsIgnoreCase("INACTIVO")) {
                return construirError("Operación rechazada: El campo 'estatus' debe contener la palabra 'ACTIVO' o 'INACTIVO'.");
            }

            boolean estatusBool = estatusWord.equalsIgnoreCase("ACTIVO");

            userService.cambiarEstatus(id, idRol, estatusBool);
            
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Estatus del usuario actualizado a '" + estatusWord.toUpperCase() + "' correctamente.");
            return ResponseEntity.ok(res);
            
        } catch (IllegalArgumentException e) {
            return construirError(e.getMessage());
        } catch (Exception e) {
            return construirErrorServidor(e.getMessage());
        }
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
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return construirError(e.getMessage());
        }
    }
}