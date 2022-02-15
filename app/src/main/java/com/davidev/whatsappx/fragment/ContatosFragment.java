package com.davidev.whatsappx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidev.whatsappx.R;
import com.davidev.whatsappx.activity.ChatActivity;
import com.davidev.whatsappx.activity.GrupoActivity;
import com.davidev.whatsappx.adapter.ContatosAdapter;
import com.davidev.whatsappx.config.ConfiguracaoFirebase;
import com.davidev.whatsappx.helper.RecyclerItemClickListener;
import com.davidev.whatsappx.helper.UsuarioFirebase;
import com.davidev.whatsappx.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private final ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configurações iniciais
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //configurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity() );

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaContatos.setLayoutManager( layoutManager );
        recyclerViewListaContatos.setHasFixedSize( true );
        recyclerViewListaContatos.setAdapter( adapter );

        //Configurar evento de clique no recyclerview
        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<Usuario> listaUsuariosAtualizada = adapter.getContatos();

                                Usuario usuarioSelecionado = listaUsuariosAtualizada.get(position);
                                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();

                                if (cabecalho) {

                                    Intent i = new Intent(getActivity(), GrupoActivity.class);
                                    startActivity(i);

                                } else {
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato", usuarioSelecionado);
                                    startActivity(i);
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        adicionarMenuNovoGrupo();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void limparListaContatos() {
        listaContatos.clear();
        adicionarMenuNovoGrupo();
    }

    public void adicionarMenuNovoGrupo() {
        //Define usuário com e-mail vazio
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);

    }

    public void pesquisarContatos(String texto) {
        //Log.d("pesquisa",  texto );

        List<Usuario> listaContatosBusca = new ArrayList<>();

        for (Usuario usuario : listaContatos) {

            String nome = usuario.getNome().toLowerCase();
            if (nome.contains(texto)) {
                listaContatosBusca.add(usuario);
            }

        }

        adapter = new ContatosAdapter(listaContatosBusca, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarContatos() {
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarContatos() {


        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                limparListaContatos();


                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    assert usuario != null;
                    assert emailUsuarioAtual != null;
                    if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                        listaContatos.add(usuario);
                    }


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}