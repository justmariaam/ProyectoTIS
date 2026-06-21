/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.vehiculos.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uv.listi.sicae.vehiculos.dto.UsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.HttpClientErrorException;
import uv.listi.sicae.vehiculos.exception.UsuarioNoEncontradoException;

@Service
public class UsuarioCliente {
    
    private final RestTemplate restTemplate;

    public UsuarioCliente(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioDTO obtenerUsuarioPorId(Integer idUsuario) {
    try {
      String token = obtenerTokenDelContexto();

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + token);

      HttpEntity<?> entity = new HttpEntity<>(headers);

      ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
              "http://sicae-usuarios-app:8082/api/users/profile/" + idUsuario,
              HttpMethod.GET,
              entity,
              UsuarioDTO.class
      );

      return response.getBody();

    } catch (HttpClientErrorException e) {
      String errorMessage = e.getResponseBodyAsString();
      throw new UsuarioNoEncontradoException("El usuario no existe: " + errorMessage);
    } catch (Exception e) {
      throw new UsuarioNoEncontradoException("Error al comunicarse con el servicio de usuarios");
    }
  }
    
    public UsuarioDTO obtenerUsuarioPorClave(String claveUsuario) {
        String token = obtenerTokenDelContexto();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
            "http://sicae-usuarios-app:8082/api/users/clave/" + claveUsuario,
            HttpMethod.GET,
            entity,
            UsuarioDTO.class
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