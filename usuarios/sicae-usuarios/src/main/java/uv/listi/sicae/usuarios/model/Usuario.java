package uv.listi.sicae.usuarios.model;

import java.time.LocalDateTime;

public class Usuario {
    private Integer idUsuario;
    private Integer idRol;
    private Integer idTipoUsuario;
    private Integer idProgramaEducativo;
    private String nombre;
    private String apellidoPaterno;
    private String username;
    private String password;
    private String correo;
    private String telefono;
    private String claveUsuario;
    private boolean estatus;
    private String estatusWord;
    private LocalDateTime tiempoCreacion;
    private LocalDateTime tiempoActualizacion;

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }
    public Integer getIdTipoUsuario() { return idTipoUsuario; }
    public void setIdTipoUsuario(Integer idTipoUsuario) { this.idTipoUsuario = idTipoUsuario; }
    public Integer getIdProgramaEducativo() { return idProgramaEducativo; }
    public void setIdProgramaEducativo(Integer idProgramaEducativo) { this.idProgramaEducativo = idProgramaEducativo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getClaveUsuario() { return claveUsuario; }
    public void setClaveUsuario(String claveUsuario) { this.claveUsuario = claveUsuario; }
    public boolean isEstatus() { return estatus; }
    public void setEstatus(boolean estatus) { this.estatus = estatus; }
    public String getEstatusWord() {
        if (this.estatusWord != null) {
            return this.estatusWord.toUpperCase();
        }
        return this.estatus ? "ACTIVO" : "INACTIVO";
    }
    public void setEstatusWord(String estatusWord) {
        this.estatusWord = estatusWord;
        if (estatusWord != null) {
            this.estatus = estatusWord.equalsIgnoreCase("ACTIVO");
        }
    }
    public LocalDateTime getTiempoCreacion() { return tiempoCreacion; }
    public void setTiempoCreacion(LocalDateTime tiempoCreacion) { this.tiempoCreacion = tiempoCreacion; }
    public LocalDateTime getTiempoActualizacion() { return tiempoActualizacion; }
    public void setTiempoActualizacion(LocalDateTime tiempoActualizacion) { this.tiempoActualizacion = tiempoActualizacion; }
}