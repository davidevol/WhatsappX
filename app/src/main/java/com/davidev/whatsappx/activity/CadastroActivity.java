package com.davidev.whatsappx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.helper.Base64Custom;
import com.davidev.whatsappx.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
    }

    //Cadastra recuperando o objeto usuario de validarCadastroUsuario(View view)
    public void cadastrarUsuarioFirebase(final Usuario usuario){


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()){//Checa se foi sucedido em criar o usuário

                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar!",
                            Toast.LENGTH_SHORT).show();
                    finish();

                    try {

                        String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                        usuario.setId( identificadorUsuario );
                        usuario.salvar();


                    }catch (Exception e){
                        e.printStackTrace();
                    }

            } else {//Exibe o erro ao usuário

                String excecao;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    excecao = "Digite uma senha mais forte!";
                } catch ( FirebaseAuthInvalidCredentialsException e) {
                    excecao = "Por favor, digite um e-mail válido";
                } catch ( FirebaseAuthUserCollisionException e) {
                    excecao = "Esta conta já foi cadastrada";
                } catch (Exception e) {
                    excecao = "Erro ao cadastrar o usuário: " + e.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
            }

            }
        });

    }


//Acompanha incoerencias de cadastro que o usuário possa cometer.
    public void validarCadastroUsuario(View view) {
        String textoNome = Objects.requireNonNull(campoNome.getText()).toString();
        String textoEmail = Objects.requireNonNull(campoEmail.getText()).toString();
        String textoSenha = Objects.requireNonNull(campoSenha.getText()).toString();

        if (!textoNome.isEmpty()) {//Checa se há nome
            if (!textoEmail.isEmpty()) {//Checa se há email
                if (!textoSenha.isEmpty()) {//Checa se há senha

                    Usuario usuario = new Usuario();
                    usuario.setNome( textoNome );
                    usuario.setEmail( textoEmail );
                    usuario.setSenha( textoSenha );

                    cadastrarUsuarioFirebase( usuario );

                } else {
                    Toast.makeText(CadastroActivity.this, "Faltou preencher a senha!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(CadastroActivity.this, "Faltou preencher o email!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CadastroActivity.this, "Faltou preencher o nome!", Toast.LENGTH_SHORT).show();
        }

    }

}

