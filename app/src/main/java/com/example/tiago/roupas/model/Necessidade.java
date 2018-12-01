package com.example.tiago.roupas.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Necessidade {

    public String titulo;
    public String descricao;
    public String justificativa;
    public String createdAt;

    public Necessidade()
    {

    }

    public Necessidade(String titulo, String descricao, String justificativa) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.justificativa = justificativa;

        this.createdAt = "";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getCreatedAt() {

        if( this.createdAt.isEmpty() ){
            Calendar calendar = Calendar.getInstance();
            //SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //SimpleDateFormat mdformat = new SimpleDateFormat("ddMMyyyyHHmmss");
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return mdformat.format(calendar.getTime());
        }

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("titulo", this.getTitulo());
        result.put("descricao", this.getDescricao());
        result.put("justificatica", this.getJustificativa());
        result.put("createdAt", this.getCreatedAt());

        return result;
    }
}
