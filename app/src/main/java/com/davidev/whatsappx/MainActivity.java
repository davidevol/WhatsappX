package com.davidev.whatsappx;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;


import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    //Dependências do Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtem a instância do Firebase.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


    }
}