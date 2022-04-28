package ia2.datagather.finanzas.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.UUID;

import ia2.datagather.finanzas.Model.Usuario;
import ia2.datagather.finanzas.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView username;
    private TextView password;
    private Button loginBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"entro",Toast.LENGTH_LONG);
        loginBtn = findViewById(R.id.LoginBtn);
        password = findViewById(R.id.LoginPassword);
        username = findViewById(R.id.LoginUsername);
        db = FirebaseFirestore.getInstance();
        loginBtn.setOnClickListener(this);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET
        },1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginBtn:
                Toast.makeText(this,"entro",Toast.LENGTH_LONG);
                String user = username.getText().toString().toLowerCase();
                String pswrd = password.getText().toString();
                Usuario ingreso = new Usuario(UUID.randomUUID().toString(), user, pswrd);
                CollectionReference usuarioRef = db.collection("usuarios");
                Query query = usuarioRef.whereEqualTo("username", ingreso.getUsername());
                query.get().addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        if (t.getResult().size() > 0) {
                            for (QueryDocumentSnapshot doc : t.getResult()) {
                                Usuario dbUsuario = doc.toObject(Usuario.class);
                                if (dbUsuario.getPassword().equals(pswrd)) {
                                    goToCardsPage(dbUsuario);
                                    break;
                                }
                            }
                        } else {
                            db.collection("usuarios").document(ingreso.getId()).set(ingreso);
                            goToCardsPage(ingreso);
                        }
                    }
                });
                break;
        }
    }

    private void goToCardsPage(Usuario usr){
        Intent i = new Intent(this,CardsActivity.class);
        i.putExtra("usuario", usr);
        startActivity(i);
    }
}