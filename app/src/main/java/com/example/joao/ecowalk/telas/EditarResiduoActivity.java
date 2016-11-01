package com.example.joao.ecowalk.telas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.joao.ecowalk.R;

public class EditarResiduoActivity extends AppCompatActivity {

    private TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_residuo);

        texto = (TextView) findViewById(R.id.textView);
        texto.setText(ResiduosActivity.artefato_editado.getDescricao());
    }

}
