package com.example.tiago.bancoderemedios.IBGEApi;

import java.io.Serializable;

public class Estado  implements Serializable {

    public String id;
    public String nome;
    public String sigla;

    public Estado(String id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }
}