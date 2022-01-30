package com.davidev.whatsappx.helper;

import android.util.Base64;

public class Base64Custom {

    // Captura os bytes da string sem o pulador de linha e o retorno de carro.
    public static String codificarBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    // Decodifica para utf-8
    public static String decodificarBase64(String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }

}
