package com.davidev.whatsappx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.davidev.whatsappx.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
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

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsappX");
        setSupportActionBar( toolbar );

        /*
        //Configurando abas
        FragmentPagerItemAdapter adapter = FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add()
                .add()
                .create()
        );

         */

    }

    //Infla o menu_main.xml dentro da activit_main.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Acessa o item Sair do menu e desloga usuario
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Tenta deslogar o usuario atual
    public void deslogarUsuario(){

        try {
                autenticacao.signOut();
        }catch (Exception e){

        }
    }
}