package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ia2.datagather.finanzas.List.AdaptadorTransacciones;
import ia2.datagather.finanzas.Model.ConteoCategorias;
import ia2.datagather.finanzas.Model.Gasto;
import ia2.datagather.finanzas.Model.Ingreso;
import ia2.datagather.finanzas.Model.TarjetaReporte;
import ia2.datagather.finanzas.Model.Transaccion;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class ReporteEspecifico extends AppCompatActivity implements View.OnClickListener{
    private Usuario usuario;
    private TarjetaReporte tarjeta;
    private TextView monto,tipo,titulo, categoriaMasComun;
    private RecyclerView listaTransacciones;
    private FirebaseFirestore db;
    private AdaptadorTransacciones adaptador;
    private ArrayList<ConteoCategorias> categorias;

    public ReporteEspecifico() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_especifico);
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        tarjeta = (TarjetaReporte) getIntent().getExtras().get("tarjeta");
        monto = findViewById(R.id.RepEspMontoTV);
        tipo = findViewById(R.id.RepEspTipoTV);
        titulo = findViewById(R.id.RespEspTituloTV);
        categorias = new ArrayList<>();
        adaptador = new AdaptadorTransacciones();
        listaTransacciones.setAdapter(adaptador);
        listaTransacciones.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaTransacciones.setLayoutManager(manager);
        cargarTransacciones();
        organizarCategorias();
    }

    private void organizarCategorias() {
        AtomicInteger maxValor = new AtomicInteger();
        AtomicReference<String> cat = new AtomicReference<>("");
        categorias.forEach( c -> {
            if (maxValor.get() < c.contador){
                maxValor.set(c.contador);
                cat.set(c.categoria);
            }
        });
        if(cat.get().equals("")){
            categoriaMasComun.setText("Ningunca Categoria presente");
        }else{
            categoriaMasComun.setText("La categoria con mas gastos es: "+cat);
        }
    }

    private void cargarTransacciones() {
        ArrayList<Transaccion> transaccions= new ArrayList<>();
        if(tarjeta.getTipo().equals("Credito")){
            transaccions = cargarGastos();
        }else{
            transaccions = cargarGastos();
            cargarIngresos(transaccions);
        }
        ArrayList<Transaccion> ordenados= new ArrayList<>();
        ordenados = ordenarTransacciones(transaccions);
        adaptador.setTransacciones(ordenados);

    }

    private ArrayList<Transaccion> ordenarTransacciones(ArrayList<Transaccion> transaccions) {
        Comparator<Transaccion> comparador = (t1, t2) -> t1.getFecha().compareTo(t2.getFecha());
        transaccions.sort(comparador);
        return transaccions;
    }

    private void cargarIngresos(ArrayList<Transaccion> transaccions) {

        Query refIngresos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                .document(tarjeta.getId()).collection("ingresos");
        AtomicReference<Double> suma= new AtomicReference<>(0.0);
        refIngresos.get().addOnCompleteListener(
                g->{
                    for(QueryDocumentSnapshot doc: g.getResult()){
                        Ingreso ingreso = doc.toObject(Ingreso.class);
                        transaccions.add(new Transaccion("Ingreso","","Ingreso",
                                ingreso.getIngreso(),ingreso.getFecha()));
                    }
                }
        );
    }

    private ArrayList<Transaccion> cargarGastos() {
        ArrayList<Transaccion> resultados = new ArrayList<>();
        Query refGastos;
        if(tarjeta.getTipo().equals("Credito")){
            LocalDate fecha = LocalDate.now().minusDays(30);
            Date fecha30diasAntes = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Timestamp comparacion = new Timestamp(new java.sql.Timestamp(fecha30diasAntes.getTime()));
            refGastos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                    .document(tarjeta.getId()).collection("gastos").whereGreaterThan("fecha", comparacion);
        }else{
            refGastos = db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                    .document(tarjeta.getId()).collection("gastos");
        }
        refGastos.get().addOnCompleteListener(
                g->{
                    for(QueryDocumentSnapshot doc: g.getResult()) {
                        Gasto gasto = doc.toObject(Gasto.class);
                        resultados.add(new Transaccion("Gasto", gasto.getDescripcion(),
                                gasto.getCategoria(), gasto.getMonto(), gasto.getFecha()));
                        agregarCategoria(gasto.getCategoria());
                    }
                }
        );
        return resultados;
    }

    private void agregarCategoria(String descripcion) {
        categorias.forEach(c -> {
            if(c.categoria.equals(descripcion)){
                c.contador +=1;
            }else{
                categorias.add(new ConteoCategorias(descripcion,1));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}