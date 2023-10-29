package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ouvir_categorias extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouvir_categorias);

    }

    public void filtroPortugues(View view){

        abrirListplayer("Português");
    }
    public void filtroMatematica(View view){

        abrirListplayer("Matemática");
    }
    public void filtroHistoria(View view){

        abrirListplayer("História");
    }
    public void filtroGeografia(View view){

        abrirListplayer("Geografia");
    }
    public void filtroBiologia(View view){

        abrirListplayer("Biologia");
    }
    public void filtroFisica(View view){

        abrirListplayer("Física");
    }
    public void filtroQuimica(View view){

        abrirListplayer("Química");
    }
    public void filtroFilosofia(View view){

        abrirListplayer("Filosofia");
    }
    public void filtroIngles(View view){

        abrirListplayer("Inglês");
    }
    public void filtroEspanhol(View view){

        abrirListplayer("Espanhol");
    }

    public void abrirListplayer(String filter){

        Intent intent = new Intent(this, tela_player.class);
        intent.putExtra("filter", filter);
        startActivity(intent);

    }





}