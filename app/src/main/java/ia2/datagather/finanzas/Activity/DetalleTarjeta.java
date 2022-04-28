package ia2.datagather.finanzas.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import ia2.datagather.finanzas.List.AdaptadorGasto;
import ia2.datagather.finanzas.Model.Gasto;
import ia2.datagather.finanzas.Model.Ingreso;
import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class DetalleTarjeta extends AppCompatActivity implements View.OnClickListener, AdaptadorGasto.PresionarGastoListener {

    private Double montoTotal;
    private TextView tituloTarjeta, tipoTarjeta, montoDisponible, fragMontoIngreso;
    private Usuario usuario;
    private Tarjeta tarjeta;
    private RecyclerView listaGasto;
    private FirebaseFirestore db;
    private Button crearGastoBtn,agregarIngresoBtn,fragAgregarIngreso;
    private AdaptadorGasto adaptador;
    private AlertDialog.Builder dbuilder;
    private Dialog addDialog;
    private Boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarjeta);

        loaded = false;
        tarjeta = (Tarjeta) getIntent().getExtras().get("tarjeta");
        montoTotal = tarjeta.getCupo();
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        db = FirebaseFirestore.getInstance();
        tituloTarjeta = findViewById(R.id.tarjetaDetalleTituloTV);
        tipoTarjeta = findViewById(R.id.TipoTarjetaTextView);
        montoDisponible = findViewById(R.id.montoTarjetaTextView);
        listaGasto = findViewById(R.id.listaGastoRecycler);
        adaptador = new AdaptadorGasto();
        adaptador.setListener(this);
        listaGasto.setAdapter(adaptador);
        listaGasto.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaGasto.setLayoutManager(manager);
        crearGastoBtn = findViewById(R.id.agregarGastoBtn);
        crearGastoBtn.setOnClickListener(this);
        agregarIngresoBtn = findViewById(R.id.AgregarIngresoBtn);
        agregarIngresoBtn.setOnClickListener(this);
        tituloTarjeta.setText(tarjeta.getNombre() + ", " + tarjeta.getDescripcion());
        tipoTarjeta.setText("Tipo de tarjeta: " + tarjeta.getTipo());
        montoDisponible.setText(" Monto Disponible: Cargando");
        cargarGastos();
    }

    private void cargarGastos() {
        if(tarjeta.getTipo().equals("Credito") && !loaded){
            agregarIngresoBtn.setEnabled(false);
            agregarIngresoBtn.setAlpha(0);
            LocalDate fecha = LocalDate.now().minusDays(30);
            Date fecha30diasAntes = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
            AtomicReference<Double> totalGastos = new AtomicReference<>(0.0);
            tarjeta.getGastos().forEach(g->{
                int resultado = g.getFecha().compareTo(fecha30diasAntes);
                if(resultado>0){
                    totalGastos.updateAndGet(v -> v + g.getMonto());
                    adaptador.agregarGasto(g);
                }
            });
            runOnUiThread(
                    () ->{
                        montoDisponible.setText("Monto Disponible: "+ (montoTotal-totalGastos.get()));
                    }
            );
            loaded = true;
            /*
            Query refGastos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas").document(tarjeta.getId()).collection("gastos").whereGreaterThan("fecha", comparacion);
            refGastos.get().addOnCompleteListener(
                    g->{
                        adaptador.limpiarLista();
                        Double totalGastos = 0.0;
                        for(QueryDocumentSnapshot doc: g.getResult()){
                            Gasto gasto = doc.toObject(Gasto.class);
                            totalGastos += gasto.getMonto();
                            adaptador.agregarGasto(gasto);
                        }
                        montoDisponible.setText("Monto Disponible: " + (montoTotal-totalGastos));
                    }
            );*/
        }else {
            AtomicReference<Double> totalGastos = new AtomicReference<>(0.0);
            tarjeta.getGastos().forEach(g->{
                totalGastos.updateAndGet(v -> v + g.getMonto());
                adaptador.agregarGasto(g);
            });
            montoTotal = montoTotal - totalGastos.get();
            runOnUiThread(
                    () ->{
                        montoDisponible.setText("Monto Disponible: " + (montoTotal));
                    }
            );

            loaded = true;
            if(montoTotal <= 0){
                runOnUiThread(
                        ()->{
                            crearGastoBtn.setEnabled(false);
                        }
                );
            }else {
                runOnUiThread(
                        ()->{
                            crearGastoBtn.setEnabled(true);
                        }
                );
            }
            loaded=true;
            /*
            Query refIngresos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas").document(tarjeta.getId()).collection("ingresos");
            refIngresos.get().addOnCompleteListener(
                    g->{
                        Double totalIngreso = 0.0;
                        for(QueryDocumentSnapshot doc: g.getResult()){
                            Ingreso ingreso = doc.toObject(Ingreso.class);
                            totalIngreso += ingreso.getIngreso();
                        }
                        montoTotal = tarjeta.getCupo()+totalIngreso;
                    }
            );
            Query refGastos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas").document(tarjeta.getId()).collection("gastos").orderBy("fecha",Query.Direction.DESCENDING);
            refGastos.get().addOnCompleteListener(
                    g->{
                        adaptador.limpiarLista();
                        Double totalGastos = 0.0;
                        for(QueryDocumentSnapshot doc: g.getResult()){
                            Gasto gasto = doc.toObject(Gasto.class);
                            totalGastos += gasto.getMonto();
                            adaptador.agregarGasto(gasto);
                        }
                        if(!loaded){
                            montoTotal = montoTotal - totalGastos;
                            montoDisponible.setText("Monto Disponible: " + (montoTotal));
                            loaded = true;
                            if(montoTotal <= 0){
                                runOnUiThread(
                                        ()->{
                                            crearGastoBtn.setEnabled(false);
                                        }
                                );
                            }else {
                                runOnUiThread(
                                        ()->{
                                            crearGastoBtn.setEnabled(true);
                                        }
                                );
                            }
                        }
                    }
            );*/
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.agregarGastoBtn:
                Intent i = new Intent(this, AgregarGasto.class);
                i.putExtra("usuario", usuario);
                i.putExtra("tarjeta", tarjeta);
                loaded=false;
                startActivityForResult(i,1);
                break;

            case R.id.AgregarIngresoBtn:
                crearDialogo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            if(resultCode== RESULT_OK){
                cargarGastos();
            }
        }
    }

    private void crearDialogo() {
        dbuilder = new AlertDialog.Builder(this);
        final View frag_agregar = getLayoutInflater().inflate(R.layout.fragment_agregar_ingreso,null);
        fragMontoIngreso = (TextView) frag_agregar.findViewById(R.id.montoIngresoTV);
        fragAgregarIngreso = (Button) frag_agregar.findViewById(R.id.crearIngresoBtn);
        dbuilder.setView(frag_agregar);
        addDialog = dbuilder.create();
        addDialog.show();

        fragAgregarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarIngreso();
                addDialog.dismiss();
            }
        });
    }

    private void agregarIngreso() {
        Date date = new Date();
        Ingreso nuevoIngreso = new Ingreso(UUID.randomUUID().toString(),
                Double.parseDouble(fragMontoIngreso.getText().toString()),
                date);
        tarjeta.getIngresos().add(nuevoIngreso);
        db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                .document(tarjeta.getId()).set(tarjeta);
        cargarGastos();
    }


    @Override
    public void gastoPresionado(Gasto gasto) {

    }
}