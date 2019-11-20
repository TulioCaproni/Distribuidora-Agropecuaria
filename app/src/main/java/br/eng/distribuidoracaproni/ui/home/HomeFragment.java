package br.eng.distribuidoracaproni.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import br.eng.distribuidoracaproni.MainActivity;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.ui.listarestoque.ListarEstoqueFragment;
import br.eng.distribuidoracaproni.ui.listarvendas.ListarVendasFragment;
import br.eng.distribuidoracaproni.ui.vendas.VendasFragment;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel mViewModel;
    private ImageButton btnEstoque,btnRegisto,btnVenda;
    Fragment fragment = null;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_fragment, container, false);

       btnEstoque = view.findViewById(R.id.estoque);
       btnRegisto = view.findViewById(R.id.registros);
       btnVenda = view.findViewById(R.id.vender);

       return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();

        btnRegisto.setOnClickListener(this);
        btnEstoque.setOnClickListener(this);
        btnVenda.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String Tag;
        switch (v.getId()) {
            case R.id.estoque:
                fragment = new ListarEstoqueFragment();
                Tag = "ListarEstoqueFragment";
                callActivity(fragment, Tag);
                break;
            case R.id.vender:
                fragment = new VendasFragment();
                Tag = "VendasFragment";
                callActivity(fragment, Tag);
                break;
            case R.id.registros:
                fragment = new ListarVendasFragment();
                Tag = "ListarVendasFragment";
                callActivity(fragment, Tag);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void callActivity(Fragment fragment, String Tag) {
        Activity main = getActivity();

        if (main != null) {
            ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
        }
    }
}
