package uv.listi.sicae.vehiculos.controller;

import uv.listi.sicae.vehiculos.model.Vehiculo;
import uv.listi.sicae.vehiculos.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Vehiculo vehiculo, @RequestHeader("Authorization") String token) {
        try {
            vehicleService.registrar(vehiculo);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Vehículo registrado y asociado correctamente.");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Vehiculo vehiculo, 
                                    @PathVariable("id") Integer idVehiculo,
                                    @RequestParam("idUsuarioAutenticado") Integer idUsuario,
                                    @RequestHeader("Authorization") String token) {
        try {
            vehicleService.editar(vehiculo, idVehiculo, idUsuario);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Información del vehículo modificada de manera exitosa.");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> buscarVehiculosPorUsuario(@PathVariable("idUsuario") Integer idUsuario, @RequestHeader("Authorization") String token) {
        try {
            List<Map<String, Object>> lista = vehicleService.listarPorUsuario(idUsuario);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cambiarEstatus(@PathVariable("id") Integer idVehiculo,
                                             @RequestBody Map<String, Object> payload,
                                             @RequestHeader("Authorization") String token) {
        try {
            Integer idUsuario = (Integer) payload.get("idUsuario");
            boolean estatus = (boolean) payload.get("estatus");
            
            vehicleService.cambiarEstatus(idVehiculo, idUsuario, estatus);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "El estatus del vehículo se actualizó correctamente.");
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
}