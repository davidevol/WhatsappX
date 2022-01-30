package com.davidev.whatsappx.adapter;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.davidev.whatsappx.R;
import com.davidev.whatsappx.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GrupoSelecionadoAdapter  extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {

    private List<Usuario> contatosSelecionados;
    private Context context;

    public GrupoSelecionadoAdapter(List<Usuario> listaContatos, Context c) {
        this.contatosSelecionados = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public GrupoSelecionadoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext() ).inflate(R.layout.adapter_grupo_selecionado, parent, false);
        return new GrupoSelecionadoAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(GrupoSelecionadoAdapter.MyViewHolder holder, int position) {

        Usuario usuario = contatosSelecionados.get( position );

        holder.nome.setText( usuario.getNome() );

        if( usuario.getFoto() != null ){
            Uri uri = Uri.parse( usuario.getFoto() );
            Glide.with( context ).load( uri ).into( holder.foto );
        }else {
            holder.foto.setImageResource( R.drawable.padrao );
        }

    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado);

        }
    }

}
