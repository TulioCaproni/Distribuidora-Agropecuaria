package br.eng.distribuidoracaproni;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

        if(savedInstanceState == null) {
            // adicionar o fragmento inicial
            getSupportFragmentManager().beginTransaction().add(R.id.frame, new HomeFragment(), "HomeFragment").commit();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        HomeFragment HomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if(HomeFragment != null && HomeFragment.isVisible()){
            this.finish();
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
            this.finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, Tag);
        //fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }

}
