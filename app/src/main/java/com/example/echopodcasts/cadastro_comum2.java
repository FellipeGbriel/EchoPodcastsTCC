package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class cadastro_comum2 extends AppCompatActivity {

    String urlWebServicesDesenvolvimento = "http://192.168.15.149/echopod/getUsuarios.php";
    String urlWebServicesProducao = "https://testeechopod.000webhostapp.com/getUsuarios.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String finalcript = "2019293";

    RadioButton rBtn_fund1;
    RadioButton rBtn_fund2;
    RadioButton rBtn_medio;
    RadioButton rBtn_superior;

    Button btn_entrarLog;

    String nome;
    String senha;
    String email;
    String apelido;

    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_comum2);

        requestQueue = Volley.newRequestQueue(this);

        rBtn_fund1 = findViewById(R.id.rBtn_fund1);
        rBtn_fund2 = findViewById(R.id.rBtn_fund2);
        rBtn_medio = findViewById(R.id.rBtn_medio);
        rBtn_superior = findViewById(R.id.rBtn_superior);

        nome = getIntent().getStringExtra("nome");
        senha = getIntent().getStringExtra("senha");
        email = getIntent().getStringExtra("email");
        apelido = getIntent().getStringExtra("apelido");

        btn_entrarLog = findViewById(R.id.btn_entrarLog);

        loading = findViewById(R.id.loading);



    }

    public void cadastrar(View view){

        // VERIFICAR SE OS CAMPOS ESTÃO PREENCHIDOS

        final String finalNome = nome;
        final String finalApelido= apelido;
        final String finalEmail = email;
        final String finalSenha = senha;

        boolean preenchido = false;
        int escolaridade_id = 0;

        if (rBtn_fund1.isChecked()) {

            preenchido = true;
            escolaridade_id = 1;

        } else if (rBtn_fund2.isChecked()) {

            preenchido = true;
            escolaridade_id = 2;

        }
        else if (rBtn_medio.isChecked()){

            preenchido = true;
            escolaridade_id = 3;

        }else if (rBtn_superior.isChecked()){

            preenchido = true;
            escolaridade_id = 4;

        }else{

            rBtn_superior.setError("Campo obrigatório");
            rBtn_medio.setError("Campo obrigatório");
            rBtn_fund2.setError("Campo obrigatório");
            rBtn_fund1.setError("Campo obrigatório");
        }



        // SE TODOS OS CAMPOS FOREM PREENCHIDOS, PASSAR OS AS INFORMAÇÕES
        // PARA O ARQUIVO PHP FAZER A INSERÇÃO NO BANCO DE DADOS

        if (preenchido) {

            loading.setVisibility(View.VISIBLE);
            btn_entrarLog.setVisibility(View.GONE);


            int finalEscolaridade_id1 = escolaridade_id;
            stringRequest = new StringRequest(Request.Method.POST,
                    "https://testeechopod.000webhostapp.com/putUsuarios.php",
                    response -> {
                        Log.v("LogLogin", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean isErro = jsonObject.getBoolean("erro");

                            if (isErro) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"),
                                        Toast.LENGTH_LONG).show();

                                loading.setVisibility(View.GONE);
                                btn_entrarLog.setVisibility(View.VISIBLE);
                            } else {
                                abrirTelaLogin();
                            }
                        } catch (Exception e) {

                            Log.v("LogLogin", e.getMessage());

                        }
                    },
                    error -> Log.e("LogLogin", error.getMessage())) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nome", finalNome);
                    params.put("senha", finalSenha);
                    params.put("email", finalEmail);
                    params.put("apelido", finalApelido);
                    params.put("tipoUser_id", "1");
                    params.put("escolaridade_id", String.valueOf(finalEscolaridade_id1));
                    params.put("cript", finalcript);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

        }
    }

    public void abrirTelaLogin(){

        Intent intent1 = new Intent(this, tela_login.class);
        startActivity(intent1);

    }

    public void abrirTelaLoginAlt(View view){

        Intent intent = new Intent(this, tela_login.class);
        startActivity(intent);

    }


}