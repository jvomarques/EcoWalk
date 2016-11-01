package com.example.joao.ecowalk.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by joao on 31/05/16.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String[] titulos = {"TODOS, LIXO 1, LIXO 2, LIXO 3, Lixo 4"};


    public TabsAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        mContext = ctx;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        if(position == 0)
            Toast.makeText(mContext, "Tela 1", Toast.LENGTH_LONG).show();
        else if(position == 1)
            Toast.makeText(mContext, "Tela 2", Toast.LENGTH_LONG).show();
        else if(position == 2)
            Toast.makeText(mContext, "Tela 3", Toast.LENGTH_LONG).show();
        else if(position == 3)
            Toast.makeText(mContext, "Tela 4", Toast.LENGTH_LONG).show();
        else if(position == 4)
            Toast.makeText(mContext, "Tela 5", Toast.LENGTH_LONG).show();


        return frag;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }


}
