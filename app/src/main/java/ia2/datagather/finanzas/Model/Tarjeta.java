package ia2.datagather.finanzas.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Tarjeta implements Serializable {
    private String id,nombre,tipo,descripcion;
    private double cupo;
    private ArrayList<Gasto> gastos;
    private ArrayList<Ingreso> ingresos;

    public Tarjeta() {
        ingresos = new ArrayList<>();
        gastos = new ArrayList<>();
    }

    public Tarjeta(String id, String nombre, String tipo, String descripcion, double cupo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cupo = cupo;
        ingresos = new ArrayList<>();
        gastos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(ArrayList<Gasto> gastos) {
        this.gastos = gastos;
    }

    public ArrayList<Ingreso> getIngresos() {
        return ingresos;
    }

    public void setIngresos(ArrayList<Ingreso> ingresos) {
        this.ingresos = ingresos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCupo() {
        return cupo;
    }

    public void setCupo(double cupo) {
        this.cupo = cupo;
    }
}
