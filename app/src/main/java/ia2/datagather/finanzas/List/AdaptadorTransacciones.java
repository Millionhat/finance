package ia2.datagather.finanzas.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ia2.datagather.finanzas.Model.Transaccion;
import ia2.datagather.finanzas.R;

public class AdaptadorTransacciones extends RecyclerView.Adapter<TransaccionViewModel> {

    private ArrayList<Transaccion> transacciones;

    public AdaptadorTransacciones() {transacciones = new ArrayList<>();}
    @NonNull
    @Override
    public TransaccionViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fila_transaccion,parent,false);
        TransaccionViewModel transaccionViewModel = new TransaccionViewModel(view);
        return transaccionViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewModel holder, int position) {
        String valor, titulo;
        Transaccion transaccion = transacciones.get(position);
        if(transaccion.getTipo().equals("Gasto")){
            titulo= transaccion.getDescripcion();
            valor= "-$ "+transaccion.getMonto();
        }else{
            titulo= transaccion.getTipo();
            valor= "$ "+transaccion.getMonto();
        }
        holder.getDescripcion().setText(titulo);
        holder.getFecha().setText(transaccion.getFecha().toDate().toString());
        holder.getMonto().setText(valor);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setTransacciones(ArrayList t){
        transacciones=t;
        notifyDataSetChanged();
    }
}
