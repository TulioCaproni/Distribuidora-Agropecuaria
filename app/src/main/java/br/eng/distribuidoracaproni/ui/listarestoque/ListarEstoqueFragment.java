package br.eng.distribuidoracaproni.ui.listarestoque;

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

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Estoque;

public class ListarEstoqueFragment extends Fragment implements Serializable, View.OnClickListener {
    private static EstoqueAdapter adapter;
    //ArrayAdapter<Estoque> adapter;
    private static Context context;
    private static ArrayList<Estoque> arrayEstoqueAux = new ArrayList<>();;
    private static ArrayList<Estoque> arrayEstoque = new ArrayList<>();
    private static ListView listaestoque;
    Estoque estoque = new Estoque();

    public SearchView filter;

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference estoqueReference = databaseReference.child("Estoque");

    private ListarEstoqueViewModel mViewModel;

    public static ListarEstoqueFragment newInstance() {
        return new ListarEstoqueFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listar_estoque_fragment, container, false);
        context = view.getContext();
        listaestoque = view.findViewById(R.id.listaListEstoque);
        filter = view.findViewById(R.id.searchListEstoque);

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
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    public void listStart(){
        adapter = new EstoqueAdapter(context ,arrayEstoqueAux);
        listaestoque.setAdapter(adapter);
    }

    public void  reload (){
        arrayEstoqueAux.clear();
        listStart();
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
                    }
                    adapter.notifyDataSetChanged();
                }

                /*listaestoque.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                        view.setSelected(true);
                        view.setBackgroundResource(0);
                        Estoque estoque = (Estoque) listaestoque.getAdapter().getItem(position);
                        Toast.makeText(context, estoque.getInfoProduto().getNome().toString() + " sucesso !", Toast.LENGTH_SHORT).show();

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
