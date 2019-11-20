package br.eng.distribuidoracaproni.objetos;

import java.io.Serializable;

public class Vendas implements Serializable {

    private int quantidade;
    private float precoTotal;
    private  String nomePR,tamanhoR,fabricanteR,nomeCR,urlR;

    public Vendas() {
        this.quantidade = quantidade;
        this.precoTotal = precoTotal;
        this.nomePR = nomePR;
        this.tamanhoR = tamanhoR;
        this.fabricanteR = fabricanteR;
        this.nomeCR = nomeCR;
        this.urlR = urlR;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getNomePR() {
        return nomePR;
    }

    public void setNomePR(String nomePR) {
        this.nomePR = nomePR;
    }

    public String getTamanhoR() {
        return tamanhoR;
    }

    public void setTamanhoR(String tamanhoR) {
        this.tamanhoR = tamanhoR;
    }

    public String getFabricanteR() {
        return fabricanteR;
    }

    public void setFabricanteR(String fabricanteR) {
        this.fabricanteR = fabricanteR;
    }

    public String getNomeCR() {
        return nomeCR;
    }

    public void setNomeCR(String nomeCR) {
        this.nomeCR = nomeCR;
    }

    public String getUrlR() {
        return urlR;
    }

    public void setUrlR(String urlR) {
        this.urlR = urlR;
    }
}
