package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import ia2.datagather.finanzas.List.AdaptadorTarjeta;
import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class CardsActivity extends AppCompatActivity implements View.OnClickListener, AdaptadorTarjeta.PresionarTarjetaListener{

    private TextView titulo;
    private Usuario usuario;
    private RecyclerView listaTarjeta;
    private FirebaseFirestore db;
    private Button creatTarjetaBtn,reportesBtn;
    private AdaptadorTarjeta adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        db = FirebaseFirestore.getInstance();
        this.titulo = findViewById(R.id.tituloTarjetas);
        listaTarjeta = findViewById(R.id.recyclerView);
        this.usuario = (Usuario) getIntent().getExtras().get("usuario");
        adaptador = new AdaptadorTarjeta();
        adaptador.setListener(this);
        listaTarjeta.setAdapter(adaptador);
        listaTarjeta.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listaTarjeta.setLayoutManager(manager);
        creatTarjetaBtn = findViewById(R.id.crearTarjeta);
        creatTarjetaBtn.setOnClickListener(this);
        reportesBtn = findViewById(R.id.tarjetasReporteBtn);
        reportesBtn.setOnClickListener(this);
        cargarTarjetas();
        Toast.makeText(this,"TerminoSetup",Toast.LENGTH_LONG);
    }

    public void cargarTarjetas() {
        Query refTarjetas =db.collection("usuarios").document(usuario.getId()).collection("tarjetas").orderBy("nombre").limit(10);
        refTarjetas.get().addOnCompleteListener(
                t->{
                    adaptador.limpiarLista();
                    for(QueryDocumentSnapshot doc: t.getResult()){
                        Tarjeta tarjeta =doc.toObject(Tarjeta.class);
                        adaptador.agregarTarjeta(tarjeta);
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.crearTarjeta:
                Intent i = new Intent(this, AgregarTarjeta.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                break;

            case R.id.tarjetasReporteBtn:
                Intent j = new Intent(this, ReporteGeneralActivity.class);
                j.putExtra("usuario",usuario);
                startActivity(j);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTarjetas();
    }

    @Override
    public void tarjetaPresionada(Tarjeta tarjeta) {
        Intent i = new Intent(this, DetalleTarjeta.class);
        i.putExtra("usuario", usuario);
        i.putExtra("tarjeta", tarjeta);
        startActivity(i);
    }
}