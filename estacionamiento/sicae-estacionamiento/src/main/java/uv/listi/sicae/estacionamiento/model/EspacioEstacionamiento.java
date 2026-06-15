package uv.listi.sicae.estacionamiento.model;

public class EspacioEstacionamiento {
    private Integer idEspacio;
    private String claveEspacio;
    private String tipo;
    private boolean ocupado;
    private boolean estatus;

    public Integer getIdEspacio() { return idEspacio; }
    public void setIdEspacio(Integer idEspacio) { this.idEspacio = idEspacio; }
    public String getClaveEspacio() { return claveEspacio; }
    public void setClaveEspacio(String claveEspacio) { this.claveEspacio = claveEspacio; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }
    public boolean isEstatus() { return estatus; }
    public void setEstatus(boolean estatus) { this.estatus = estatus; }
}