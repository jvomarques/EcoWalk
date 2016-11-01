package com.example.joao.ecowalk.telas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joao.ecowalk.R;
import com.example.joao.ecowalk.classe.Artefato;
import com.example.joao.ecowalk.classe.Email;
import com.example.joao.ecowalk.database.Extrator;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.joao.ecowalk.R.drawable.ic_deletar_residuo;
import static com.example.joao.ecowalk.R.drawable.ic_edicao;
import static com.example.joao.ecowalk.R.drawable.lixo_icone;
import static com.example.joao.ecowalk.R.drawable.ponto;


public class ResiduosActivity extends ActionBarActivity implements OnMapReadyCallback, InfoWindowAdapter {

    public static GoogleMap g_mMap;
    private Toolbar mToolbar;

    public static Artefato artefato_adicionado, artefato_editado, artefato_excluido;

    private DialogInterface.OnClickListener alert_listener;

    /*UTILIZANDO FLOAT ACTION BUTTON*/
    private FloatingActionButton fab;

    int op_menu;

    /*NAVIGATION DRAWER*/
    private Drawer.Result navigationDrawer_esquerda;
    private AccountHeader.Result headerNavigation_esquerda;

    private GoogleApiClient client;

    /*VARIAVEIS PARA EXCLUSAO DE UM PONTO*/
    private String nome_pontoexcluao;
    private double latidue_pontoexlusao, longitude_pontoexclusao;


    private double latitude_atual, longitude_atual;

    public void setLatitudeAtual(double latitude)
    {
        this.latitude_atual = latitude;
    }

    public double getLatitudeAtual()
    {
        return  this.latitude_atual;
    }

    public void setLongitudeAtual(double longitude)
    {
        this.longitude_atual = longitude;
    }

    public double getLongitudeAtual()
    {
        return  this.longitude_atual;
    }



    // METODOS USADOS NOS COMPONENTES 'SWITCH' DO NAVIGATION DRAWER
    private OnCheckedChangeListener mOnCheckedChangeListener_mapahibrido = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {

            if(isChecked)
                g_mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            else
                g_mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residuos);

        // SETA DE RETORNO DA TELA NO TOOLBAR
        //setupActionBar();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar = (Toolbar) findViewById(R.id.tb_lixo);
        mToolbar.setTitle("EcoWalk");
        mToolbar.setSubtitle("Resíduos");
        mToolbar.setLogo(R.drawable.lixo_nao_perigoso_nao_inerte);
        setSupportActionBar(mToolbar);

