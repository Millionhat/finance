package ia2.datagather.finanzas.List;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ia2.datagather.finanzas.R;

public class GastoViewModel extends RecyclerView.ViewHolder {

    private Button boton;
    private TextView categoria, monto, fecha;

    public GastoViewModel(@NonNull View itemView) {
        super(itemView);

        boton = itemView.findViewById(R.id.botonFilaGasto);
        categoria = itemView.findViewById(R.id.fila_categoria_TextView);
        monto = itemView.findViewById(R.id.fila_monto_valor_textview);
        fecha = itemView.findViewById(R.id.fila_fecha_textview);
    }

    public Button getBoton() {
        return boton;
    }

    public TextView getCategoria() {
        return categoria;
    }

    public TextView getMonto() {
        return monto;
    }

    public TextView getFecha() {
        return fecha;
    }
}
