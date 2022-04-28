package ia2.datagather.finanzas.List;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ia2.datagather.finanzas.R;

public class RepTarjetaViewModel extends RecyclerView.ViewHolder {

    private Button boton;
    private TextView titulo,tipo,monto;

    public RepTarjetaViewModel(@NonNull View itemView) {
        super(itemView);

        boton = itemView.findViewById(R.id.repTarjetaBtn);
        titulo = itemView.findViewById(R.id.tituloRepTarjeta);
        tipo = itemView.findViewById(R.id.tipoRepTarjeta);
        monto = itemView.findViewById(R.id.montoRepTarjeta);
    }

    public Button getBoton() {
        return boton;
    }

    public void setBoton(Button boton) {
        this.boton = boton;
    }

    public TextView getTitulo() {
        return titulo;
    }

    public void setTitulo(TextView titulo) {
        this.titulo = titulo;
    }

    public TextView getTipo() {
        return tipo;
    }

    public void setTipo(TextView tipo) {
        this.tipo = tipo;
    }

    public TextView getMonto() {
        return monto;
    }

    public void setMonto(TextView monto) {
        this.monto = monto;
    }

}
