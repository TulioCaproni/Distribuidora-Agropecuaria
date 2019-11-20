package br.eng.distribuidoracaproni.ui.vendas;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.eng.distribuidoracaproni.objetos.Estoque;
import br.eng.distribuidoracaproni.Informacaodialog.InformacaoDialog;
import br.eng.distribuidoracaproni.Informacaodialog.InformacaoListener;
import br.eng.distribuidoracaproni.R;

class VendasAdapter  extends BaseAdapter implements InformacaoListener {

    private AlertDialog alerta;

    InformacaoDialog info;

    VendasListener listener;

    private Context context;
    private ArrayList<Estoque> estoque;
    String nomeProd, tamanhoProd, fabricanteProd,precoProd,urlProd ;
    int qtdProd;

    public VendasAdapter(Context context, ArrayList<Estoque> est, VendasListener listener) {
        this.context = context;
        this.estoque = est;
        this.listener=listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        VendasAdapter.ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_vendas, parent, false);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new VendasAdapter.ItemSuporte();

            itemHolder.ivPhoto = convertView.findViewById(R.id.ivPhoto);
            itemHolder.btnVend = convertView.findViewById(R.id.imageButtonVenda);
            itemHolder.tvNome = convertView.findViewById(R.id.txtViewNome);
            itemHolder.tvTam = convertView.findViewById(R.id.txtViewTam);
            itemHolder.tvFabri = convertView.findViewById(R.id.txtViewFrabri);
            itemHolder.tvCod = convertView.findViewById(R.id.txtViewQtd);
            itemHolder.tvPre = convertView.findViewById(R.id.txtViewPrec);
            //define os itens na view;
            convertView.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (VendasAdapter.ItemSuporte) convertView.getTag();
        }

        final Estoque prod = getItem(position);

        itemHolder.btnVend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nomeProd = prod.getInfoProduto().getNome();
                tamanhoProd = prod.getInfoProduto().getTamanho();
                qtdProd = prod.getQtd();
                fabricanteProd = prod.getInfoProduto().getFabricante();
                precoProd = prod.getInfoProduto().getPreco();
                urlProd = prod.getInfoProduto().getUrl();
                chamarAlert();
            }

        });


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
        itemHolder.tvPre.setText(prod.getInfoProduto().getPreco());


        return convertView;
    }

    @Override
    public void onSessionClickedAlert(int quantidade) {
        if(quantidade > qtdProd){
            Toast.makeText(context, "Quantidade de produtos insuficente!", Toast.LENGTH_SHORT).show();
        }else{
            listener.onSessionClicked(nomeProd, tamanhoProd, fabricanteProd,precoProd, urlProd , quantidade);
        }
    }

    private class ItemSuporte {

        ImageView ivPhoto;
        ImageButton btnVend;
        TextView tvNome,tvTam,tvFabri,tvCod ,tvPre;
    }

    public void chamarAlert(){
        info = new InformacaoDialog(context,this);
        info.show();
    }
}
