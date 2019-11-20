package br.eng.distribuidoracaproni.objetos;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Produtos implements Serializable {

    private String nome,fabricante,tamanho,preco,codigo,url=null;
    public  static int sel_prod=-1;
    public static String nomeProdudutoSel;
    public static String tamanhoProdudutoSel;
    private Drawable photo;

    public Produtos() {
    }


    public Produtos(String nome, String fabricante, String tamanho, String preco, String codigo, String url) {
        this.nome = nome;
        this.fabricante = fabricante;
        this.tamanho = tamanho;
        this.preco = preco;
        this.codigo = codigo;
        this.url = url;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    @Override
    public String toString() {
        return
                "Nome: " + nome +"   "+ tamanho + "\n"+
                "Fabricante: " + fabricante + "    COD.: " + codigo +"\n"+
                "Preço: R$ " + preco;
    }
}
