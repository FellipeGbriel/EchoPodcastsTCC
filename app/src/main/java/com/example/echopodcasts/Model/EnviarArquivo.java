package com.example.echopodcasts.Model;

public class EnviarArquivo {

    public String audiosCategory, tituloAudio, autor, arteGrupo, duracaoAudio, linkAudio, mKey, grupo;

    public String visibilidade = "I";

    public EnviarArquivo(String audiosCategory, String tituloAudio, String autor, String arteGrupo, String duracaoAudio, String grupo, String linkAudio) {

        if (tituloAudio.trim().equals("")){

            tituloAudio = "Sem título";

        }

        this.audiosCategory = audiosCategory;
        this.tituloAudio = tituloAudio;
        this.autor = autor;
        this.arteGrupo = arteGrupo;
        this.duracaoAudio = duracaoAudio;
        this.linkAudio = linkAudio;
        this.grupo = grupo;

    }

    public EnviarArquivo() {

    }

    public String getAudiosCategory() {
        return audiosCategory;
    }

    public void setAudiosCategory(String audiosCategory) {
        this.audiosCategory = audiosCategory;
    }

    public String getTituloAudio() {
        return tituloAudio;
    }

    public void setTituloAudio(String tituloAudio) {
        this.tituloAudio = tituloAudio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getArteGrupo() {
        return arteGrupo;
    }

    public void setArteGrupo(String arteGrupo) {
        this.arteGrupo = arteGrupo;
    }

    public String getDuracaoAudio() {
        return duracaoAudio;
    }

    public void setDuracaoAudio(String duracaoAudio) {
        this.duracaoAudio = duracaoAudio;
    }

    public String getLinkAudio() {
        return linkAudio;
    }

    public void setLinkAudio(String linkAudio) {
        this.linkAudio = linkAudio;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getGrupo() {
        return grupo;
    }

    public void getGrupo(String grupo) {
        this.grupo = grupo;
    }
}
