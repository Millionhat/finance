package ia2.datagather.finanzas.Model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Gasto implements Serializable {

    private String id;
    private String categoria,descripcion;
    private Double monto;
    private Timestamp fecha;

    public Gasto() {
    }

    public Gasto(String id, String categoria, String descripcion, Double monto, Timestamp fecha) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Timestamp  getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
