package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import ia2.datagather.finanzas.List.AdaptadorReporteGeneral;
import ia2.datagather.finanzas.Model.Gasto;
import ia2.datagather.finanzas.Model.Ingreso;
import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.TarjetaReporte;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class ReporteGeneralActivity extends AppCompatActivity implements View.OnClickListener, AdaptadorReporteGeneral.PresionarRepTarjetaListener {

    private Usuario usuario;
    private TextView valorTotal, valorDisponible;
    private RecyclerView listaTarjetas;
    private FirebaseFirestore db;
    private AdaptadorReporteGeneral adaptador;
    private Double montoTotal, montoDisp;
    private ArrayList<TarjetaReporte> listado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_general);
        listado = new ArrayList<>();
        montoDisp= 0.0;
        montoTotal = 0.0;
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        db = FirebaseFirestore.getInstance();
        valorTotal = findViewById(R.id.repgeneralTotalValorTV);
        valorDisponible = findViewById(R.id.repGenMontoDispTV);
        listaTarjetas = findViewById(R.id.repListaTarjetas);
        adaptador = new AdaptadorReporteGeneral();
        adaptador.setListener(this);
        listaTarjetas.setAdapter(adaptador);
        listaTarjetas.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaTarjetas.setLayoutManager(manager);
        valorTotal.setText(" Cargando");
        valorDisponible.setText(" Cargando");
        cargarReporte();
    }

    private void cargarReporte() {
        Query refTarjetas =db.collection("usuarios").document(usuario.getId()).collection("tarjetas").orderBy("nombre");
        refTarjetas.get().addOnCompleteListener(
                t->{
                    for(QueryDocumentSnapshot doc: t.getResult()){
                        Tarjeta tarjeta =doc.toObject(Tarjeta.class);
                        Double tarjetaMonto = 0.0;
                        AtomicReference<Double> gastos = new AtomicReference<>(0.0);
                        AtomicReference<Double> ingresos = new AtomicReference<>(0.0);
                        ingresos.updateAndGet(v -> v + tarjeta.getCupo());
                        if(tarjeta.getTipo().equals("Credito")){
                            LocalDate fecha = LocalDate.now().minusDays(30);
                            Date fecha30diasAntes = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            AtomicReference<Double> totalGastos = new AtomicReference<>(0.0);
                            tarjeta.getGastos().forEach(g->{
                                int resultado = g.getFecha().compareTo(fecha30diasAntes);
                                if(resultado>0){
                                    gastos.updateAndGet(v -> v + g.getMonto());
                                }
                            });
                        }else {
                            tarjeta.getGastos().forEach(g->{
                                gastos.updateAndGet(v -> v + g.getMonto());
                            });
                            tarjeta.getIngresos().forEach( i -> {
                                ingresos.updateAndGet(v -> v + i.getIngreso());
                            });
                        }
                        montoTotal += ingresos.get();
                        tarjetaMonto = ingresos.get() - gastos.get();
                        montoDisp += tarjetaMonto;
                        TarjetaReporte converted = new TarjetaReporte();
                        converted.setId(tarjeta.getId());
                        converted.setTitulo(tarjeta.getNombre());
                        converted.setTipo(tarjeta.getTipo());
                        converted.setMonto(tarjetaMonto);
                        listado.add(converted);
                    }
                    listado.forEach(f->{
                        adaptador.agregarTarjeta(f);
                        Toast.makeText(this,"Entro",Toast.LENGTH_LONG);
                    });
                    runOnUiThread(
                            () -> {
                                valorTotal.setText(montoTotal.toString());
                                valorDisponible.setText(montoDisp.toString());
                            }
                    );
                }
        );
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void repTarjetaPresionada(TarjetaReporte tarjeta) {
        Intent i = new Intent(this, ReporteEspecifico.class);
        i.putExtra("usuario",usuario);
        i.putExtra("tarjeta", tarjeta);
        startActivity(i);
    }
}