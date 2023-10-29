package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    private static final String ARQ_userDATA = "userDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferences preferences = getSharedPreferences(ARQ_userDATA,0);

                if (preferences.contains("userlogado")){

                    Integer tipoUser= Integer.parseInt(preferences.getString("id_user","1"));

                    if (tipoUser == 1) {
                        Intent intent = new Intent(getBaseContext(), tela_home_comum.class);
                        startActivity(intent);
                    }
                    if (tipoUser == 2) {
                        Intent intent = new Intent(getBaseContext(), tela_homeAdmin.class);
                        startActivity(intent);
                    }
                    if (tipoUser == 3) {
                        Intent intent = new Intent(getBaseContext(), tela_home_monitor.class);
                        startActivity(intent);
                    }



                }
                else{

                        Intent intent = new Intent(getBaseContext(), tela_inicial.class);
                        startActivity(intent);


                }

            }
        }, 1500);


    }
}