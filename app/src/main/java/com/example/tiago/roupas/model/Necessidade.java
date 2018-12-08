package com.example.tiago.roupas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Necessidade implements Serializable {

    public String tipo;
    public String descricao;
    public String justificativa;
    public String createdAt;
    public String userId;

    public Necessidade()
    {

    }

    public Necessidade(String tipo, String descricao, String justificativa, String userId) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.justificativa = justificativa;
        this.userId = userId;
        this.createdAt = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

        result.put("tipo", this.getTipo());
        result.put("descricao", this.getDescricao());
        result.put("justificatica", this.getJustificativa());
        result.put("createdAt", this.getCreatedAt());
        result.put("userId", this.getUserId());

        return result;
    }
}
