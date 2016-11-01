package com.example.joao.ecowalk.telas;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joao.ecowalk.R;
import com.example.joao.ecowalk.classe.Email;

public class AddLixoActivity extends ActionBarActivity
{
    private Toolbar mToolbar;
    private Button botao_adicionar;
    private EditText et_nome, et_descricao, et_info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lixo);

        mToolbar = (Toolbar) findViewById(R.id.tb_lixo);
        mToolbar.setTitle("EcoWalk");
        mToolbar.setSubtitle("Adicionar ponto de resíduo");
        setSupportActionBar(mToolbar);
        setupActionBar();

        et_nome = (EditText) findViewById(R.id.editText_nome_local);
        et_descricao = (EditText) findViewById(R.id.editText_descricao_local);
        et_info = (EditText) findViewById(R.id.editText_info_ecologicas);

        /*CRIANDO EVENTOS PARA OS BOTÕES*/
        botao_adicionar = (Button) findViewById(R.id.bt_adicionar);
        clickAdicionar();

    }

    private void clickAdicionar() {

        botao_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResiduosActivity r = new ResiduosActivity();
                Log.i("Latidude: ", String.valueOf(ResiduosActivity.g_mMap.getMyLocation().getLatitude()));
                Email email = new Email();

                email.addResiduo(et_nome.getText().toString(), et_descricao.getText().toString(), et_info.getText().toString(), String.valueOf(ResiduosActivity.g_mMap.getMyLocation().getLatitude()), String.valueOf(ResiduosActivity.g_mMap.getMyLocation().getLongitude()));

                Toast.makeText(getApplicationContext(), "Ponto enviado para análise", Toast.LENGTH_LONG).show();

                /*CHAMANDO NOVA TELA*/
                Intent intent = new Intent();
                intent.setClass(AddLixoActivity.this, ResiduosActivity.class);
                startActivity(intent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*EVENTO PARA VOLTAR TELA*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(AddLixoActivity.this, ResiduosActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
