package ia2.datagather.finanzas.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.R;

public class AdaptadorTarjeta extends RecyclerView.Adapter<TarjetaViewModel> {

    private ArrayList<Tarjeta> tarjetas;
    private PresionarTarjetaListener listener;

    public AdaptadorTarjeta(){
        tarjetas = new ArrayList();
    }

    public void agregarTarjeta(Tarjeta tarjeta){
        tarjetas.add(tarjeta);
        notifyDataSetChanged();
    }

    public void limpiarLista() {
        tarjetas.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarjetaViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fila_tarjeta, parent, false);
        TarjetaViewModel tarjetaViewModel = new TarjetaViewModel(view);
        return tarjetaViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull TarjetaViewModel holder, int position) {
        String valor = "$"+tarjetas.get(position).getCupo();
        holder.getNombreTarjeta().setText(tarjetas.get(position).getNombre());
        if(tarjetas.get(position).getTipo().equals("Credito")){
            holder.getMontoValor().setText(valor);
        }else {
            holder.getMontoTexto().setText("Tarjeta debito");
            holder.getMontoValor().setText("");
        }

        holder.getBoton().setOnClickListener(v-> listener.tarjetaPresionada(tarjetas.get(position)));
    }

    @Override
    public int getItemCount() {
        return tarjetas.size();
    }

    public void setListener(PresionarTarjetaListener listener) {
        this.listener = listener;
    }

    public interface PresionarTarjetaListener {
        void tarjetaPresionada(Tarjeta tajeta);
    }
}
