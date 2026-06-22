package uv.listi.sicae.vehiculos.controller;

import uv.listi.sicae.vehiculos.model.Vehiculo;
import uv.listi.sicae.vehiculos.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Vehiculo vehiculo) {
        try {
            vehicleService.registrar(vehiculo);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Vehículo registrado y asociado correctamente.");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @PutMapping("/usuario/{idUsuario}/vehiculo/{idVehiculo}")
    public ResponseEntity<?> editar(
            @RequestBody Vehiculo vehiculo,
            @PathVariable("idUsuario") Integer idUsuarioPath,
            @PathVariable("idVehiculo") Integer idVehiculo) {

      try {
        Integer idUsuarioToken = obtenerIdUsuarioDelToken();

        if (!idUsuarioPath.equals(idUsuarioToken)) {
          return construirRespuestaError("No tienes permisos para editar vehículos de otro usuario.");
        }

        if (vehiculo.getIdVehiculo() != null && !vehiculo.getIdVehiculo().equals(idVehiculo)) {
          return construirRespuestaError("El ID del vehículo en la URL no coincide con el del cuerpo.");
        }

        vehicleService.editar(vehiculo, idVehiculo, idUsuarioToken);

        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "Información del vehículo modificada de manera exitosa.");
        return ResponseEntity.ok(res);

      } catch (Exception e) {
        return construirRespuestaError(e.getMessage());
      }
    }

    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> buscarVehiculosPorUsuario(@PathVariable("idUsuario") Integer idUsuario) {
      try {
        if (idUsuario == null) {
          return construirRespuestaError("El idUsuario es obligatorio en la URL.");
        }
        List<Map<String, Object>> lista = vehicleService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(lista);
      } catch (Exception e) {
        return construirRespuestaError(e.getMessage());
      }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstatus(@PathVariable("id") Integer idVehiculo,
            @RequestBody Map<String, Object> payload) {
      try {
        Integer idUsuarioJson = (Integer) payload.get("idUsuario");
        Integer idUsuarioToken = obtenerIdUsuarioDelToken();        
        String nuevoEstatus = (String) payload.get("estatus");
        
        if (!idUsuarioJson.equals(idUsuarioToken)) {
          return construirRespuestaError("El ID de usuario no coincide con el usuario autenticado.");
        }
        vehicleService.cambiarEstatus(idVehiculo, idUsuarioToken, nuevoEstatus);

        Map<String, String> res = new HashMap<>();
        res.put("mensaje", "El estatus del vehículo se actualizó a: " + nuevoEstatus);
        return ResponseEntity.ok(res);

      } catch (Exception e) {
        return construirRespuestaError(e.getMessage());
      }
    }

    private ResponseEntity<?> construirRespuestaError(String msg) {
        Map<String, String> error = new HashMap<>();
        error.put("error", msg);
        return ResponseEntity.badRequest().body(error);
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validarVehiculo(
            @RequestParam Integer idUsuario,
            @RequestParam String placa){
            
        Vehiculo vehiculo =
                vehicleService.validarVehiculo(
                        idUsuario,placa);
        return ResponseEntity.ok(vehiculo);
    }
    
    @GetMapping("/user/clave/{claveUsuario}")
    public ResponseEntity<?> buscarVehiculosPorClaveUsuario(@PathVariable("claveUsuario") String claveUsuario) {
      try {
        List<Map<String, Object>> lista = vehicleService.listarPorClaveUsuario(claveUsuario);
        return ResponseEntity.ok(lista);
      } catch (Exception e) {
        return construirRespuestaError(e.getMessage());
      }
    }
    
    private Integer obtenerIdUsuarioDelToken() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null || auth.getDetails() == null) {
        throw new SecurityException("No se pudo obtener la información del usuario autenticado.");
      }
      return (Integer) auth.getDetails();
    }

}