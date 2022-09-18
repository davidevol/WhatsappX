package com.davidev.whatsappx.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.helper.Base64Custom;
import com.davidev.whatsappx.helper.UsuarioFirebase;
import com.davidev.whatsappx.model.Usuario;
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

    public void validarCadastroUsuario(View view) {

        String Nome = Objects.requireNonNull(campoNome.getText()).toString();
        String Email = Objects.requireNonNull(campoEmail.getText()).toString();
        String Senha = Objects.requireNonNull(campoSenha.getText()).toString();
        boolean ehValido = !Nome.isEmpty() && !Email.isEmpty() && !Senha.isEmpty();

        if (ehValido) {
            usuarioValido(Nome, Email, Senha);
        } else {
            Toast.makeText(CadastroActivity.this,
                    R.string.cadastro_aviso_campos, Toast.LENGTH_SHORT).show();
        }
    }

    private void usuarioValido(String textoNome, String textoEmail, String textoSenha) {
        Usuario usuario = new Usuario();
        usuario.setNome(textoNome);
        usuario.setEmail(textoEmail);
        usuario.setSenha(textoSenha);

        cadastrarUsuario(usuario);
    }

    public void cadastrarUsuario(final Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {
                sucessoCadastro(usuario);
                try {
                    atribuirIdentificador(usuario);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                motivoFracassoCadastro(task);
            }
        });
    }

    private void sucessoCadastro(Usuario usuario) {
        Toast.makeText(CadastroActivity.this,
                R.string.cadastro_sucesso,
                Toast.LENGTH_SHORT).show();
        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
        finish();
    }

    private void atribuirIdentificador(Usuario usuario) {
        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
        usuario.setId(identificadorUsuario);
        usuario.salvar();
    }

    private void motivoFracassoCadastro(Task<AuthResult> task) {
        String excecao;
        try {
            throw Objects.requireNonNull(task.getException());
        } catch (FirebaseAuthWeakPasswordException e) {
            excecao = getString(R.string.cadastro_fracasso_senha_fraca);
        } catch (FirebaseAuthInvalidCredentialsException e) {
            excecao = getString(R.string.cadastro_fracasso_email);
        } catch (FirebaseAuthUserCollisionException e) {
            excecao = getString(R.string.cadastro_fracasso_conta_existente);
        } catch (Exception e) {
            excecao = getString(R.string.cadastro_fracasso) + e.getMessage();
            e.printStackTrace();
        }

        Toast.makeText(CadastroActivity.this,
                excecao,
                Toast.LENGTH_SHORT).show();
    }
}