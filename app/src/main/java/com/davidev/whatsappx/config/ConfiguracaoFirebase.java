package com.davidev.whatsappx.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.jar.Manifest;

public class ConfiguracaoFirebase {

    private static DatabaseReference databaase;
    private static FirebaseAuth auth;

    //Retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if ( databaase == null ){
            databaase = FirebaseDatabase.getInstance().getReference();
        }
        return databaase;
    }

    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if ( auth == null ){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}
