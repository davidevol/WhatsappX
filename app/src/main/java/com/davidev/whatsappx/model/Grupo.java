package com.davidev.whatsappx.model;

import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {

    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        setId( idGrupoFirebase );

    }

    public void salvar(){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference grupoRef = database.child("grupos");

        grupoRef.child( getId() ).setValue( this );

        //Salvar conversa para membros do grupo
        for( Usuario membro: getMembros() ){

            String idRemetente = Base64Custom.codificarBase64( membro.getEmail() );
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente( idRemetente );
            conversa.setIdDestinatario( idDestinatario );
            conversa.setUltimaMensagem("");
            conversa.setIsGroup("true");
            conversa.setGrupo( this );

            conversa.salvar();

        }

    }

    public String getId() {
        return id;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
