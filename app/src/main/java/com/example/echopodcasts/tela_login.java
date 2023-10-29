package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class tela_login extends AppCompatActivity {

    String urlWebServicesDesenvolvimento = "http://192.168.15.149/echopod/getUsuarios.php";
    String urlWebServicesProducao = "https://testeechopod.000webhostapp.com/getUsuarios.php";
    private static final String ARQ_userDATA = "userDATA";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button btn_entrarLogin;
    EditText editT_login;
    EditText editT_senha;

    String cript = "2019293";

    ProgressBar loading;

    String apelidoUser;
    String tipoUser_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        requestQueue = Volley.newRequestQueue(this);

        btn_entrarLogin = findViewById(R.id.btn_entrarLog);
        editT_login = findViewById(R.id.editT_login);
        editT_senha = findViewById(R.id.editT_senha);

        loading = findViewById(R.id.loading);
    }

    public void validarLogin(View view) {

        // VERIFICA SE OS CAMPOS ESTÃO PREENCHIDOS

        boolean preenchido = true;

        if (editT_login.getText().length() == 0){
            editT_login.setError("Campo obrigatório");
            editT_login.requestFocus();
            preenchido = false;

        }

        if (editT_senha.getText().length() == 0){
            editT_senha.setError("Campo obrigatório");
            editT_senha.requestFocus();
            preenchido = false;

        }

        // ENVIA OS DADOS DE LOGIN PARA O PHP E RETORNA A EXISTÊNCIA DO USUÁRIO

        if (preenchido) {

            loading.setVisibility(View.VISIBLE);
            btn_entrarLogin.setVisibility(View.GONE);

            stringRequest = new StringRequest(Request.Method.POST,
                    "https://testeechopod.000webhostapp.com/getUsuarios.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("LogLogin", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                boolean isErro = jsonObject.getBoolean("erro");

                                if (isErro) {

                                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"),
                                            Toast.LENGTH_LONG).show();
                                    loading.setVisibility(View.GONE);
                                    btn_entrarLogin.setVisibility(View.VISIBLE);

                                } else {

                                    // ARMAZENA DADOS PARA MANTER USUÁRIO CONECTADO

                                    apelidoUser = jsonObject.getString("apelido");
                                    tipoUser_id = jsonObject.getString("tipoUser_id");

                                    SharedPreferences preferences = getSharedPreferences(ARQ_userDATA,0);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    String user = editT_login.getText().toString();
                                    String pass = editT_senha.getText().toString();

                                    editor.putString("userlogado", user);
                                    editor.putString("passLogado", pass);
                                    editor.putString("id_user", tipoUser_id);
                                    editor.commit();

                                    abrirTela();


                                }
                            } catch (Exception e) {

                                Log.v("LogLogin", e.getMessage());

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LogLogin", error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("login", editT_login.getText().toString());
                    params.put("senha", editT_senha.getText().toString());
                    params.put("cript", cript);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }


    }

    public void abrirTelaCadastro(View view){

        Intent intent = new Intent(this, cadastro_comum.class);
        startActivity(intent);

    }

    public void abrirTela(){

        int user;
        user = Integer.parseInt(tipoUser_id);

        if (user == 1) {

            Intent intent1 = new Intent(this, tela_home_comum.class);
            intent1.putExtra("apelidoUser", apelidoUser);
            startActivity(intent1);
        }

        if (user == 2) {

            Intent intent1 = new Intent(this, tela_homeAdmin.class);
            intent1.putExtra("apelidoUser", apelidoUser);
            startActivity(intent1);
        }
        if (user == 3) {

            Intent intent1 = new Intent(this, tela_home_monitor.class);
            intent1.putExtra("apelidoUser", apelidoUser);
            startActivity(intent1);
        }



    }
}