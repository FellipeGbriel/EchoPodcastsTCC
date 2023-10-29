package com.example.echopodcasts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import com.example.echopodcasts.Model.EnviarArquivo;

public class upload_arquivo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String ARQ_userDATA = "userDATA";
    TextView textViewImagem;
    ProgressBar progressBar;
    Uri audioUri;
    StorageReference mStorageref_audio, mStorageref_img;
    StorageTask mUploadsTask;
    DatabaseReference referenceAudios_audio, referenceAudios_img;
    String audiosCategory;
    MediaMetadataRetriever metadataRetriever;
    byte [] art;
    String titulo1, autor1, arteGrupo1 = "", duracao1, grupo1;
    EditText titulo;
    TextView duracao;

    String apelidoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_arquivo);

        apelidoUser = getIntent().getStringExtra("apelidoUser");

        textViewImagem = findViewById(R.id.text_arquivoSelecionado);
        progressBar = findViewById(R.id.progressbar);
        titulo = findViewById(R.id.text_titulo);
        duracao = findViewById(R.id.text_duracao);

        metadataRetriever = new MediaMetadataRetriever();


        referenceAudios_audio = FirebaseDatabase.getInstance().getReference().child("audios");
        mStorageref_audio = FirebaseStorage.getInstance().getReference().child("audios");


        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);


        List<String> categorias = new ArrayList<>();

        categorias.add("Português");
        categorias.add("História");
        categorias.add("Biologia");
        categorias.add("Química");
        categorias.add("Inglês");
        categorias.add("Matemática");
        categorias.add("Geografia");
        categorias.add("Física");
        categorias.add("Filosofia");
        categorias.add("Espanhol");

        ArrayAdapter <String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        audiosCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Selecionado: "+audiosCategory, Toast.LENGTH_SHORT).show();

        if (audiosCategory == "Português"){



        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void abrirArquivoDeAudio (View v){

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null){

            audioUri = data.getData();
            String nomeDoArquivo = getFileName(audioUri);
            textViewImagem.setText(nomeDoArquivo);
            metadataRetriever.setDataSource(this, audioUri);

            art = metadataRetriever.getEmbeddedPicture();
            if (art !=null){

                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);

            }else{



            }

            long milis = Long.parseLong(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            long minutos = (milis / 1000) / 60;
            int segundos = (int)((milis / 1000) % 60);

            if (segundos < 10) {

                duracao.setText("Duração: " + minutos + ":0" + segundos);

            }
            else {
                duracao.setText("Duração: " + minutos + ":" + segundos);
            }

            titulo.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

            autor1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            titulo1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            duracao1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri){

        String result = null;

        if (uri.getScheme().equals("content")){

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {

                if (cursor != null && cursor.moveToFirst()) {

                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }
            finally {
                cursor.close();
            }
        }

        if (result == null){

            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){

                    result = result.substring(cut +1);
            }

        }

        return result;
    }

    public void enviarArquivoParaFirebase(View v){

        if(textViewImagem.equals("Nenhum arquivo selecionado")){

            Toast.makeText(this,"Por favor selecione um arquivo",Toast.LENGTH_SHORT).show();
        }
        else{

            if (mUploadsTask != null && mUploadsTask.isInProgress()){

                Toast.makeText(this,"O envio de arquivo já está em progresso!",Toast.LENGTH_SHORT).show();

            } else{

                enviarArquivos();

            }

        }

    }

    private void enviarArquivos() {

        if (audioUri != null){

            Toast.makeText(this,"Enviando, por favor aguarde!",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference = mStorageref_audio.child(System.currentTimeMillis() + "." + getFileExtension(audioUri));
            mUploadsTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if (titulo.getText() != null) {

                                titulo1 = titulo.getText().toString();

                                SharedPreferences preferences = getSharedPreferences(ARQ_userDATA,0);
                                String userRegistrado = preferences.getString("userlogado", "Usuário Anônimo");



                                EnviarArquivo enviarArquivo = new EnviarArquivo(audiosCategory, titulo1, userRegistrado, arteGrupo1, duracao1, grupo1, uri.toString());
                                String uploadId = referenceAudios_audio.push().getKey();
                                referenceAudios_audio.child(uploadId).setValue(enviarArquivo);

                            } else {

                                Toast.makeText(upload_arquivo.this, "Digite um título para seu áudio", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progresso = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progresso);
                    if (progresso == 100.0) {
                        Toast.makeText(upload_arquivo.this, "Upload concluído", Toast.LENGTH_SHORT).show();
                        abrirTelaHome();

                    }

                }
            });

        } else{

            Toast.makeText(this, "Nenhum arquivo selecionado para enviar", Toast.LENGTH_SHORT).show();

        }

    }

    private String getFileExtension(Uri audioUri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));

    }

    public void abrirTelaHome(){

        Intent intent = new Intent(this, tela_home_comum.class);
        intent.putExtra("apelidoUser", apelidoUser);
        startActivity(intent);


    }

}