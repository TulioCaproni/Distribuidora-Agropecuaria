package br.eng.distribuidoracaproni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import br.eng.distribuidoracaproni.ui.adicionarestoque.AdicionarEstoqueFragment;
import br.eng.distribuidoracaproni.ui.cadastrarprodutos.CadastrarProdutosFragment;
import br.eng.distribuidoracaproni.ui.home.HomeFragment;
import br.eng.distribuidoracaproni.ui.listarestoque.ListarEstoqueFragment;
import br.eng.distribuidoracaproni.ui.listarprodutos.ListarProdutosFragment;
import br.eng.distribuidoracaproni.ui.listarvendas.ListarVendasFragment;
import br.eng.distribuidoracaproni.ui.vendas.VendasFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String GOOGLE_ACCOUNT = null ;
    String Tag;
    private String email;
    private String nome;
    Conexao conexao = new Conexao();

    Activity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle i = getIntent().getExtras();
        if( i != null ) {
            email = i.getString("email");
            nome = i.getString("nome");
        }

        if(savedInstanceState == null) {
            // adicionar o fragmento inicial
            getSupportFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment(), "HomeFragment").commit();
        }

        Log.d("myTag", email.toString());

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        // Obtém a referência da view de cabeçalho
        View headerView = navView.getHeaderView(0);

        // Obtém a referência do nome do usuário e altera seu nome
        TextView txtUsuarioEmail = (TextView) headerView.findViewById(R.id.textViewEmail);
        txtUsuarioEmail.setText(email.toString());
        TextView txtUsuarioNome = (TextView) headerView.findViewById(R.id.textViewNome);
        txtUsuarioNome.setText(nome.toString());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (!(conexao.isOnline(MainActivity.this))) {
            Toast.makeText(MainActivity.this, "Verifique a Conexão com a internet!!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        HomeFragment HomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        AdicionarEstoqueFragment AdicionarEstoqueFragment = (AdicionarEstoqueFragment) getSupportFragmentManager().findFragmentByTag("AdicionarEstoqueFragment");
        if(HomeFragment != null && HomeFragment.isVisible()){
            this.finish();
        }else if (AdicionarEstoqueFragment != null && AdicionarEstoqueFragment.isVisible()){
            Fragment fragment = new ListarEstoqueFragment();
            Tag = "ListarEstoqueFragment";
            displaySelectedFragment(fragment ,Tag);
        }else{
            Fragment fragment = new HomeFragment();
            Tag = "HomeFragment";
            displaySelectedFragment(fragment ,Tag);
        }

        /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            //SignInActivity signInActivity = null;
            // signInActivity.signOut();
            callActivity();

        }

        return super.onOptionsItemSelected(item);
    }
    private void callActivity(){

        Intent myIentent = new Intent(MainActivity.this, SignInActivity.class);
        myIentent.putExtra("signOut",1);
        startActivity(myIentent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            Tag = "HomeFragment";
            displaySelectedFragment(fragment ,Tag);
        } else if (id == R.id.nav_cadastrar_produtos) {
            fragment = new CadastrarProdutosFragment();
            Tag = "CadastrarProdutosFragment";
            displaySelectedFragment(fragment, Tag);
        } else if (id == R.id.nav_listar_Produtos) {
            fragment = new ListarProdutosFragment();
            Tag = "ListarProdutosFragment";
            displaySelectedFragment(fragment ,Tag);
        } else if (id == R.id.nav_adicionar_estoque) {
            fragment = new AdicionarEstoqueFragment();
            Tag = "AdicionarEstoqueFragment";
            displaySelectedFragment(fragment ,Tag);
        } else if (id == R.id.nav_listar_estoque) {
            fragment = new ListarEstoqueFragment();
            Tag = "ListarEstoqueFragment";
            displaySelectedFragment(fragment ,Tag);
        }else if (id == R.id.nav_vendas) {
            fragment = new VendasFragment();
            Tag = "VendasFragment";
            displaySelectedFragment(fragment ,Tag);
        }else if (id == R.id.nav_listar_vendas) {
            fragment = new ListarVendasFragment();
            Tag = "ListarVendasFragment";
            displaySelectedFragment(fragment, Tag);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displaySelectedFragment(Fragment fragment, String Tag) {
        if (!(conexao.isOnline(MainActivity.this))) {
            Toast.makeText(MainActivity.this, "Verifique a Conexão com a internet!!!", Toast.LENGTH_SHORT).show();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, Tag);
        fragmentTransaction.commit();
    }

}
