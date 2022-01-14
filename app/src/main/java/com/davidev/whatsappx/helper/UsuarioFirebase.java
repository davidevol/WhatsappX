package com.davidev.whatsappx.helper;

import android.net.Uri;
import android.util.Log;

import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class UsuarioFirebase {

    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = Objects.requireNonNull(usuario.getCurrentUser()).getEmail();
        //String identificadorUsuario = Base64Custom.codificarBase64( email );

        assert email != null;
        return Base64Custom.codificarBase64( email );

    }

    // Recupera o usuario atual
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    // Caso o usuario queira mudar o proprio nome
    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(task -> {
                if( !task.isSuccessful() ){
                    Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static boolean atualizarFotoUsuario(Uri url){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(task -> {
                if( !task.isSuccessful() ){
                    Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static Usuario getDadosUsuarioLogado() {

        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        if (firebaseUser.getPhotoUrl() == null){
            usuario.setFoto("");
        }else {
            usuario.setFoto( firebaseUser.getPhotoUrl().toString() );
        }

        return usuario;
    }

}
