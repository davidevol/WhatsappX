package com.davidev.whatsappx.model;

import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {
    }

    // Com os emails obtidos do cadastro, são codificados em base64 e adiciona ao diretório chamado: usuarios
    public void salvar() {
            DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
            DatabaseReference usuario = firebaseRef.child("usuarios").child( getId() );

            usuario.setValue( this );
    }

    public void atualizar(){

        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("usuarios")
                .child( identificadorUsuario );

        Map<String, Object> valoresUsuario = converterParaMap();


        usuariosRef.updateChildren( valoresUsuario );

    }

    @Exclude
    public Map<String, Object> converterParaMap(){// Recupera valores do usuario, converte para objeto HashMap

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail() );
        usuarioMap.put("nome", getNome()   );
        usuarioMap.put("foto", getFoto()   );

        return usuarioMap;

    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}