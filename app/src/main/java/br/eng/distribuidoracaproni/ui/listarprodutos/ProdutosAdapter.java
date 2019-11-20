package br.eng.distribuidoracaproni.ui.listarprodutos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.eng.distribuidoracaproni.objetos.Produtos;
import br.eng.distribuidoracaproni.R;

public class ProdutosAdapter extends BaseAdapter {

    private AlertDialog alerta;

    private Context context;
    private ArrayList<Produtos> produtos;
    private ProdutosListener listener;
    String nomeProd, tamanhoProd;
    int id=0;

    public ProdutosAdapter(Context context, ArrayList<Produtos> produtos,ProdutosListener listener) {
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
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = LayoutInflater.from(context).inflate(R.layout.linha_produtos, parent, false);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();

            itemHolder.ivPhoto = convertView.findViewById(R.id.ivPhoto);
            itemHolder.btnEdit = convertView.findViewById(R.id.imageButtonEdit);
            itemHolder.btnDel = convertView.findViewById(R.id.imageButtonDel);
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


        itemHolder.btnEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Editar("Editar","Deseja editar o produto "+prod.getNome()+" "+prod.getTamanho()+"?");
                    nomeProd = prod.getNome();
                    tamanhoProd = prod.getTamanho();

                }

            });
        itemHolder.btnDel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Excluir("Excluir","Deseja excluir o produto "+prod.getNome()+" "+prod.getTamanho()+"?");
                    nomeProd = prod.getNome();
                    tamanhoProd = prod.getTamanho();
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

    private class ItemSuporte {

        ImageView ivPhoto;
        ImageButton btnEdit,btnDel;
        TextView tvNome,tvTam,tvFabri,tvCod ,tvPre;
    }

    public void Editar(String titulo, String message){
        final boolean[] resultado = new boolean[1];
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(message);
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                id =1;
                listener.onSessionClicked(id, nomeProd,tamanhoProd);

            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void Excluir(String titulo, String message){
        final boolean[] resultado = new boolean[1];
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle(titulo);
        //define a mensagem
        builder.setMessage(message);
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                id =2;
                listener.onSessionClicked(id, nomeProd,tamanhoProd);

            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}

