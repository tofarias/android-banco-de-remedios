package com.br.tiago.roupas.model;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    public String nome;
    public String email;
    public String photo_url;
    public String id;

    public Usuario() {}

    public Usuario(String nome, String email, String photo_url, String id) {
        this.nome = nome;
        this.email = email;
        this.photo_url = photo_url;
        this.id = id;
    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("nome", nome);
        result.put("email", email);
        result.put("photo_url", photo_url);
        result.put("id", id);

        return result;
    }
}
