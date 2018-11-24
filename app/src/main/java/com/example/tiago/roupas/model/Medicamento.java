package com.example.tiago.roupas.model;

import java.util.HashMap;
import java.util.Map;

public class Medicamento {

    private String nome;
    private String laboratorio;
    private String principioAtivo;

    public Medicamento(){ }

    public Medicamento(String nome, String laboratorio, String principioAtivo) {
        this.nome = nome;
        this.laboratorio = laboratorio;
        this.principioAtivo = principioAtivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("nome", this.getNome());
        result.put("laboratorio", this.getLaboratorio());
        result.put("principioAtivo", this.getPrincipioAtivo());

        return result;
    }
}
