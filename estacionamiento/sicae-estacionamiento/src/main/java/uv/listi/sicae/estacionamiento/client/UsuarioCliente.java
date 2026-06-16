/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.estacionamiento.client;
/**
 *
 * @author marian
 */
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uv.listi.sicae.estacionamiento.dto.UsuarioDTO;

@Service
public class UsuarioCliente {
    private final RestTemplate restTemplate;
    public UsuarioCliente(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO obtenerUsuario(String claveUsuario) {
        return restTemplate.getForObject(
            "http://localhost:8082/api/users/clave/" + claveUsuario,
            UsuarioDTO.class
        );
    }
}