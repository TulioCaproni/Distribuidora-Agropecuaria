package br.eng.distribuidoracaproni.ui.listarvendas;

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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Vendas;

public class ListarVendasFragment extends Fragment {

    private static VendasListAdapter adapter;

    private static Context context;
    private static ArrayList<Vendas> arrayVendasAux = new ArrayList<>();;
    private static ArrayList<Vendas> arrayVendas = new ArrayList<>();
    private static ListView listaVendas;
    Vendas vendas = new Vendas();

    public SearchView filter;

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference vendaReference = databaseReference.child("Registro_Vendas");

    private ListarVendasViewModel mViewModel;

    public static ListarVendasFragment newInstance() {
        return new ListarVendasFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listar_vendas_fragment, container, false);

        context = view.getContext();
        listaVendas = view.findViewById(R.id.listaListVendas);
        filter = view.findViewById(R.id.searchListVendas);

        listStart();
        listar();

        adapter.notifyDataSetChanged();

        return view;
    }

    public void listStart(){
        adapter = new VendasListAdapter(context ,arrayVendasAux);
        listaVendas.setAdapter(adapter);
    }

    public void listar(){
        vendaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayVendasAux.clear();
                    arrayVendas.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Vendas vendas = objSnapshot.getValue(Vendas.class);
                        arrayVendasAux.add(vendas);
                        arrayVendas.add(vendas);
                        adapter.notifyDataSetChanged();
                    }
                }

                listaVendas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                        view.setSelected(true);
                        view.setBackgroundResource(0);
                        Vendas vendas = (Vendas) listaVendas.getAdapter().getItem(position);
                        Toast.makeText(context, vendas.getNomePR().toString() , Toast.LENGTH_SHORT).show();

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
                        arrayVendasAux.clear();
                        if(s.equals("")){
                            arrayVendasAux.addAll(arrayVendas);
                        }else{
                            for (Vendas v :  arrayVendas){
                                if( v.getNomePR().toString().toLowerCase().contains(s) || v.getTamanhoR().toString().toLowerCase().contains(s)|| v.getFabricanteR().toString().toLowerCase().contains(s)){
                                    arrayVendasAux.add(v);
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
}
