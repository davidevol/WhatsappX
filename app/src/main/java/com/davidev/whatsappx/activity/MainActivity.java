package com.davidev.whatsappx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.fragment.ContatosFragment;
import com.davidev.whatsappx.fragment.ConversasFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Dependências do Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth autenticacao;

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtem a instância do Firebase.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtem a instância do FirebaseAutenticacao
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        searchView = findViewById(R.id.materialSearchPrincipal);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsappX");
        setSupportActionBar( toolbar );

        //Configura abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter (adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager( viewPager );

        searchView = findViewById(R.id.materialSearchPrincipal);

        // Listener para a pesquisa
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();


            }
        });

        // Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                if( newText != null && !newText.isEmpty() ) {
                    fragment.pesquisarConversas(newText.toLowerCase());

                }
                    return true;

            }
        });


    }

    //Infla o menu_main.xml dentro da activit_main.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    //Acesso ao menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent (MainActivity.this, ConfiguracoesActivity.class);
        startActivity( intent );
    }

    //Tenta deslogar o usuario atual
    public void deslogarUsuario(){

        try {
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}