package com.example.joao.ecowalk.classe;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by joao on 23/09/16.
 */
public class Email
{
    public void enviaExclusao(final String nome, final double latitude, final double longitude)
    {

        final GMailSender sender = new GMailSender("ecowalkufrn@gmail.com", "caminhando");
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {
                    sender.sendMail("EcoWalk - Exclusão de ponto de lixo",
                            "Nome: " + nome + "\nLatidude: " + latitude + "\nLongitude: " + longitude,
                            "ecowalkufrn@gmail.com",
                            "jvo.marques@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;
            }
        }   .execute();
    }

    public void addResiduo(final String nome, final String descricao, final String info_ecologicas, final String latitude, final String longitude)
    {
        final GMailSender sender = new GMailSender("ecowalkufrn@gmail.com", "caminhando");
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {
                    sender.sendMail("EcoWalk - Adição de ponto de resíduo",
                            "Nome: " + nome + "\nDescricao: " + descricao + "\nInformações ecologicas: " + info_ecologicas + "\nLatitude: " + latitude + "\nLongitude: " + longitude ,
                            "ecowalkufrn@gmail.com",
                            "jvo.marques@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;
            }
        }   .execute();
    }

}
