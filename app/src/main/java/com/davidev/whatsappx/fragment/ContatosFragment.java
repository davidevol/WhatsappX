package com.davidev.whatsappx.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.adapter.ContatosAdapter;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatatos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        // Configurações Iniciais
        recyclerViewListaContatos = view.findViewById(R.id.reciclerViewListaContatos);
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");

        // Adapter
        adapter = new ContatosAdapter(listaContatatos, getActivity());

        // Configuração do recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaContatos.setLayoutManager( layoutManager );
        recyclerViewListaContatos.setHasFixedSize( true );
        recyclerViewListaContatos.setAdapter(adapter);

         return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        recuperarContatos();
    }

    public void onStop(){
        super.onStop();
        usuariosRef.removeEventListener( valueEventListenerContatos );
    }

    // Adiciona ao adapter os valores novos
    public void recuperarContatos(){

        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dados: snapshot.getChildren() ){

                    Usuario usuario = dados.getValue( Usuario.class );
                    listaContatatos.add(usuario);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}