/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.estacionamiento.dto;

/**
 *
 * @author maria
 */
public class VehiculoDTO {
    private Integer idVehiculo;
    private String placa;
    private boolean estatus;

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }
    
    
}
