package com.example.tiago.roupas.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Necessidade {

    private String titulo;
    private String descricao;
    private String justificativa;
    private String created_at;

    public Necessidade()
    {
        this.created_at = "";
    }

    public Necessidade(String titulo, String descricao, String justificativa) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.justificativa = justificativa;

        this.created_at = "";
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

        if( this.created_at.isEmpty() ){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            return mdformat.format(calendar.getTime());
        }

        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("titulo", this.getTitulo());
        result.put("descricao", this.getDescricao());
        result.put("justificatica", this.getJustificativa());

        result.put("created_at", this.getCreatedAt());

        return result;
    }
}
