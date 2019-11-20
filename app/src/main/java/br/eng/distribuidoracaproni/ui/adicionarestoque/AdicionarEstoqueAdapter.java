package br.eng.distribuidoracaproni.ui.adicionarestoque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.eng.distribuidoracaproni.Informacaodialog.InformacaoDialog;
import br.eng.distribuidoracaproni.Informacaodialog.InformacaoListener;
import br.eng.distribuidoracaproni.objetos.Produtos;
import br.eng.distribuidoracaproni.R;

public class AdicionarEstoqueAdapter extends BaseAdapter implements InformacaoListener {

    InformacaoDialog info;

    private Context context;
    private ArrayList<Produtos> produtos;
    private AdicionarEstoqueListener listener;
    String nomeProd, tamanhoProd;
    int qtd=0;

    public AdicionarEstoqueAdapter(Context context, ArrayList<Produtos> produtos, AdicionarEstoqueListener listener) {
        this.context = context;
        this.produtos = produtos;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Produtos getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        notifyDataSetChanged();

        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_addestoque, parent, false);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();

            itemHolder.ivPhoto = convertView.findViewById(R.id.ivPhoto);
            itemHolder.btnAdd = convertView.findViewById(R.id.imageButtonAdd);
            itemHolder.tvNome = convertView.findViewById(R.id.txtViewNome);
            itemHolder.tvTam = convertView.findViewById(R.id.txtViewTam);
            itemHolder.tvFabri = convertView.findViewById(R.id.txtViewFrabri);
            itemHolder.tvCod = convertView.findViewById(R.id.txtViewCod);
            itemHolder.tvPre = convertView.findViewById(R.id.txtViewPreco);
            //define os itens na view;
            convertView.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) convertView.getTag();
        }

        final Produtos prod = getItem(position);


        // Mais
        itemHolder.btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nomeProd = prod.getNome();
                tamanhoProd = prod.getTamanho();
                chamarAlert();
            }

        });

        //ivPhoto.setImageDrawable();
        if(prod.getUrl().contains("/")){
            Picasso.get().load(prod.getUrl()).into(itemHolder.ivPhoto);
        }else {
            itemHolder.ivPhoto.setImageResource(R.drawable.ic_add_to_photos_black_80dp);
        }


        itemHolder.tvNome.setText(prod.getNome());
        itemHolder.tvTam.setText(prod.getTamanho());
        itemHolder.tvFabri.setText(prod.getFabricante());
        itemHolder.tvCod.setText(prod.getCodigo());
        itemHolder.tvPre.setText(prod.getPreco());


        return convertView;
    }

    @Override
    public void onSessionClickedAlert(int quantidade) {

        qtd = quantidade;

        info.dismiss();
        listener.onSessionClicked(nomeProd, tamanhoProd, qtd);
    }

    private class ItemSuporte {

        ImageView ivPhoto;
        ImageButton btnAdd;
        TextView tvNome,tvTam,tvFabri,tvCod ,tvPre;
    }

    public void chamarAlert(){
        info = new InformacaoDialog(context,this);
        info.show();
    }
}