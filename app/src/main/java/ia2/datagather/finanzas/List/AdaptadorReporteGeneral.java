package ia2.datagather.finanzas.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.TarjetaReporte;
import ia2.datagather.finanzas.R;

public class AdaptadorReporteGeneral extends RecyclerView.Adapter<RepTarjetaViewModel> {

    private ArrayList<TarjetaReporte> tarjetas;
    private PresionarRepTarjetaListener listener;

    public AdaptadorReporteGeneral() { tarjetas = new ArrayList<>();}

    public void limpiarLista() {
        tarjetas.clear();
        notifyDataSetChanged();
    }

    public void agregarTarjeta(TarjetaReporte tarjeta) {
        tarjetas.add(tarjeta);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RepTarjetaViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fila_rep_tarjeta,parent,false);
        RepTarjetaViewModel repTarjetaViewModel = new RepTarjetaViewModel(view);
        return repTarjetaViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull RepTarjetaViewModel holder, int position) {
        String valor = "Monto disponible : $" + tarjetas.get(position).getMonto();
        holder.getTipo().setText(tarjetas.get(position).getTipo());
        holder.getTitulo().setText(tarjetas.get(position).getTitulo());
        holder.getMonto().setText(valor);
        holder.getBoton().setOnClickListener(v-> listener.repTarjetaPresionada(tarjetas.get(position)));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setListener(PresionarRepTarjetaListener listener) {
        this.listener = listener;
    }

    public interface PresionarRepTarjetaListener {
        void repTarjetaPresionada(TarjetaReporte tarjeta);
    }
}
