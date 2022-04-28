package ia2.datagather.finanzas.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ia2.datagather.finanzas.Model.Gasto;
import ia2.datagather.finanzas.R;

public class AdaptadorGasto extends RecyclerView.Adapter<GastoViewModel> {

    private ArrayList<Gasto> gastos;
    private PresionarGastoListener listener;

    public AdaptadorGasto() { gastos = new ArrayList<>(); }

    public void agregarGasto(Gasto gasto){
        gastos.add(gasto);
        notifyDataSetChanged();
    }

    public void limpiarLista() {
        gastos.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GastoViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fila_gasto,parent, false);
        GastoViewModel gastoViewModel = new GastoViewModel(view);
        return gastoViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewModel holder, int position) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String valor = "Monto del gasto: $" + gastos.get(position).getMonto();
        holder.getCategoria().setText(gastos.get(position).getCategoria());
        holder.getFecha().setText(gastos.get(position).getFecha().toDate().toString());
        holder.getMonto().setText(valor);
        holder.getBoton().setOnClickListener(v-> listener.gastoPresionado(gastos.get(position)));
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public void setListener(PresionarGastoListener listener) {this.listener = listener;}

    public interface PresionarGastoListener {
        void gastoPresionado(Gasto gasto);
    }
}
