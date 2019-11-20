package br.eng.distribuidoracaproni.objetos;

import java.io.Serializable;

public class Estoque implements Serializable {

    private float precoTotal;
    private int qtd;
    Produtos infoProduto;


    public Estoque() {
        this.qtd = qtd;
        this.precoTotal = precoTotal;
        this.infoProduto = infoProduto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Produtos getInfoProduto() {
        return infoProduto;
    }

    public void setInfoProduto(Produtos infoProduto) {
        this.infoProduto = infoProduto;
    }
}
