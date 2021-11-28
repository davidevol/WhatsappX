package com.davidev.whatsappx.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);

    }

    public void logarUsuario(Usuario usuario){


        //Loga o usuario
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {

            if ( task.isSuccessful() ){
                abrirTelaPrincipal();
            } else {

                String excecao;
                try {
                    throw Objects.requireNonNull(task.getException());
                }catch  (FirebaseAuthInvalidCredentialsException e) {
                    excecao = "E-mail e senha não correspondem!";
                }catch  (FirebaseAuthInvalidUserException e) {
                    excecao = "Usuário não está cadastrado!";
                }catch  (Exception e) {
                    excecao = "Erro ao logar usuário: " + e.getMessage();
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Efetua o login
    public void validarAutenticacaoUsuario(View view){

        //Recupera os textos dos campos
        String textoEmail = Objects.requireNonNull(campoEmail.getText()).toString();
        String textoSenha = Objects.requireNonNull(campoSenha.getText()).toString();

        //Checar se campos foram preenchidos
        if (!textoEmail.isEmpty()) {//Checa se há email
            if (!textoSenha.isEmpty()) {//Checa se há senha

                Usuario usuario = new Usuario();
                usuario.setEmail( textoEmail );
                usuario.setSenha( textoSenha );

                logarUsuario( usuario );

            } else {
                Toast.makeText(LoginActivity.this, "Faltou preencher a senha!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(LoginActivity.this, "Faltou preencher o email!", Toast.LENGTH_SHORT).show();
        }

    }

    //Mantem logado se haver login existente
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if( usuarioAtual != null ){
            abrirTelaPrincipal();
        }
    }

    //Abre a activity de cadastro
    public void abrirTelaCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity( intent );
    }

    //Abre a activity principal
    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity( intent );
    }

}