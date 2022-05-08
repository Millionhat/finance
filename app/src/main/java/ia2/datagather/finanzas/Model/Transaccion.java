package ia2.datagather.finanzas.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Transaccion {
    private String tipo, descripcion,categoria;
    private Double monto;
    private Date fecha;

    public Transaccion() {
    }

    public Transaccion(String tipo, String descripcion, String categoria,Double monto, Date fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.categoria =categoria;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getMonto() {
        return monto;
    }
}
