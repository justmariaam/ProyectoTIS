/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.estacionamiento.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uv.listi.sicae.estacionamiento.dto.VehiculoDTO;

/**
 *
 * @author marian
 */

@Service
public class VehiculoCliente {
    private final RestTemplate restTemplate;
    public VehiculoCliente(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public VehiculoDTO validarVehiculo(
        Integer idUsuario,
        String placa){
        return restTemplate.getForObject(
            "http://localhost:8083/api/vehicles/validate?"
            + "idUsuario=" + idUsuario
            + "&placa=" + placa,
            VehiculoDTO.class
        );
    }
}
