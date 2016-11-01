package com.example.joao.ecowalk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joao on 12/04/16.
 */
public class Conexao extends SQLiteOpenHelper {

    private static final String nome_banco = "BANCOECOWALK.db";
    private static final int versao = 1;

    /*NOME DA TABELA*/
    private static final String table_artefatos = "Artefatos";
    /*NOME DAS COLUNAS*/
    private static final String id_artefato = "idArtefato";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";

    /*NOME DA TABELA*/
    private static final String table_tipoArtefatos = "TiposArtefatos";


    public Conexao(Context context){
        super(context, nome_banco,null,versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE Artefatos " +
                        "(idArtefato INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                        "informacoes_ecologicas VARCHAR(2000), " +
                        "latitude DOUBLE, " +
                        "longitude DOUBLE, " +
                        "descricao VARCHAR(2000), " +
                        "nome VARCHAR(200), " +
                        "detalhes VARCHAR(2000));";


        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Artefatos;");
        onCreate(db);
    }
}
