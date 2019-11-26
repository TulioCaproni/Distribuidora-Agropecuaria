package br.eng.distribuidoracaproni.Informacaodialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.eng.distribuidoracaproni.R;

public class InformacaoDialog extends Dialog {

    private EditText editTextNome;
    private int quantidade;

    // Teste
    private InformacaoListener listener;
    String nomeProd, tamanhoProd;

    public InformacaoDialog(Context context, InformacaoListener listener) {
        super(context);
        this.listener= (InformacaoListener) listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_informacao);
        editTextNome = findViewById(R.id.editTextNome);

        Button buttonAdicionar = findViewById(R.id.buttonAdicionar);
        Button buttonCancelar = findViewById(R.id.buttonCancelar);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // btn adicionar
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    quantidade = Integer.parseInt( editTextNome.getText().toString() );
                    listener.onSessionClickedAlert(quantidade);
                }catch (Exception e){
                    Toast.makeText(getContext(), "Entre com um número valido!!!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}