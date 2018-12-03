package com.example.tiago.roupas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Donativo implements Serializable {

    public String tipo;
    public String descricao;
    public String justificativa;
    public String createdAt;
    public String quantidade;

    public Donativo()
    {

    }

    public Donativo(String tipo, String descricao, String justificativa, String quantidade) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.justificativa = justificativa;
        this.quantidade = quantidade;

        this.createdAt = "";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getQuantidade() {
        return this.quantidade;
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

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
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

        result.put("tipo", this.getTipo());
        result.put("descricao", this.getDescricao());
        result.put("justificatica", this.getJustificativa());
        result.put("quantidade", this.getQuantidade());
        result.put("createdAt", this.getCreatedAt());

        return result;
    }
}