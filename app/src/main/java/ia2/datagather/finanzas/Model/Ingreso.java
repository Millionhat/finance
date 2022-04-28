package ia2.datagather.finanzas.Model;

import com.google.firebase.Timestamp;

public class Ingreso {

    private String id;
    private Double ingreso;
    private Timestamp fecha;

    public Ingreso() {
    }

    public Ingreso(String id, Double ingreso, Timestamp fecha) {
        this.id = id;
        this.ingreso = ingreso;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getIngreso() {
        return ingreso;
    }

    public void setIngreso(Double ingreso) {
        this.ingreso = ingreso;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
