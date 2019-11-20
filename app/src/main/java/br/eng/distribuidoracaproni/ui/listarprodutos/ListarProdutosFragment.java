package br.eng.distribuidoracaproni.ui.listarprodutos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import br.eng.distribuidoracaproni.MainActivity;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Produtos;
import br.eng.distribuidoracaproni.ui.cadastrarprodutos.CadastrarProdutosFragment;

public class ListarProdutosFragment extends Fragment implements Serializable, ProdutosListener, View.OnClickListener {

    private static ProdutosAdapter adapter;
    private static Context context;
    private static ArrayList<Produtos> arrayProdutosAux = new ArrayList<>();;
    private static ListView listaDeProdutos;

    public SearchView filter;

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference produtosReference = databaseReference.child("Produtos");

    Produtos produtos = new Produtos();

    ArrayList<Produtos> arrayProdutos = new ArrayList<>();

    Fragment fragment;
    Activity main;

    String Tag;

    //ArrayAdapter<Produtos> adapter;

    ImageView imagem;
    Button btnAddProduto;

    public static ListarProdutosFragment newInstance() {
        return new ListarProdutosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listar_produtos_fragment, container, false);

        context = view.getContext();
        listaDeProdutos = view.findViewById(R.id.lista);
        filter = view.findViewById(R.id.search);
        imagem = view.findViewById(R.id.ivPhoto);
        btnAddProduto = view.findViewById(R.id.btnAddProduto);

        fragment = new CadastrarProdutosFragment();
        main = getActivity();

        listStart();
        listar();

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        btnAddProduto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddProduto:
                if (main != null) {
                    Tag = "CadastrarProdutosFragment";
                    ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    public void listStart(){
        adapter = new ProdutosAdapter(context ,arrayProdutosAux,this);
        listaDeProdutos.setAdapter(adapter);
    }

    public void reload(){
        arrayProdutosAux.clear();
        listStart();
    }

    public void listar(){
        produtosReference.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayProdutosAux.clear();
                    arrayProdutos.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Produtos produtos = objSnapshot.getValue(Produtos.class);
                        arrayProdutosAux.add(produtos);
                        arrayProdutos.add(produtos);
                        adapter.notifyDataSetChanged();
                    }
                }

                //adapter = new ArrayAdapter<Produtos>(context, android.R.layout.simple_list_item_1, arrayProdutos);
                //listaDeProdutos.setAdapter(adapter);
                //listaDeProdutos.setAdapter(new ProdutosAdapter(context,arrayProdutos));
                //ProdutosAdapter produtosAdapter = new ProdutosAdapter(context ,arrayProdutos);
                //listaDeProdutos.setAdapter(produtosAdapter);

                //RvAdapter recyclerViewAdapter = new RvAdapter(context, arrayProdutos);
                //listaDeProdutos.setAdapter((ListAdapter) recyclerViewAdapter);

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

                /*filter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        (ListarProdutosFragment.this).adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });*/

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
                        arrayProdutosAux.clear();
                        if(s.equals("")){
                            arrayProdutosAux.addAll(arrayProdutos);
                        }else{
                            for (Produtos p :  arrayProdutos){
                                if(p.getNome().toString().toLowerCase().contains(s) || p.getTamanho().toString().toLowerCase().contains(s)|| p.getFabricante().toString().toLowerCase().contains(s)|| p.getCodigo().toString().toLowerCase().contains(s)|| p.getPreco().toString().toLowerCase().contains(s) ){
                                //if(p.toString().toLowerCase().contains(s)){
                                    arrayProdutosAux.add(p);
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
    public void onSessionClicked(long sessionId , final String nom, final String tam) {

        if (sessionId==1){
            Produtos.sel_prod=1;
            Produtos.nomeProdudutoSel = nom;
            Produtos.tamanhoProdudutoSel = tam;
            if (main != null) {
                Tag = "CadastrarProdutosFragment";
                ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
            }
        }else if(sessionId==2){
            produtosReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Produtos produtos = objSnapshot.getValue(Produtos.class);
                            if(produtos.getNome().equals(nom) && produtos.getTamanho().equals(tam)){
                                produtosReference.child(objSnapshot.getRef().getKey()).removeValue();
                                Toast.makeText(getContext(), "Produto excluido com sucesso!!!!", Toast.LENGTH_SHORT).show();
                                reload();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Não foi possível encontrar as informações!", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}
