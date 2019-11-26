package br.eng.distribuidoracaproni.ui.cadastrarprodutos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import br.eng.distribuidoracaproni.Conexao;
import br.eng.distribuidoracaproni.MainActivity;
import br.eng.distribuidoracaproni.R;
import br.eng.distribuidoracaproni.objetos.Produtos;
import br.eng.distribuidoracaproni.ui.listarprodutos.ListarProdutosFragment;

public class CadastrarProdutosFragment extends Fragment implements  View.OnClickListener{

    Conexao conexao = new Conexao();

    private CadastrarProdutosViewModel mViewModel;
    Produtos produtos = new Produtos();

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference produtosReference = databaseReference.child("Produtos");

    //Firebase Storage
    Button chooseImg;
    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    Bitmap bitmap;

    StorageReference ref;
    UploadTask uploadTask;


    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://distribuidoracaproni.appspot.com/");

    Fragment fragment = null;

    // While the file names are the same, the references point to different files
    //mountainsRef.getName().equals(mountainImagesRef.getName());    // true
    //mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

    private Button btnCadastrar ;
    public TextView textNome,textFabricante,textTamanho,textPreco,textCodigo;
    String textUrl="not";
    String dadosedit="not";


    public static CadastrarProdutosFragment newInstance() {
        return new CadastrarProdutosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cadastrar_produtos_fragment, container, false);

        btnCadastrar = view.findViewById(R.id.cadastrar);

        textNome = view.findViewById(R.id.nome);
        textFabricante = view.findViewById(R.id.fabricante);
        textTamanho = view.findViewById(R.id.tamanho);
        textPreco = view.findViewById(R.id.preco);
        textCodigo = view.findViewById(R.id.codigo);

        chooseImg = view.findViewById(R.id.chooseImg);
        imgView = view.findViewById(R.id.imgView);

        if (Produtos.sel_prod == 1){
            carregarDados();
        }

        return view;
    }

    private void carregarDados() {
        produtosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Produtos produtos = objSnapshot.getValue(Produtos.class);
                        if(produtos.getNome().equals(Produtos.nomeProdudutoSel) && produtos.getTamanho().equals(Produtos.tamanhoProdudutoSel)){
                            textNome.setText(produtos.getNome().toString());
                            textTamanho.setText(produtos.getTamanho().toString());
                            textFabricante.setText(produtos.getFabricante().toString());
                            textCodigo.setText(produtos.getCodigo().toString());
                            textPreco.setText(produtos.getPreco().toString());
                            textUrl = produtos.getUrl().toString();
                            if(produtos.getUrl().contains("/")){
                                Picasso.get().load(produtos.getUrl()).into(imgView);
                            }else {
                                imgView.setImageResource(R.drawable.ic_add_to_photos_black_80dp);
                            }
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CadastrarProdutosViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onResume() {
        super.onResume();
        btnCadastrar.setOnClickListener(this);
        chooseImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cadastrar:
                upload();
                break;
            case R.id.chooseImg:
                choseImagem();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void choseImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                ContentResolver contentResolver;
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);

                //Setting image to ImageView
                imgView.setImageBitmap(bit);
            } catch (Exception e) {
                e.printStackTrace();
            }/*
            try {
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);

                bitmap = new Compressor(getContext())
                        .setMaxHeight(200) //Set height and width
                        .setMaxWidth(200)
                        .setQuality(100) // Set Quality
                        .compressToFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] bytes = baos.toByteArray();
            uploadTask = ref.putBytes(bytes);*/
        }
    }

    public void upload(){

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ref = storageRef.child("images").child(textNome.getText().toString()+" "+textTamanho.getText().toString());
            //+ UUID.randomUUID().toString());

            uploadTask = ref.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            //Do what you want with the url
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            textUrl = downloadUrl.toString();
                            salvarDadosFirebase();
                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }else{
            salvarDadosFirebase();
        }
    }


    public void salvarDadosFirebase() {
        produtos.setNome(textNome.getText().toString());
        produtos.setFabricante(textFabricante.getText().toString());
        produtos.setTamanho(textTamanho.getText().toString());
        produtos.setPreco(textPreco.getText().toString());
        produtos.setCodigo(textCodigo.getText().toString());
        produtos.setUrl(textUrl.toString());

        if(verificaNull()) {

            if (conexao.isOnline(getContext())) {

                if (Produtos.sel_prod == 1) {
                    Toast.makeText(getContext(), produtos.getNome().toString() + " editado com sucesso !", Toast.LENGTH_SHORT).show();
                    Produtos.sel_prod = -1;
                    produtosReference.child(Produtos.nomeProdudutoSel + " " + Produtos.tamanhoProdudutoSel).removeValue();
                } else {
                    Toast.makeText(getContext(), produtos.getNome().toString() + " cadastrado com sucesso !", Toast.LENGTH_SHORT).show();
                }
                produtosReference.child(textNome.getText().toString() + " " + textTamanho.getText().toString()).setValue(produtos);

                fragment = new ListarProdutosFragment();
                Activity main = getActivity();
                String Tag = "ListarProdutosFragment";

                if (main != null) {
                    ((MainActivity) getActivity()).displaySelectedFragment(fragment, Tag);
                }
            } else {
                Toast.makeText(getContext(), "Verifique a Conexão com a internet!!!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private boolean verificaNull() {
        if(produtos.getNome().equals("") || produtos.getFabricante().equals("") || produtos.getTamanho().equals("") || produtos.getCodigo().equals("") || produtos.getPreco().equals("") || produtos.getUrl().equals("") ){
            Toast.makeText(getContext(), "Dados invalidos ! ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}