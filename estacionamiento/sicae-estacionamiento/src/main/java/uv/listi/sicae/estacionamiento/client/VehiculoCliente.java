/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.estacionamiento.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uv.listi.sicae.estacionamiento.dto.VehiculoDTO;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class VehiculoCliente {
    private final RestTemplate restTemplate;

    public VehiculoCliente(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VehiculoDTO validarVehiculo(Integer idUsuario, String placa) {
        String token = obtenerTokenDelContexto();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<VehiculoDTO> response = restTemplate.exchange(
            "http://sicae-vehiculos-app:8083/api/vehicles/validate?idUsuario=" + idUsuario + "&placa=" + placa,
            HttpMethod.GET,
            entity,
            VehiculoDTO.class
        );
        
        return response.getBody();
    }
    
    private String obtenerTokenDelContexto() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }
}