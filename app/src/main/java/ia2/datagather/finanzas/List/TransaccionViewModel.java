package ia2.datagather.finanzas.List;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ia2.datagather.finanzas.R;

public class TransaccionViewModel extends RecyclerView.ViewHolder {

    private TextView descripcion, fecha,monto,categoria;

    public TransaccionViewModel(@NonNull View itemView) {
        super(itemView);
        descripcion = itemView.findViewById(R.id.filaTdescripcion);
        fecha = itemView.findViewById(R.id.filaTFecha);
        monto = itemView.findViewById(R.id.filaTMonto);
        categoria = itemView.findViewById(R.id.textView15);
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public TextView getFecha() {
        return fecha;
    }

    public TextView getMonto() {
        return monto;
    }

    public TextView getCategoria(){ return categoria;}
}
