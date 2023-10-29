package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class tela_inicial extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);


    }



    public void abrirTelaLogin(View view){

        Intent intent = new Intent(this, tela_login.class);
        startActivity(intent);
    }

    public void abrirTelaCadastro(View view){

        Intent intent = new Intent(this, cadastro_comum.class);
        startActivity(intent);
    }



}
