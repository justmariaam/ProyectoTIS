/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.estacionamiento.dto;

/**
 *
 * @author marian
 */
public class UsuarioDTO {
    private Integer idUsuario;
    private String claveUsuario;
    private Boolean estatus;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
    
    
    
}
