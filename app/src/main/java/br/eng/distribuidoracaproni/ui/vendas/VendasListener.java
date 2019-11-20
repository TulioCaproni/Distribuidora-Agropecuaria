package br.eng.distribuidoracaproni.ui.vendas;

public interface VendasListener {
    public void onSessionClicked(String nomeProd, String tamanhoProd, String fabricanteProd, String precoProd, String urlProd, int quantidade);
}
