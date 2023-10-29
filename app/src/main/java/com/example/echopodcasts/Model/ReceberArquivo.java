package com.example.echopodcasts.Model;

public class ReceberArquivo {

    String tituloAudio;
    String linkAudio;
    String audiosCategory;
    String autor;
    String duracaoAudio;


    public ReceberArquivo(String tituloAudio, String linkAudio, String audiosCategory, String autor, String duracaoAudio) {
        this.tituloAudio = tituloAudio;
        this.linkAudio = linkAudio;
        this.audiosCategory = audiosCategory;
        this.autor = autor;
        this.duracaoAudio = duracaoAudio;

    }


    public String getTituloAudio() {
        return tituloAudio;
    }

    public void setTituloAudio(String tituloAudio) {
        this.tituloAudio = tituloAudio;
    }

    public String getLinkAudio() {
        return linkAudio;
    }

    public void setLinkAudio(String linkAudio) {
        this.linkAudio = linkAudio;
    }

    public String getAudiosCategory() {
        return audiosCategory;
    }

    public void setAudiosCategory(String audiosCategory) {
        this.audiosCategory = audiosCategory;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDuracaoAudio() {
        return duracaoAudio;
    }

    public void setDuracaoAudio(String duracaoAudio) {
        this.duracaoAudio = duracaoAudio;
    }

    public ReceberArquivo() {



    }
}
