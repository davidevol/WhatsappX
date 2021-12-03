package com.davidev.whatsappx.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

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

        if (item.getItemId() == R.id.menuSair) {
            deslogarUsuario();
            finish();
        }

        /*
        Anotado com possibilidade de usos futuros
        switch ( item.getItemId() ){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
        }
         */

        return super.onOptionsItemSelected(item);
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