package com.example.joao.ecowalk.classe;

/**
 * Created by joao on 25/08/16.
 */
public class Artefato
{
    String informacoes_ecologicas, descricao, nome, detalhes;
    Double latidude, longitude;

    /*GETTERS*/
    public String Informacoes_ecologicas(){
        return informacoes_ecologicas;
    }

    public String getDescricao(){
        return  descricao;
    }

    public String getNome(){
        return  nome;
    }

    public String getDetalhes(){
        return  detalhes;
    }

    public Double getLatidude(){
        return  latidude;
    }

    public Double getLongitude(){
        return  longitude;
    }

    /*SETTERS*/
    public void setInformacoes_ecologicas(String informacoes_ecologicas) {
        this.informacoes_ecologicas = informacoes_ecologicas;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public void setLatidude(Double latidude) {
        this.latidude = latidude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
