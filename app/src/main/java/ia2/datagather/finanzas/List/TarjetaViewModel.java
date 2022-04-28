package ia2.datagather.finanzas.List;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ia2.datagather.finanzas.R;


public class TarjetaViewModel extends RecyclerView.ViewHolder {

    private Button boton;
    private TextView nombreTarjeta,montoTexto, montoValor;

    public TarjetaViewModel(@NonNull View itemView) {
        super(itemView);

        boton = itemView.findViewById(R.id.botonFilaTarjeta);
        nombreTarjeta = itemView.findViewById(R.id.NombreTarjetaTV);
        montoTexto = itemView.findViewById(R.id.montoTV);
        montoValor = itemView.findViewById(R.id.montoValorTV);
    }

    public Button getBoton() {
        return boton;
    }

    public TextView getNombreTarjeta() {
        return nombreTarjeta;
    }

    public TextView getMontoTexto() {
        return montoTexto;
    }

    public TextView getMontoValor() {
        return montoValor;
    }
}
