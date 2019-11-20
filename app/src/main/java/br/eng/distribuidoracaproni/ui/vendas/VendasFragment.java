package br.eng.distribuidoracaproni.ui.vendas;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.eng.distribuidoracaproni.MainActivity;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Estoque;
import br.eng.distribuidoracaproni.objetos.Vendas;
import br.eng.distribuidoracaproni.ui.home.HomeFragment;
import br.eng.distribuidoracaproni.ui.listarestoque.ListarEstoqueFragment;

public class VendasFragment extends Fragment implements VendasListener {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy  HH:mm");

    SimpleDateFormat dateFormatR = new SimpleDateFormat("dd_MM_yy-HH:mm:ss");

    Date data = new Date();

    Calendar cal = Calendar.getInstance();
    Date data_atual = cal.getTime();

    String data_completa = dateFormat.format(data_atual);

    String data_completaR = dateFormatR.format(data_atual);


    private static VendasAdapter adapter;
    //ArrayAdapter<Estoque> adapter;
    private static Context context;
    private static ArrayList<Estoque> arrayEstoqueAux = new ArrayList<>();;
    private static ArrayList<Estoque> arrayEstoque = new ArrayList<>();
    private static ListView listaVendas;

    ListarEstoqueFragment listarEstoqueFragment = new ListarEstoqueFragment();

    Estoque estoque = new Estoque();
    Vendas vendas = new Vendas();

    public SearchView filter;

    Fragment fragment;

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference estoqueReference = databaseReference.child("Estoque");
    private DatabaseReference vendaReference = databaseReference.child("Registro_Vendas");

    private VendasViewModel mViewModel;

    public static VendasFragment newInstance() {
        return new VendasFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendas_fragment, container, false);

        context = view.getContext();
        listaVendas = view.findViewById(R.id.listaAddVendas);
        filter = view.findViewById(R.id.searchAddVendas);

        listStart();
        listar();

        return  view;
    }

    public void listStart(){
        adapter = new VendasAdapter(context ,arrayEstoqueAux,this);
        listaVendas.setAdapter(adapter);
    }

    public void listar(){
        estoqueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayEstoqueAux.clear();
                    arrayEstoque.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Estoque estoque = objSnapshot.getValue(Estoque.class);
                        arrayEstoqueAux.add(estoque);
                        arrayEstoque.add(estoque);
                        adapter.notifyDataSetChanged();
                    }
                }

                filter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(filter.getWindowToken(), 0);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        s = s.toLowerCase();
                        arrayEstoqueAux.clear();
                        if(s.equals("")){
                            arrayEstoqueAux.addAll(arrayEstoque);
                        }else{
                            for (Estoque p :  arrayEstoque){
                                if(p.getInfoProduto().getNome().toString().toLowerCase().contains(s) || p.getInfoProduto().getTamanho().toString().toLowerCase().contains(s)|| p.getInfoProduto().getFabricante().toString().toLowerCase().contains(s)|| p.getInfoProduto().getCodigo().toString().toLowerCase().contains(s)|| p.getInfoProduto().getPreco().toString().toLowerCase().contains(s) ){
                                    //if(p.toString().toLowerCase().contains(s)){
                                    arrayEstoqueAux.add(p);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onSessionClicked(final String nomeProd, final String tamanhoProd, String fabricanteProd, String precoProd, String urlProd, final int quantidade) {

        estoqueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Estoque estoque = objSnapshot.getValue(Estoque.class);
                        if ((estoque.getInfoProduto().getNome().equals(nomeProd) && estoque.getInfoProduto().getTamanho().equals(tamanhoProd)) ) {
                            int quanti = estoque.getQtd() - quantidade;
                            if(quanti == 0){
                                estoqueReference.getRef().child(objSnapshot.getKey()).removeValue();
                                listarEstoqueFragment.reload();

                            }else{
                                estoque.setInfoProduto(estoque.getInfoProduto());
                                estoque.setQtd(quanti);
                                estoque.setPrecoTotal(quanti * Float.parseFloat(estoque.getInfoProduto().getPreco()));
                                estoqueReference.getRef().child(objSnapshot.getKey()).setValue(estoque);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
            }
        });
        vendas.setNomePR(nomeProd);
        vendas.setTamanhoR(tamanhoProd);
        vendas.setFabricanteR(fabricanteProd + "   " + data_completa);
        vendas.setQuantidade(quantidade);
        vendas.setPrecoTotal(Float.parseFloat( precoProd)*quantidade);
        vendas.setUrlR(urlProd);

        vendaReference.child(data_completaR).setValue(vendas);
        Toast.makeText(getContext(), " Venda realizada com sucesso !", Toast.LENGTH_SHORT).show();

        fragment = new HomeFragment();
        Activity main = getActivity();
        String Tag = "HomeFragment";

        if (main != null) {
            ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
        }

    }
}
