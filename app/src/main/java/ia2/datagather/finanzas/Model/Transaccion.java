package ia2.datagather.finanzas.Model;

import com.google.firebase.Timestamp;

public class Transaccion {
    private String tipo, descripcion,categoria;
    private Double monto;
    private Timestamp fecha;

    public Transaccion() {
    }

    public Transaccion(String tipo, String descripcion, String categoria,Double monto, Timestamp fecha) {
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

    public Timestamp getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getMonto() {
        return monto;
    }
}
