package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ia2.datagather.finanzas.Model.Gasto;
import ia2.datagather.finanzas.Model.Tarjeta;
import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class AgregarGasto extends AppCompatActivity implements View.OnClickListener{

    private TextView descripcion, monto;
    private Spinner categoria;
    private Button cancelarBtn, crearBtn;
    private Usuario usuario;
    private Tarjeta tarjeta;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto);
        usuario = (Usuario) getIntent().getExtras().get("usuario");
        tarjeta = (Tarjeta) getIntent().getExtras().get("tarjeta");
        descripcion = findViewById(R.id.descripcionAgregarGastoTV);
        categoria = (Spinner) findViewById(R.id.spinnerCategoriaAgregarGasto);
        monto = findViewById(R.id.valorAgregarGastoTV);
        List<String> list = new ArrayList<>();
        list.add("Entretenimiento");
        list.add("Comida");
        list.add("Viajes");
        list.add("Educación");
        list.add("Otros");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item,list);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoria.setAdapter(dataAdapter);
        cancelarBtn = findViewById(R.id.cancelarAgregarGastoBtn);
        cancelarBtn.setOnClickListener(this);
        crearBtn = findViewById(R.id.agregarNuevoGastoBtn);
        crearBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.agregarNuevoGastoBtn:
                String desc = descripcion.getText().toString();
                String cat = categoria.getSelectedItem().toString();
                Double valor = Double.parseDouble(monto.getText().toString());
                if(desc!="" && cat!="" && valor > 0) {
                    Date date = new Date();
                    Gasto nuevoGasto = new Gasto(UUID.randomUUID().toString(),cat,desc,valor, new Date());
                    tarjeta.getGastos().add(nuevoGasto);
                    db.collection("usuarios").document(usuario.getId()).collection("tarjetas")
                            .document(tarjeta.getId()).set(tarjeta);
                    Intent i = new Intent();
                    setResult(RESULT_OK,i);
                    finish();
                }
                break;

            case R.id.cancelarAgregarGastoBtn:
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
                break;
        }

    }
}