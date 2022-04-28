package ia2.datagather.finanzas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ia2.datagather.finanzas.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarIngreso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarIngreso extends Fragment implements View.OnClickListener{

    public AgregarIngreso() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AgregarIngreso newInstance() {
        AgregarIngreso fragment = new AgregarIngreso();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_ingreso,container,false);
        TextView valor = (TextView) view.findViewById(R.id.montoIngresoTV);
        Button agregar = (Button) view.findViewById(R.id.crearIngresoBtn);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crearIngresoBtn:
                Intent salida = new Intent();
                getActivity().setResult(Activity.RESULT_OK,salida);
                getActivity().finish();
                break;
        }
    }
}