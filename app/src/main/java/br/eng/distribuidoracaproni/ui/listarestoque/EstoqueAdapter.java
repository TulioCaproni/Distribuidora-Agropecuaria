package br.eng.distribuidoracaproni.ui.listarestoque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.eng.distribuidoracaproni.objetos.Estoque;
import br.eng.distribuidoracaproni.R;

public class EstoqueAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Estoque> estoque;

    public EstoqueAdapter(Context context, ArrayList<Estoque> est) {
        this.context = context;
        this.estoque = est;
    }


    @Override
    public int getCount() {
        return estoque.size();
    }

    @Override
    public Estoque getItem(int position) {
        return estoque.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();

        EstoqueAdapter.ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_list, parent, false);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new EstoqueAdapter.ItemSuporte();

            itemHolder.ivPhoto = convertView.findViewById(R.id.ivPhoto);
            itemHolder.tvNome = convertView.findViewById(R.id.txtViewNome);
            itemHolder.tvTam = convertView.findViewById(R.id.txtViewTam);
            itemHolder.tvFabri = convertView.findViewById(R.id.txtViewFrabri);
            itemHolder.tvCod = convertView.findViewById(R.id.txtViewQtd);
            itemHolder.tvPreTotal = convertView.findViewById(R.id.txtViewPrecoTotal);
            //define os itens na view;
            convertView.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (EstoqueAdapter.ItemSuporte) convertView.getTag();
        }

        final Estoque prod = getItem(position);



        //ivPhoto.setImageDrawable();
        if(prod.getInfoProduto().getUrl().contains("/")){
            Picasso.get().load(prod.getInfoProduto().getUrl()).into(itemHolder.ivPhoto);
        }else {
            itemHolder.ivPhoto.setImageResource(R.drawable.ic_add_to_photos_black_80dp);
        }


        itemHolder.tvNome.setText(prod.getInfoProduto().getNome());
        itemHolder.tvTam.setText(prod.getInfoProduto().getTamanho());
        itemHolder.tvFabri.setText(prod.getInfoProduto().getFabricante());
        itemHolder.tvCod.setText(prod.getQtd()+"");
        itemHolder.tvPreTotal.setText(prod.getPrecoTotal()+"");


        return convertView;
    }

    private class ItemSuporte {

        ImageView ivPhoto;
        TextView tvNome,tvTam,tvFabri,tvCod ,tvPreTotal;
    }
    }
