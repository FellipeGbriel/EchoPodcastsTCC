package com.example.echopodcasts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.echopodcasts.Model.ReceberArquivo;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class tela_player extends AppCompatActivity {

    ListView listaPodcasts;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    public ImageView thumb;
    private List<ReceberArquivo> dowloads;

    String urlDownload;
    String tituloDownload;

    JcPlayerView jcPlayerView;
    DownloadManager manager;

    String FILTER_cat;

    private long downloadID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_player);

        Intent intent = getIntent();
        FILTER_cat = intent.getStringExtra("filter");


        jcPlayerView = findViewById(R.id.jcplayer);
        ArrayList<JcAudio> jcAudios = new ArrayList<>();

        listaPodcasts = findViewById(R.id.listViewPodcasts);
        ArrayList<String> arrayListTitulos = new ArrayList<>();
        ArrayList<String> arrayListAutor = new ArrayList<>();
        ArrayList<String> arrayCategoria = new ArrayList<>();
        ArrayList<String> arrayListUrl = new ArrayList<>();
        ArrayList<String> arrayListDuracao = new ArrayList<>();

        // ADAPTADOR PARA O TRATAMENTO DOS DADOS PARA A EXIBIÇÃO NO LISTVIEW
        MyAdapter adapter = new MyAdapter(this, arrayListTitulos, arrayListAutor,
                arrayCategoria,arrayListUrl, arrayListDuracao);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor aguarde ...");
        progressDialog.show();

        // FILTRO BASEADO NA CATEGORIA ESCOLHIDA
        Query query = FirebaseDatabase.getInstance().getReference("audios")
                .orderByChild("audiosCategory")
                .equalTo(FILTER_cat);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                        // RECEBE OS DADOS DO BANCO DE DADOS FIREBASE
                    ReceberArquivo receberArquivo = ds.getValue(ReceberArquivo.class);
                        arrayListTitulos.add(receberArquivo.getTituloAudio());
                        arrayListAutor.add(receberArquivo.getAutor());
                        arrayCategoria.add(receberArquivo.getAudiosCategory());
                        arrayListUrl.add(receberArquivo.getLinkAudio());
                        arrayListDuracao.add(receberArquivo.getDuracaoAudio());

                        jcAudios.add(JcAudio.createFromURL(receberArquivo.getTituloAudio(),
                                receberArquivo.getLinkAudio()));
               }
                // INICIA O PLAYER COM OS DADOS DO ADAPTADOR
                jcPlayerView.initPlaylist(jcAudios,null);
                listaPodcasts.setAdapter(adapter);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listaPodcasts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ABRE O PLAYER COM O PODCAST SELECIONADO
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification(R.drawable.ic_logo);
                urlDownload = arrayListUrl.get(position);
                tituloDownload = arrayListTitulos.get(position);

            }
        });

        listaPodcasts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                // AÇÕES DO LONG CLICK AQUI
                urlDownload = arrayListUrl.get(position);
                tituloDownload = arrayListTitulos.get(position);
                registerForContextMenu(view);

                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_podcast, menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        // DONWLOAD AQUI

        Uri uri = Uri.parse(urlDownload);
        DownloadManager.Request request = null;
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        File file = new File(getExternalFilesDir(null), "Músicas");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(uri)
                    .setTitle(tituloDownload)
                    .setDescription("Baixando")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        }
        else
        {
            request=new DownloadManager.Request(uri)
                    .setTitle(tituloDownload)
                .setDescription("Baixando")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverRoaming(true);
        }

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);


        return true; // não pode tirar

    }

    private BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                Toast.makeText(tela_player.this, "Download completo", Toast.LENGTH_LONG).show();

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;

        ArrayList<String> rTitulo = new ArrayList<>();
        ArrayList<String> rAutor = new ArrayList<>();
        ArrayList<String> rCategoria = new ArrayList<>();
        ArrayList<String> rUrl = new ArrayList<>();
        ArrayList<String> rDuracao = new ArrayList<>();



        MyAdapter (Context c, ArrayList<String> titulo, ArrayList<String> autor, ArrayList<String> categoria, ArrayList<String> url, ArrayList<String> duracao){
            super(c, R.layout.row_listview, R.id.tituloRow, titulo);

            this.context = c;
            this.rTitulo = titulo;
            this.rAutor = autor;
            this.rCategoria = categoria;
            this.rUrl = url;
            this.rDuracao = duracao;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview, parent,false);
            ImageView IVthumb = row.findViewById(R.id.imageViewRow);
            TextView TXtitulo = row.findViewById(R.id.tituloRow);
            TextView TXautor = row.findViewById(R.id.autorRow);
            TextView TXduracao = row.findViewById(R.id.tempoRow);

            // SETAR AQUI
            TXtitulo.setText(rTitulo.get(position));
            TXautor.setText("Por: " + rAutor.get(position));

            String cat = rCategoria.get(position);

            switch (cat) {
                case "Português":
                    IVthumb.setImageResource(R.drawable.img_portugues);
                    break;
                case "História":
                    IVthumb.setImageResource(R.drawable.img_historia);
                    break;
                case "Biologia":
                    IVthumb.setImageResource(R.drawable.img_biologia);
                    break;
                case "Química":
                    IVthumb.setImageResource(R.drawable.img_quimica);
                    break;
                case "Matemática":
                    IVthumb.setImageResource(R.drawable.img_matematica);
                    break;
                case "Geografia":
                    IVthumb.setImageResource(R.drawable.img_geografia);
                    break;
                case "Física":
                    IVthumb.setImageResource(R.drawable.img_fisica);
                    break;
                case "Filosofia":
                    IVthumb.setImageResource(R.drawable.img_filosofia);
                    break;
                case "Espanhol":
                    IVthumb.setImageResource(R.drawable.img_espanhol);
                    break;
                case "Inglês":
                    IVthumb.setImageResource(R.drawable.img_ingles);
                    break;
            }

            Integer duracaoInt = Integer.valueOf(rDuracao.get(position));

            long milis = Long.parseLong(String.valueOf(duracaoInt));
            long minutos = (milis / 1000) / 60;
            int segundos = (int)((milis / 1000) % 60);

            if (segundos < 10) {

                TXduracao.setText(minutos + ":0" + segundos);

            }
            else {
                TXduracao.setText(minutos + ":" + segundos);
            }
            return row;
        }
    }
}