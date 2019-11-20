package br.eng.distribuidoracaproni;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import br.eng.distribuidoracaproni.ui.vendas.VendasFragment;

public class Vendas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, VendasFragment.newInstance())
                    .commitNow();
        }
    }
}
