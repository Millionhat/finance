package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        valorTotal = findViewById(R.id.repgeneralTotalValorTV);
        valorDisponible = findViewById(R.id.repGenMontoDispTV);
        listaTarjetas = findViewById(R.id.repListaTarjetas);
        adaptador = new AdaptadorReporteGeneral();
        adaptador.setListener(this);
        listaTarjetas.setAdapter(adaptador);
        listaTarjetas.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaTarjetas.setLayoutManager(manager);
        valorTotal.setText("Monto Total: Cargando");
        valorDisponible.setText("Monto Disponible: Cargando");
        db = FirebaseFirestore.getInstance();
        configurarInfo();
    }

    private void configurarInfo() {
        cargarReporte();
        cargarIngresos();
        montoDisp = montoTotal;
        cargarGastos();
        cargarListado();
    }

    private void cargarListado() {
        valorTotal.setText("$"+montoTotal);
        valorDisponible.setText("$"+montoDisp);
        adaptador.limpiarLista();
        listado.forEach(t->{
            adaptador.agregarTarjeta(t);
        });
    }

    private void cargarReporte() {
        Query refTarjetas =db.collection("usuarios").document(usuario.getId()).collection("tarjetas").orderBy("nombre");
        refTarjetas.get().addOnCompleteListener(
                t->{
                    adaptador.limpiarLista();
                    for(QueryDocumentSnapshot doc: t.getResult()){
                        Tarjeta tarjeta =doc.toObject(Tarjeta.class);
                        TarjetaReporte converted = new TarjetaReporte();
                        converted.setId(tarjeta.getId());
                        converted.setTitulo(tarjeta.getNombre());
                        converted.setTipo(tarjeta.getTipo());
                        converted.setMonto(tarjeta.getCupo());
                        listado.add(converted);
                    }
                }
        );
    }

    private void cargarGastos() {
        listado.forEach( t -> {
            Query refGastos;
            if (t.getTipo().equals("Credito")){
                LocalDate fecha = LocalDate.now().minusDays(30);
                Date fecha30diasAntes = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Timestamp comparacion = new Timestamp(new java.sql.Timestamp(fecha30diasAntes.getTime()));
                refGastos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                        .document(t.getId()).collection("gastos").whereGreaterThan("fecha", comparacion);
            }else {
                refGastos = db.collection("usuarios").document(usuario.getId())
                        .collection("tarjetas").document(t.getId())
                        .collection("gastos").orderBy("fecha",Query.Direction.DESCENDING);
            }
            refGastos.get().addOnCompleteListener(
                    g->{
                        Double totalGastos = 0.0;
                        for(QueryDocumentSnapshot doc: g.getResult()){
                            Gasto gasto = doc.toObject(Gasto.class);
                            totalGastos += gasto.getMonto();
                        }
                        t.setMonto(t.getMonto() - totalGastos);
                        montoDisp -= totalGastos;
                    }
            );
        });
    }

    private void cargarIngresos() {
        listado.forEach(t -> {
            if(t.getTipo().equals("Debito")){
                Query refIngresos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                        .document(t.getId()).collection("ingresos");
                refIngresos.get().addOnCompleteListener(
                        g->{
                            Double totalIngreso = 0.0;
                            for(QueryDocumentSnapshot doc: g.getResult()){
                                Ingreso ingreso = doc.toObject(Ingreso.class);
                                totalIngreso += ingreso.getIngreso();
                            }
                            t.setMonto(t.getMonto()+totalIngreso);
                            montoTotal += t.getMonto();
                        }
                );
            }
        });

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