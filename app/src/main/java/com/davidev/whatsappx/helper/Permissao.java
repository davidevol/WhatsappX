package com.davidev.whatsappx.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> listaPermissoes = new ArrayList<>();

            /*Percorre as permissões passadas,
            verificando uma a uma
            * se já tem a permissao liberada */
            for ( String permissao : permissoes ){
                boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if ( !temPermissao ) listaPermissoes.add(permissao);
            }

            /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
            if ( listaPermissoes.isEmpty() ) return true;
            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray( novasPermissoes );

            //Solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode );


        }

        return true;

    }

}
