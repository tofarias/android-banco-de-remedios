package com.br.tiago.roupas.IBGEApi;

import java.io.Serializable;

public class Municipio implements Serializable {

    public String id;
    public String nome;

    public Municipio(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}