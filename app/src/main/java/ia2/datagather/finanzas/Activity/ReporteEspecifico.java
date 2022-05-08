package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ia2.datagather.finanzas.List.AdaptadorTransacciones;
import ia2.datagather.finanzas.Model.ConteoCategorias;
import ia2.datagather.finanzas.Model.Tarjeta;
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
        db = FirebaseFirestore.getInstance();
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        tarjeta = (TarjetaReporte) getIntent().getExtras().get("tarjeta");
        monto = findViewById(R.id.RepEspMontoTV);
        tipo = findViewById(R.id.RepEspTipoTV);
        titulo = findViewById(R.id.RespEspTituloTV);
        categoriaMasComun= findViewById(R.id.RespEspCategoria);
        listaTransacciones = findViewById(R.id.ResEspTransacciones);
        categorias = new ArrayList<>();
        adaptador = new AdaptadorTransacciones();
        listaTransacciones.setAdapter(adaptador);
        listaTransacciones.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaTransacciones.setLayoutManager(manager);
        titulo.setText(tarjeta.getTitulo());
        tipo.setText("Tipo de Tarjeta: " + tarjeta.getTipo());
        monto.setText("Monto Disponible: $ "+ tarjeta.getMonto());
        cargarTransacciones();
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
        Query refTarjeta =  db.collection("usuarios").document(usuario.getId()).collection("tarjetas");
        refTarjeta.get().addOnCompleteListener(
                t-> {
                    for(QueryDocumentSnapshot doc: t.getResult()){
                        Tarjeta tarjeta1 = doc.toObject(Tarjeta.class);
                        if(tarjeta1.getTipo().equals("Credito")){
                            tarjeta1.getGastos().forEach(g->{
                                transaccions.add(new Transaccion(tarjeta1.getTipo(),
                                        g.getDescripcion(),g.getCategoria(),-g.getMonto(),g.getFecha()));
                                boolean agregado = false;
                                categorias.forEach(c -> {
                                    if(c.categoria.equals(g.getCategoria())){
                                        c.contador +=1;
                                    }
                                });
                                if (!agregado){
                                    categorias.add(new ConteoCategorias(g.getCategoria(),1));
                                }
                            });
                        }else{
                            tarjeta1.getGastos().forEach(g->{
                                transaccions.add(new Transaccion("Gasto",
                                        g.getDescripcion(),g.getCategoria(),-g.getMonto(),g.getFecha()));
                                AtomicBoolean agregado = new AtomicBoolean(false);
                                categorias.forEach(c -> {
                                    if(c.categoria.equals(g.getCategoria())){
                                        c.contador +=1;
                                        agregado.set(true);
                                    }
                                });
                                if (!agregado.get()){
                                    categorias.add(new ConteoCategorias(g.getCategoria(),1));
                                }
                            });
                            tarjeta1.getIngresos().forEach(i ->{
                                transaccions.add(new Transaccion("Ingreso",
                                        "Ingreso","Ingreso",i.getIngreso(),i.getFecha()));
                            });
                        }
                        categorias.sort(Comparator.comparing(ConteoCategorias::getContador).reversed());
                        if(categorias.size()>0){
                            categoriaMasComun.setText("La categoria con mas gastos es: "+ categorias.get(0).categoria);
                        }else {
                            categoriaMasComun.setText("Ningunca Categoria presente");
                        }
                        Comparator<Transaccion> comparador = (t1, t2) -> t1.getFecha().compareTo(t2.getFecha());
                        transaccions.sort(comparador);
                        for(int i = transaccions.size()-1; i>=0; i--){
                            adaptador.agregarTransaccion(transaccions.get(i));
                        }
                    }
                }
        );


    }

    private ArrayList<Transaccion> ordenarTransacciones(ArrayList<Transaccion> transaccions) {
        Comparator<Transaccion> comparador = (t1, t2) -> t1.getFecha().compareTo(t2.getFecha());
        transaccions.sort(comparador);
        return transaccions;
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