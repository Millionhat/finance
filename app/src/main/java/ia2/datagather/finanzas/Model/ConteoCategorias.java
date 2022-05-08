package ia2.datagather.finanzas.Model;

public class ConteoCategorias {
    public String categoria;
    public int contador;

    public ConteoCategorias() {
    }

    public ConteoCategorias(String categoria, int contador) {
        this.categoria = categoria;
        this.contador = contador;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
}
