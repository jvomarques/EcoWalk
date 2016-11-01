package com.example.joao.ecowalk.telas;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joao.ecowalk.R;
import com.example.joao.ecowalk.layout.SlidingTabLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

//Toast.makeText(getApplicationContext(), tam, Toast.LENGTH_LONG).show();


public class MenuActivity extends ActionBarActivity {

    private Button botao_residuo;
    private Button botao_login;
    private Button botao_configuracoes;
    private Button botao_rotas;

    private TextView txtTam_local;

    public List<String> lista_coordenadas;


    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mi  pmap.ic_launcher);
        */


        /*CRIANDO EVENTOS PARA OS BOTÕES*/
        botao_residuo = (Button) findViewById(R.id.bt_residuo);
        clickBotaoResiduo();

        botao_login = (Button) findViewById(R.id.bt_login);
        clickBotaoLogin();

        botao_configuracoes = (Button) findViewById(R.id.bt_configuracoes);
        clickBotaoConfiguracoes();

        botao_rotas = (Button) findViewById(R.id.bt_rotas);
        clickBotaoRotas();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void clickBotaoResiduo() {

        botao_residuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CHAMANDO NOVA TELA*/
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, ResiduosActivity.class);
                startActivity(intent);
            }
        });
    }


    private void clickBotaoLogin() {

        botao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void clickBotaoConfiguracoes() {

        botao_configuracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CHAMANDO NOVA TELA*/
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, ConfiguracoesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickBotaoRotas(){

        botao_rotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*CHAMANDO NOVA TELA*/
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, RotasActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Menu Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.joao.ecowalk/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Menu Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.joao.ecowalk/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
