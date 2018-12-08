package com.example.tiago.roupas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Necessidade implements Serializable {

    public String id;
    public String tipo;
    public String descricao;
    public String justificativa;
    public String createdAt;

    public Necessidade()
    {

    }

    public Necessidade(String tipo, String descricao, String justificativa) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.justificativa = justificativa;
        this.createdAt = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        result.put("id", this.getId());
        result.put("tipo", this.getTipo());
        result.put("descricao", this.getDescricao());
        result.put("justificatica", this.getJustificativa());
        result.put("createdAt", this.getCreatedAt());

        return result;
    }
}
