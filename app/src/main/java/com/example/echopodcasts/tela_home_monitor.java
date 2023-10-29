package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.echopodcasts.R;
import com.example.echopodcasts.ouvir_categorias;
import com.example.echopodcasts.tela_inicial;
import com.example.echopodcasts.upload_arquivo;

public class tela_home_monitor extends AppCompatActivity {

    String apelidoUser;
    private static final String ARQ_userDATA = "userDATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_home_monitor);
        apelidoUser = getIntent().getStringExtra("apelidoUser");
    }

    public void abrirTelaEnviar(View v) {

        Intent intent = new Intent(this, upload_arquivo.class);
        intent.putExtra("apelidoUser", apelidoUser);
        startActivity(intent);
    }

    public void abrirTelaCategorias(View v) {

        Intent intent = new Intent(this, ouvir_categorias.class);
        startActivity(intent);
    }


    public void sair(View v) {

        SharedPreferences preferences = getSharedPreferences(ARQ_userDATA, 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("userlogado");
        editor.remove("passLogado");
        editor.commit();

        Intent intent = new Intent(this, tela_inicial.class);
        startActivity(intent);

    }
}