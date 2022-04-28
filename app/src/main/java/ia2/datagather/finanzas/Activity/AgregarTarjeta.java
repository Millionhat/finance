package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class AgregarTarjeta extends AppCompatActivity implements View.OnClickListener{

    private TextView nombreTarjeta,montoTotal,descripcionTarjeta;
    private Spinner tipoTarjeta;
    private Button agregarTarjetaBtn, cancelarBtn;
    private Usuario usuario;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarjeta);
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        nombreTarjeta = findViewById(R.id.nombreTarjetaTextView);
        montoTotal = findViewById(R.id.montoMaximoTextView);
        descripcionTarjeta = findViewById(R.id.descripcionTextView);
        tipoTarjeta = (Spinner) findViewById(R.id.spinnerTipoTarjeta);
        List<String> list = new ArrayList<>();
        list.add("Credito");
        list.add("Debito");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this,R.layout.support_simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tipoTarjeta.setAdapter(dataAdapter);
        agregarTarjetaBtn = findViewById(R.id.agregarBtn);
        cancelarBtn = findViewById(R.id.cancelarBtn);
        agregarTarjetaBtn.setOnClickListener(this);
        cancelarBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.agregarBtn:
                String nombre = nombreTarjeta.getText().toString();
                String tipo = tipoTarjeta.getSelectedItem().toString();
                String descripcion = descripcionTarjeta.getText().toString();
                Double monto = Double.parseDouble(montoTotal.getText().toString());
                if(nombre!="" && tipo!="" && monto>0){
                    Tarjeta nuevaTarjeta = new Tarjeta(UUID.randomUUID().toString(),nombre,tipo,descripcion,monto);
                    db.collection("usuarios").document(usuario.getId()).collection("tarjetas").document(nuevaTarjeta.getId()).set(nuevaTarjeta);
                    finish();
                }
                break;

            case R.id.cancelarBtn:
                finish();
                break;
        }
    }
}