package br.eng.distribuidoracaproni.ui.listarvendas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Vendas;

class VendasListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Vendas> vendas;

    public VendasListAdapter(Context context, ArrayList<Vendas> vendas) {
        this.context = context;
        this.vendas = vendas;
    }


    @Override
    public int getCount() {
        return vendas.size();
    }

    @Override
    public Vendas getItem(int position) {
        return vendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VendasListAdapter.ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_list, parent, false);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new VendasListAdapter.ItemSuporte();

            itemHolder.ivPhoto = convertView.findViewById(R.id.ivPhoto);
            itemHolder.tvNome = convertView.findViewById(R.id.txtViewNome);
            itemHolder.tvTam = convertView.findViewById(R.id.txtViewTam);
            itemHolder.tvFabri = convertView.findViewById(R.id.txtViewFrabri);
            itemHolder.tvQtd = convertView.findViewById(R.id.txtViewQtd);
            itemHolder.tvPreTotal = convertView.findViewById(R.id.txtViewPrecoTotal);
            //define os itens na view;
            convertView.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (VendasListAdapter.ItemSuporte) convertView.getTag();
        }

        final Vendas prod = getItem(position);



        //ivPhoto.setImageDrawable();
        if(prod.getUrlR().contains("/")){
            Picasso.get().load(prod.getUrlR()).into(itemHolder.ivPhoto);
        }else {
            itemHolder.ivPhoto.setImageResource(R.drawable.ic_add_to_photos_black_80dp);
        }


        itemHolder.tvNome.setText(prod.getNomePR());
        itemHolder.tvTam.setText(prod.getTamanhoR());
        itemHolder.tvFabri.setText(prod.getFabricanteR());
        itemHolder.tvQtd.setText(prod.getQuantidade()+"");
        itemHolder.tvPreTotal.setText(prod.getPrecoTotal()+"");


        return convertView;
    }

    private class ItemSuporte {

        ImageView ivPhoto;
        TextView tvNome,tvTam,tvFabri,tvQtd ,tvPreTotal;
    }
}
