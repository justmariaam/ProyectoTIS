package uv.listi.sicae.estacionamiento.controller;

import uv.listi.sicae.estacionamiento.model.Movimiento;
import uv.listi.sicae.estacionamiento.model.EspacioEstacionamiento;
import uv.listi.sicae.estacionamiento.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/entry")
    public ResponseEntity<?> registrarEntrada(@RequestBody Movimiento movimiento) {
        try {
            Movimiento resultado = parkingService.registrarEntrada(movimiento);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<?> registrarSalida(@RequestBody Map<String, String> payload) {
        try {
            String placa = payload.get("placa");
            String claveUsuario = payload.get("claveUsuario");
            
            Movimiento resultado = parkingService.registrarSalida(placa, claveUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    @GetMapping("/spaces")
    public ResponseEntity<?> consultarEspacios() {
        try {
            List<EspacioEstacionamiento> disponibles = parkingService.listarDisponibles();
            return ResponseEntity.ok(disponibles);
        } catch (Exception e) {
            return construirRespuestaError(e.getMessage());
        }
    }

    private ResponseEntity<?> construirRespuestaError(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return ResponseEntity.badRequest().body(error);
    }
}