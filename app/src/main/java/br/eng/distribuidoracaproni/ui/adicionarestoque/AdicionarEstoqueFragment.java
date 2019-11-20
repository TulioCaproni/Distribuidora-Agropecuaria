package br.eng.distribuidoracaproni.ui.adicionarestoque;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import br.eng.distribuidoracaproni.MainActivity;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Estoque;
import br.eng.distribuidoracaproni.objetos.Produtos;
import br.eng.distribuidoracaproni.ui.home.HomeFragment;

public class AdicionarEstoqueFragment extends Fragment implements Serializable, AdicionarEstoqueListener {
    static int quanti;

    private static AdicionarEstoqueAdapter adapter;
    //ArrayAdapter<Produtos> adapter;
    private static Context context;
    private static ArrayList<Produtos> arrayProdAux = new ArrayList<>();;
    private static ListView listaDeProdutos;

    public SearchView filter;

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference produtosReference = databaseReference.child("Produtos");
    private DatabaseReference estoqueReference = databaseReference.child("Estoque");

    Produtos produtos = new Produtos();
    Estoque estoque = new Estoque();

    ArrayList<Produtos> arrayProd = new ArrayList<>();

    Fragment fragment;
    Activity main;

    private AdicionarEstoqueViewModel mViewModel;

    public static AdicionarEstoqueFragment newInstance() {
        return new AdicionarEstoqueFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adicionar_estoque_fragment, container, false);

        context = view.getContext();
        listaDeProdutos = view.findViewById(R.id.listaAddEstoque);
        filter = view.findViewById(R.id.searchAddEstoque);
        main = getActivity();

        listStart();
        listar();

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdicionarEstoqueViewModel.class);
        // TODO: Use the ViewModel
    }

    public void listStart(){
        adapter = new AdicionarEstoqueAdapter(context , arrayProdAux, this);
        //adapter = new ArrayAdapter<Produtos>(context, android.R.layout.simple_list_item_1, arrayProd);
        listaDeProdutos.setAdapter(adapter);
    }

    public void listar(){
        produtosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayProdAux.clear();
                    arrayProd.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Produtos produtos = objSnapshot.getValue(Produtos.class);
                        arrayProdAux.add(produtos);
                        arrayProd.add(produtos);
                        adapter.notifyDataSetChanged();
                    }
                }
                listaDeProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                        view.setSelected(true);
                        view.setBackgroundResource(0);
                        Produtos produtos = (Produtos) listaDeProdutos.getAdapter().getItem(position);
                        //Picasso.get().load(produtos.getUrl().toString()).into(imagem);
                        Toast.makeText(context, produtos.getNome().toString() + " sucesso !", Toast.LENGTH_SHORT).show();

                    }
                });

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
                        arrayProdAux.clear();
                        if(s.equals("")){
                            arrayProdAux.addAll(arrayProd);
                        }else{
                            for (Produtos p : arrayProd){
                                if(p.getNome().toString().toLowerCase().contains(s) || p.getTamanho().toString().toLowerCase().contains(s)|| p.getFabricante().toString().toLowerCase().contains(s)|| p.getCodigo().toString().toLowerCase().contains(s)|| p.getPreco().toString().toLowerCase().contains(s) ){
                                    //if(p.toString().toLowerCase().contains(s)){
                                    arrayProdAux.add(p);
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
    public void onSessionClicked(final String nomeProd, final String tamanhoProd, final int quantidades) {
        quanti=0;

           estoqueReference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                           Estoque estoque = objSnapshot.getValue(Estoque.class);
                           if (estoque.getInfoProduto().getNome().toString().equals(nomeProd) && estoque.getInfoProduto().getTamanho().toString().equals(tamanhoProd)) {
                               quanti =estoque.getQtd();
                           }
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   Toast.makeText(context, "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
               }
           });

           produtosReference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                           Produtos produtos = objSnapshot.getValue(Produtos.class);
                           if (produtos.getNome().equals(nomeProd) && produtos.getTamanho().equals(tamanhoProd)) {
                               int quantiFinal=quantidades+quanti;
                               estoque.setInfoProduto(produtos);
                               estoque.setQtd(quantiFinal);
                               estoque.setPrecoTotal(quantiFinal * Float.parseFloat(produtos.getPreco()));
                               estoqueReference.child(objSnapshot.getKey()).setValue(estoque);
                               Toast.makeText(context, produtos.getNome() + " adicionado ao estoque!", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   Toast.makeText(context, "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
               }
           });

        fragment = new HomeFragment();
        Activity main = getActivity();
        String Tag = "HomeFragment";

        if (main != null) {
            ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
        }
       }

}
