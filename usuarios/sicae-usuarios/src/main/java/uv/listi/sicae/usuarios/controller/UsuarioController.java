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

    public UsuarioController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            userService.registrar(usuario);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Usuario registrado exitosamente.");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            return construirError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable("id") Integer id) {
        try {
            userService.editar(usuario, id);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Información de usuario modificada correctamente.");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return construirError(e.getMessage());
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
    public ResponseEntity<?> cambiarEstatus(@PathVariable("id") Integer id, @RequestBody Map<String, Boolean> payload) {
        try {
            userService.cambiarEstatus(id, payload.get("estatus"));
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Estatus del usuario actualizado.");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return construirError(e.getMessage());
        }
    }

    private ResponseEntity<?> construirError(String msg) {
        Map<String, String> err = new HashMap<>();
        err.put("error", msg);
        return ResponseEntity.badRequest().body(err);
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