        alert_listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE)
                {
                    Log.i("Confirmação:", "OK!");
                    Email email_exclusaoponto = new Email();
                    email_exclusaoponto.enviaExclusao(nome_pontoexcluao, latidue_pontoexlusao, longitude_pontoexclusao);
                    Toast.makeText(getApplicationContext(), "Exlcusão enviada para analise", Toast.LENGTH_LONG).show();
                }
                else if(which == DialogInterface.BUTTON_NEGATIVE)
                {
                    Log.i("Confirmação:", "Cancelado");
                }

            }
        };

        // NAVIGATION DRAWER
        navigationDrawer_esquerda = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                        // EVENTOS DE MUDANÇA DE CONSULTA NO BANCO DE ACORDO COM O ITEM SELECIONADO
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        //VARIAVEL USADA PARA IDENTIFICAR A OPÇÃO DO MENU ESCOLHIDA NO EVENTO DE ZOOM
                        op_menu = position;
                        if (op_menu == 1) {

                            setLatitudeAtual(g_mMap.getMyLocation().getLatitude());
                            setLongitudeAtual(g_mMap.getMyLocation().getLongitude());

                            //artefato_adicionado.setLatidude(g_mMap.getMyLocation().getLatitude());
                            //artefato_adicionado.setLongitude(g_mMap.getMyLocation().getLongitude());

                            /*CHAMANDO NOVA TELA*/
                            Intent intent = new Intent();
                            intent.setClass(ResiduosActivity.this, AddLixoActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            g_mMap.clear();
                            List<Artefato> lista_lixos = new ArrayList<Artefato>();

                            Extrator ferramentas_banco = new Extrator(getBaseContext());
                            ferramentas_banco.iniciarBanco(getBaseContext());

                            lista_lixos = ferramentas_banco.getPontosResiduos();
                            for (int i = 0; i < lista_lixos.size(); i++) {
                                if (op_menu == 3) {
                                    g_mMap.addMarker(new MarkerOptions()
                                                    .title(lista_lixos.get(i).getNome())
                                                    .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                    .icon(BitmapDescriptorFactory.fromResource(ic_edicao))
                                    ).setSnippet(lista_lixos.get(i).getDescricao());
                                }
                                else if(op_menu == 2)
                                {
                                    g_mMap.addMarker(new MarkerOptions()
                                                    .title(lista_lixos.get(i).getNome())
                                                    .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                    .icon(BitmapDescriptorFactory.fromResource(ic_deletar_residuo))
                                    ).setSnippet(lista_lixos.get(i).getDescricao());

                                }
                                else {
                                    g_mMap.addMarker(new MarkerOptions()
                                                    .title(lista_lixos.get(i).getNome())
                                                    .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                    .icon(BitmapDescriptorFactory.fromResource(ponto))
                                    ).setSnippet(lista_lixos.get(i).getDescricao());
                                }
                            }

                        /* INICIO - PERSONALIZANDO INFOWINDOW */
                            g_mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {

                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    LinearLayout ll = new LinearLayout(ResiduosActivity.this);
                                    ll.setPadding(40, 40, 40, 40);

                                    ll.setOrientation(LinearLayout.VERTICAL);

                                    TextView txt_v = new TextView(ResiduosActivity.this);
                                    txt_v.setText(Html.fromHtml("<b> <font color=\"#000000\" >" + marker.getSnippet() + " </font></b>"));
                                    ll.addView(txt_v);

                                    ImageView img = new ImageView(ResiduosActivity.this);
                                    img.setImageResource(R.drawable.lixo_foto);

                                    //Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").resize(50,50).into(img);


                                    ll.addView(img);

                                    return ll;
                                }
                            });
                        /* FIM - PERSONALIZANDO INFOWINDOW */

                        /* INICIO - EVENTOS */

                            //INICIALIZANDO CAMERA
                            g_mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lista_lixos.get(0).getLatidude(), lista_lixos.get(0).getLongitude()), 16));

                            //EVENTO MARCADOR
                            g_mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if(op_menu == 2)
                                    {
                                        AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                                        alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                                        alert_confirmacao.setTitle("Informação");
                                        alert_confirmacao.setPositiveButton("Sim", alert_listener);
                                        alert_confirmacao.setNegativeButton("Não", alert_listener);

                                        nome_pontoexcluao = marker.getSnippet();
                                        latidue_pontoexlusao = marker.getPosition().latitude;
                                        longitude_pontoexclusao = marker.getPosition().longitude;

                                        alert_confirmacao.show();
                                    }
                                    else if(op_menu == 3)
                                    {
                                        artefato_editado.setDescricao("oi");
                                        /*CHAMANDO NOVA TELA*/
                                        Intent intent = new Intent();
                                        intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                                        startActivity(intent);
                                    }
                                    return false;
                                }
                            });

                            //EVENTO INFOWINDOW
                            g_mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    if(op_menu == 2)
                                    {
                                        AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                                        alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                                        alert_confirmacao.setTitle("Informação");
                                        alert_confirmacao.setPositiveButton("Sim", alert_listener);
                                        alert_confirmacao.setNegativeButton("Não", alert_listener);

                                        nome_pontoexcluao = marker.getTitle();
                                        latidue_pontoexlusao = marker.getPosition().latitude;
                                        longitude_pontoexclusao = marker.getPosition().longitude;

                                        alert_confirmacao.show();
                                    }
                                    else if(op_menu == 3)
                                    {
                                        artefato_editado.setDescricao("oi");
                                        /*CHAMANDO NOVA TELA*/
                                        Intent intent = new Intent();
                                        intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        /* FIM - EVENTOS */

                        }
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        Toast.makeText(getApplicationContext(), "Item: " + position, Toast.LENGTH_LONG).show();
                        return false;
                    }
                })
                .build();
        navigationDrawer_esquerda.addItem(new SectionDrawerItem().withName("Edição de resíduos"));

        navigationDrawer_esquerda.addItem(new PrimaryDrawerItem().withName("Adicionar").withIcon(R.drawable.ic_add));
        navigationDrawer_esquerda.addItem(new PrimaryDrawerItem().withName("Remover").withIcon(R.drawable.ic_delete_black_24dp));
        navigationDrawer_esquerda.addItem(new PrimaryDrawerItem().withName("Editar").withIcon(R.drawable.ic_edicao));

        navigationDrawer_esquerda.addItem((new DividerDrawerItem()));

        navigationDrawer_esquerda.addItem(new SecondaryDrawerItem().withName("Configurações"));
        navigationDrawer_esquerda.addItem(new SwitchDrawerItem()
                .withName("Mapa híbrido").withIcon(R.drawable.ic_map_black_24dp)
                .withOnCheckedChangeListener(mOnCheckedChangeListener_mapahibrido));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /**
     * Set up the {@link ActionBar}, if the API is available.
     */
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
            startActivity(new Intent(ResiduosActivity.this, MenuActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        g_mMap = googleMap;

        //HABILITANDO BOTAO PARA "MINHA LOCALIZACAO"
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ResiduosActivity.g_mMap.setMyLocationEnabled(true);

        ResiduosActivity.g_mMap.getUiSettings().setMapToolbarEnabled(true);
        ResiduosActivity.g_mMap.getUiSettings().setZoomControlsEnabled(true);


        List<Artefato> lista_lixos = new ArrayList<Artefato>();

        Extrator ferramentas_banco = new Extrator(getBaseContext());
        ferramentas_banco.iniciarBanco(getBaseContext());

        ImageView img = new ImageView(ResiduosActivity.this);
        Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").resize(10, 10).into(img);

        lista_lixos = ferramentas_banco.getPontosResiduos();
        for (int i = 0; i < lista_lixos.size(); i++) {
            if (op_menu == 3) {
                g_mMap.addMarker(new MarkerOptions()
                                .title(lista_lixos.get(i).getNome())
                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(ic_edicao))
                ).setSnippet(lista_lixos.get(i).getDescricao());
            } else if (op_menu == 2) {
                g_mMap.addMarker(new MarkerOptions()
                                .title(lista_lixos.get(i).getNome())
                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(ic_deletar_residuo))
                ).setSnippet(lista_lixos.get(i).getDescricao());

            } else {
                g_mMap.addMarker(new MarkerOptions()
                                .title(lista_lixos.get(i).getNome())
                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(ponto))
                ).setSnippet(lista_lixos.get(i).getDescricao());
            }
        }

        /* INICIO - PERSONALIZANDO INFOWINDOW */
        g_mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout ll = new LinearLayout(ResiduosActivity.this);
                ll.setPadding(40, 40, 40, 40);

                ll.setOrientation(LinearLayout.VERTICAL);

                TextView txt_v = new TextView(ResiduosActivity.this);
                txt_v.setText(Html.fromHtml("<b> <font color=\"#000000\" >" + marker.getSnippet() + " </font></b>"));
                ll.addView(txt_v);

                ImageView img = new ImageView(ResiduosActivity.this);
                img.setImageResource(R.drawable.lixo_foto);

                Picasso.with(getApplicationContext()).load("https://www.dropbox.com/home?preview=20160504_154110.jpg").resize(50,50).into(img);

                ll.addView(img);

                return ll;
            }
        });
        /* FIM - PERSONALIZANDO INFOWINDOW */

        /* INICIO - EVENTOS */

        //INICIALIZANDO CAMERA
        g_mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lista_lixos.get(0).getLatidude(), lista_lixos.get(0).getLongitude()), 16));

        //EVENTO MARCADOR
        g_mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(op_menu == 2)
                {
                    AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                    alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                    alert_confirmacao.setTitle("Informação");
                    alert_confirmacao.setPositiveButton("Sim", alert_listener);
                    alert_confirmacao.setNegativeButton("Não", alert_listener);

                    nome_pontoexcluao = marker.getTitle();
                    latidue_pontoexlusao = marker.getPosition().latitude;
                    longitude_pontoexclusao = marker.getPosition().longitude;

                    alert_confirmacao.show();
                }
                else if(op_menu == 3)
                {
                    artefato_editado.setDescricao("oi");
                                        /*CHAMANDO NOVA TELA*/
                    Intent intent = new Intent();
                    intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        //EVENTO INFOWINDOW
        g_mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(op_menu == 2)
                {
                    AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                    alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                    alert_confirmacao.setTitle("Informação");
                    alert_confirmacao.setPositiveButton("Sim", alert_listener);
                    alert_confirmacao.setNegativeButton("Não", alert_listener);

                    nome_pontoexcluao = marker.getTitle();
                    latidue_pontoexlusao = marker.getPosition().latitude;
                    longitude_pontoexclusao = marker.getPosition().longitude;

                    alert_confirmacao.show();
                }
                else if(op_menu == 3)
                {
                    artefato_editado.setDescricao("oi");
                                        /*CHAMANDO NOVA TELA*/
                    Intent intent = new Intent();
                    intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                    startActivity(intent);
                }
            }
        });

        /* FIM - EVENTOS */


        g_mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom) {

                    currentZoom = pos.zoom;
                    if (pos.zoom >= 19)
                    {
                        g_mMap.clear();
                        List<Artefato> lista_lixos = new ArrayList<Artefato>();

                        Extrator ferramentas_banco = new Extrator(getBaseContext());
                        ferramentas_banco.iniciarBanco(getBaseContext());

                        lista_lixos = ferramentas_banco.getPontosResiduos();
                        for (int i = 0; i < lista_lixos.size(); i++) {
                            if (op_menu == 3) {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(ic_edicao))
                                ).setSnippet(lista_lixos.get(i).getDescricao());
                            } else if (op_menu == 2) {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(ic_deletar_residuo))
                                ).setSnippet(lista_lixos.get(i).getDescricao());

                            } else {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(lixo_icone))
                                ).setSnippet(lista_lixos.get(i).getDescricao());
                            }
                        }

                    }
                    else if(pos.zoom < 19)
                    {
                        g_mMap.clear();
                        List<Artefato> lista_lixos = new ArrayList<Artefato>();

                        Extrator ferramentas_banco = new Extrator(getBaseContext());
                        ferramentas_banco.iniciarBanco(getBaseContext());

                        lista_lixos = ferramentas_banco.getPontosResiduos();
                        for (int i = 0; i < lista_lixos.size(); i++) {
                            if (op_menu == 3) {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(ic_edicao))
                                ).setSnippet(lista_lixos.get(i).getDescricao());
                            } else if (op_menu == 2) {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(ic_deletar_residuo))
                                ).setSnippet(lista_lixos.get(i).getDescricao());

                            } else {
                                g_mMap.addMarker(new MarkerOptions()
                                                .title(lista_lixos.get(i).getNome())
                                                .position(new LatLng(lista_lixos.get(i).getLatidude(), lista_lixos.get(i).getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(ponto))
                                ).setSnippet(lista_lixos.get(i).getDescricao());
                            }
                        }

                    }

                        /* INICIO - PERSONALIZANDO INFOWINDOW */
                    g_mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {

                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            LinearLayout ll = new LinearLayout(ResiduosActivity.this);
                            ll.setPadding(40, 40, 40, 40);

                            ll.setOrientation(LinearLayout.VERTICAL);

                            TextView txt_v = new TextView(ResiduosActivity.this);
                            txt_v.setText(Html.fromHtml("<b> <font color=\"#000000\" >" + marker.getSnippet() + " </font></b>"));
                            ll.addView(txt_v);

                            ImageView img = new ImageView(ResiduosActivity.this);
                            img.setImageResource(R.drawable.lixo_foto);

                            //Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").resize(50,50).into(img);

                            ll.addView(img);

                            return ll;
                        }
                    });
                        /* FIM - PERSONALIZANDO INFOWINDOW */

                        /* INICIO - EVENTOS */

                    //EVENTO MARCADOR
                    g_mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if(op_menu == 2)
                            {
                                AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                                alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                                alert_confirmacao.setTitle("Informação");
                                alert_confirmacao.setPositiveButton("Sim", alert_listener);
                                alert_confirmacao.setNegativeButton("Não", alert_listener);

                                nome_pontoexcluao = marker.getTitle();
                                latidue_pontoexlusao = marker.getPosition().latitude;
                                longitude_pontoexclusao = marker.getPosition().longitude;

                                alert_confirmacao.show();
                            }
                            else if(op_menu == 3)
                            {
                                artefato_editado.setDescricao("oi");
                                        /*CHAMANDO NOVA TELA*/
                                Intent intent = new Intent();
                                intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                                startActivity(intent);
                            }
                            return false;
                        }
                    });

                    //EVENTO INFOWINDOW
                    g_mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            if(op_menu == 2)
                            {
                                AlertDialog.Builder alert_confirmacao = new AlertDialog.Builder(ResiduosActivity.this);
                                alert_confirmacao.setMessage("Deseja realmente excluir esse ponto?");
                                alert_confirmacao.setTitle("Informação");
                                alert_confirmacao.setPositiveButton("Sim", alert_listener);
                                alert_confirmacao.setNegativeButton("Não", alert_listener);

                                nome_pontoexcluao = marker.getTitle();
                                latidue_pontoexlusao = marker.getPosition().latitude;
                                longitude_pontoexclusao = marker.getPosition().longitude;

                                alert_confirmacao.show();
                            }
                            else if(op_menu == 3)
                            {
                                artefato_editado.setDescricao("oi");
                                /*CHAMANDO NOVA TELA*/
                                Intent intent = new Intent();
                                intent.setClass(ResiduosActivity.this, EditarResiduoActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                        /* FIM - EVENTOS */
                    Log.d("Zoom", "" + pos.zoom);

                }
            }
        });

        fab = (FloatingActionButton)  findViewById(R.id.fab);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #g_mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (g_mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            g_mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (g_mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #g_mMap} is not null.
     */
    private void setUpMap() {


        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onStart() {

        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
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
                "Maps Page", // TODO: Define a title for the content shown.
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

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}