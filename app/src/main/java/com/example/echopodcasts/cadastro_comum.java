package com.example.echopodcasts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class cadastro_comum extends AppCompatActivity {

    Button btn_proxCad;
    EditText editT_nome;
    EditText editT_email;
    EditText editT_senha;
    EditText editT_apelido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_comum);
        btn_proxCad = findViewById(R.id.btn_entrarLog);
        editT_nome = findViewById(R.id.editT_nome);
        editT_email = findViewById(R.id.editT_email);
        editT_senha = findViewById(R.id.editT_senha);
        editT_apelido = findViewById(R.id.ediT_apelido);

        btn_proxCad.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // VERIFICAR SE OS CAMPOS ESTÃO PREENCHIDOS

                boolean preenchido = true;

                if (editT_nome.getText().length() == 0){
                    editT_nome.setError("Campo obrigatório");
                    editT_nome.requestFocus();
                    preenchido = false;
                }

                if (editT_senha.getText().length() == 0){
                    editT_senha.setError("Campo obrigatório");
                    editT_senha.requestFocus();
                    preenchido = false;
                }

                if (editT_email.getText().length() == 0){
                    editT_email.setError("Campo obrigatório");
                    editT_email.requestFocus();
                    preenchido = false;
                }

                if (editT_apelido.getText().length() == 0){
                    editT_apelido.setError("Campo obrigatório");
                    editT_apelido.requestFocus();
                    preenchido = false;
                }

                if (preenchido){

                    abrirCadComum2();

                }
            }
        });


    }

    public void abrirCadComum2(){

        // ABRIR PRÓXIMA TELA DE CADASTRO COM AS INFORMAÇÃO OBTIDAS

        Intent intent = new Intent(this, cadastro_comum2.class);
        intent.putExtra("nome", editT_nome.getText().toString());
        intent.putExtra("senha", editT_senha.getText().toString());
        intent.putExtra("email", editT_email.getText().toString());
        intent.putExtra("apelido", editT_apelido.getText().toString());
        startActivity(intent);

    }

    public void abrirTelaLog(View view){

        // ABRIR TELA DE LOGIN

        Intent intent = new Intent(this, tela_login.class);
        startActivity(intent);

    }


}