/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.listi.sicae.vehiculos.dto;

public class UsuarioDTO {

  private Integer idUsuario;
  private String claveUsuario;
  private Boolean estatus;
  private String nombre;
  private String apellidoPaterno;
  private String correo;
  private String telefono;

  public Integer getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Integer idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getClaveUsuario() {
    return claveUsuario;
  }

  public void setClaveUsuario(String claveUsuario) {
    this.claveUsuario = claveUsuario;
  }

  public Boolean getEstatus() {
    return estatus;
  }

  public void setEstatus(Boolean estatus) {
    this.estatus = estatus;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellidoPaterno() {
    return apellidoPaterno;
  }

  public void setApellidoPaterno(String apellidoPaterno) {
    this.apellidoPaterno = apellidoPaterno;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }
}
