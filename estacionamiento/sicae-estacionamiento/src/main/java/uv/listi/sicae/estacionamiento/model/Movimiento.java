package uv.listi.sicae.estacionamiento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {
    private Integer idMovimiento;
    private String claveUsuario;
    private String placa;
    private LocalDateTime tiempoEntrada;
    private LocalDateTime tiempoSalida;
    private Integer minutosEstacionado;
    private BigDecimal horasCobradas;
    private BigDecimal costoTotal;
    private BigDecimal tarifaHora;
    private Integer idEspacio;
    private LocalDateTime tiempoCreacion;
    private LocalDateTime tiempoActualizacion;

    public Integer getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Integer idMovimiento) { this.idMovimiento = idMovimiento; }
    public String getClaveUsuario() { return claveUsuario; }
    public void setClaveUsuario(String claveUsuario) { this.claveUsuario = claveUsuario; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public LocalDateTime getTiempoEntrada() { return tiempoEntrada; }
    public void setTiempoEntrada(LocalDateTime tiempoEntrada) { this.tiempoEntrada = tiempoEntrada; }
    public LocalDateTime getTiempoSalida() { return tiempoSalida; }
    public void setTiempoSalida(LocalDateTime tiempoSalida) { this.tiempoSalida = tiempoSalida; }
    public Integer getMinutosEstacionado() { return minutosEstacionado; }
    public void setMinutosEstacionado(Integer minutosEstacionado) { this.minutosEstacionado = minutosEstacionado; }
    public BigDecimal getHorasCobradas() { return horasCobradas; }
    public void setHorasCobradas(BigDecimal horasCobradas) { this.horasCobradas = horasCobradas; }
    public BigDecimal getCostoTotal() { return costoTotal; }
    public void setCostoTotal(BigDecimal costoTotal) { this.costoTotal = costoTotal; }
    public BigDecimal getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(BigDecimal tarifaHora) { this.tarifaHora = tarifaHora; }
    public Integer getIdEspacio() { return idEspacio; }
    public void setIdEspacio(Integer idEspacio) { this.idEspacio = idEspacio; }
    public LocalDateTime getTiempoCreacion() { return tiempoCreacion; }
    public void setTiempoCreacion(LocalDateTime tiempoCreacion) { this.tiempoCreacion = tiempoCreacion; }
    public LocalDateTime getTiempoActualizacion() { return tiempoActualizacion; }
    public void setTiempoActualizacion(LocalDateTime tiempoActualizacion) { this.tiempoActualizacion = tiempoActualizacion; }
}