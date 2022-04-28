package ia2.datagather.finanzas.Model;

import java.io.Serializable;

public class TarjetaReporte implements Serializable {
    private String id;
    private String titulo;
    private String tipo;
    private Double monto;

    public TarjetaReporte() {
    }

    public TarjetaReporte(String id, String titulo, String tipo, Double monto) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
