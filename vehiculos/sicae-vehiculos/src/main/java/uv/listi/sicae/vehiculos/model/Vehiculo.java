package uv.listi.sicae.vehiculos.model;

public class Vehiculo {
    private Integer idVehiculo;
    private Integer idUsuario;
    private String claveVehiculo;
    private Integer idModelo;
    private String placa;
    private String color;
    private Integer anio;
    private String descripcion;
    private String estatus;

    public Integer getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Integer idVehiculo) { this.idVehiculo = idVehiculo; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getClaveVehiculo() { return claveVehiculo; }
    public void setClaveVehiculo(String claveVehiculo) { this.claveVehiculo = claveVehiculo; }
    public Integer getIdModelo() { return idModelo; }
    public void setIdModelo(Integer idModelo) { this.idModelo = idModelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstatus() { return estatus; }

    public void setEstatus(Object estatus) {
        if (estatus == null) {
            this.estatus = null;
            return;
        }
        if (estatus instanceof Boolean) {
            this.estatus = (Boolean) estatus ? "activo" : "inactivo";
        } else if (estatus instanceof Number) {
            this.estatus = ((Number) estatus).intValue() == 1 ? "activo" : "inactivo";
        } else if (estatus instanceof String) {
            String estatusStr = (String) estatus;
            if (estatusStr.equalsIgnoreCase("1") || estatusStr.equalsIgnoreCase("true")) {
                this.estatus = "activo";
            } else if (estatusStr.equalsIgnoreCase("0") || estatusStr.equalsIgnoreCase("false")) {
                this.estatus = "inactivo";
            } else if (estatusStr.equalsIgnoreCase("activo") || estatusStr.equalsIgnoreCase("inactivo")) {
                this.estatus = estatusStr.toLowerCase();
            } else {
                throw new IllegalArgumentException("El estatus debe ser 'activo' o 'inactivo'");
            }
        } else {
             throw new IllegalArgumentException("Tipo de dato no soportado para estatus: " + estatus.getClass().getName());
        }
    }
}