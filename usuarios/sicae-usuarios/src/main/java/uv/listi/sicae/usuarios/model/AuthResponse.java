package uv.listi.sicae.usuarios.model;

public class AuthResponse {
    private Integer idUsuario;
    private Integer idRol;
    private String rol;
    private String usuario;
    private String nombreCompleto;
    private Integer idTipoUsuario;
    private String tipoUsuario;
    private String token;

    public AuthResponse(Integer idUsuario, Integer idRol, String rol, String usuario, 
                        String nombreCompleto, Integer idTipoUsuario, String tipoUsuario, String token) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.rol = rol;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.idTipoUsuario = idTipoUsuario;
        this.tipoUsuario = tipoUsuario;
        this.token = token;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public Integer getIdRol() { return idRol; }
    public String getRol() { return rol; }
    public String getUsuario() { return usuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public Integer getIdTipoUsuario() { return idTipoUsuario; }
    public String getTipoUsuario() { return tipoUsuario; }
    public String getToken() { return token; }
}