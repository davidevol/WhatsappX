package com.davidev.whatsappx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    //Dependências do Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtem a instância do Firebase.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

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

    }

    //Infla o menu_main.xml dentro da activit_main.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Acesso ao menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        /*
        //Para testar itens unicos
        if (item.getItemId() == R.id.menuSair) {
            deslogarUsuario();
            finish();
        }

         */


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