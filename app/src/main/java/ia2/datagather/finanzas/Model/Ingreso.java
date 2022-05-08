package ia2.datagather.finanzas.Model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class Ingreso implements Serializable {

    private String id;
    private Double ingreso;
    private Date fecha;

    public Ingreso() {
    }

    public Ingreso(String id, Double ingreso, Date fecha) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
