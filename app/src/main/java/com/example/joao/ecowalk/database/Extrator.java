package com.example.joao.ecowalk.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;

import com.example.joao.ecowalk.classe.Artefato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by joao on 12/04/16.
 */
public class Extrator extends ActionBarActivity {

    private SQLiteDatabase db;
    private Conexao banco;

    public Extrator(Context context)
    {
        banco = new Conexao(context);
        db = banco.getWritableDatabase();
    }

    public void iniciarBanco(Context context)
    {
        db.execSQL("DELETE FROM Artefatos;");

        /*LENDO ARQUIVO TXT*/
        AssetManager am = context.getAssets();
        try {
            InputStream c = am.open("Artefatos.tsv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(am.open("Artefatos.tsv"))
                    .useDelimiter("\\||\\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {

            ContentValues valores = new ContentValues();

            Artefato artefatos_iniciais = new Artefato();

            artefatos_iniciais.setInformacoes_ecologicas(scanner.next());
            valores.put("informacoes_ecologicas", artefatos_iniciais.getInformacoes_ecologicas());

            artefatos_iniciais.setLatidude(Double.parseDouble(scanner.next()));
            valores.put("latitude", artefatos_iniciais.getLatidude());

            artefatos_iniciais.setLongitude(Double.parseDouble(scanner.next()));
            valores.put("longitude", artefatos_iniciais.getLongitude());

            artefatos_iniciais.setDescricao(scanner.next());
            valores.put("descricao", artefatos_iniciais.getDescricao());

            artefatos_iniciais.setNome(scanner.next());
            valores.put("nome", artefatos_iniciais.getNome());

            /*Classe*/
            artefatos_iniciais.setDetalhes(scanner.next());
            valores.put("detalhes", artefatos_iniciais.getDetalhes());

            db.insert("Artefatos", null, valores);
            //db.close();

        }
        /*LENDO ARQUIVO TXT*/

    }

    public List<Artefato> getPontosResiduos()
    {
        List<Artefato> lista_artefatos = new ArrayList<Artefato>();
        lista_artefatos.clear();

        String comando_sql = "SELECT * FROM Artefatos";


        String[] colunas = new String[]{"informacoes_ecologicas", "latitude", "longitude", "descricao", "nome", "detalhes"};

        db = banco.getReadableDatabase();

        comando_sql = "SELECT * FROM Artefatos";

        Cursor cursor = db.rawQuery(comando_sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                Artefato artefato = new Artefato();

                artefato.setInformacoes_ecologicas(cursor.getString(0));
                artefato.setLatidude(cursor.getDouble(1));
                artefato.setLongitude(cursor.getDouble(2));
                artefato.setDescricao(cursor.getString(3));
                artefato.setNome(cursor.getString(4));
                artefato.setDetalhes(cursor.getString(5));

                lista_artefatos.add(artefato);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lista_artefatos;
    }
}
