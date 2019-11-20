package br.eng.distribuidoracaproni.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.eng.distribuidoracaproni.Produtos;
import br.eng.distribuidoracaproni.R;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

    private Context context;
    private ArrayList<Produtos> produtos;

    public RvAdapter(Context context, ArrayList<Produtos> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.linha_produtos, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        Produtos prod = produtos.get(position);

        Picasso
                .get()
                .load(prod.getUrl())
                .into(holder.ivPhoto);
        holder.txtViewNome.setText(prod.toString());
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        ImageView ivPhoto = itemView.findViewById(R.id.ivPhoto);
        TextView txtViewNome = itemView.findViewById(R.id.txtViewNome);
    }
}
