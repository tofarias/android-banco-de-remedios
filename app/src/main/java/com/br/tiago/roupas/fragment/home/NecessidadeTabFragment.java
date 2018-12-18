package com.br.tiago.roupas.fragment.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.tiago.roupas.Helpers.DataHelper;
import com.br.tiago.roupas.R;
import com.br.tiago.roupas.activity.home.DetalheNecessidadeActivity;
import com.br.tiago.roupas.model.Necessidade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class NecessidadeTabFragment extends Fragment {

    List<Necessidade> necessidadeList = new ArrayList<>();

    NecessidadeAdapter necessidadeAdapter;
    RecyclerView recyclerView;

    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_necessidade_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle("Aguardando Doações");

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarNecessidade);

        this.setDatabaseReference();

        this.recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewNecessidades);

        if (this.recyclerView != null) {

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

            this.recyclerView.setLayoutManager(mLayoutManager);
            this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);


                }
            });
        }
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                if( snapshot.getKey().contentEquals("necessidades") ){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        Necessidade necessidade = snapshot1.getValue(Necessidade.class);

                        necessidadeList.add(necessidade);
                    }
                }
            }

            necessidadeAdapter = new NecessidadeAdapter(necessidadeList);

            if (recyclerView != null) {
                recyclerView.setAdapter(necessidadeAdapter);
                /*recyclerView.addItemDecoration(
                        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));*/
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void setDatabaseReference() {

        Query query = FirebaseDatabase.getInstance().getReference("usuarios/").orderByChild("necessidades");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if( snapshot.child("necessidades").exists() ){

                        DataSnapshot snapshot1 = snapshot.child("necessidades");

                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                            String id = snapshot.getKey().toString()+"/necessidades/"+snapshot2.getKey().toString();

                            Necessidade necessidade = snapshot2.getValue(Necessidade.class);
                            necessidade.setId(id);
                            necessidadeList.add(necessidade);
                        }
                    }
                }
                necessidadeAdapter = new NecessidadeAdapter(necessidadeList);

                if (recyclerView != null) {
                    recyclerView.setAdapter(necessidadeAdapter);
                    recyclerView.addItemDecoration(
                            new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                }

                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //this.mDatabaseReference = this.mFirebaseDatabase.getReference("necessidades/" + uuid);
        /*this.mDatabaseReference = this.mFirebaseDatabase.getReference("usuarios/");
        //this.mDatabaseReference.addChildEventListener(childEventListener);
        this.mDatabaseReference.orderByChild("createdAt")
                               //.startAt("2018-11-30").endAt("2018-12-01")
                               //.limitToLast(4)
                               .addListenerForSingleValueEvent(valueEventListener);*/
    }

    public class NecessidadeHolder extends RecyclerView.ViewHolder{

        private TextView textViewTipo, textViewJustificativa, textViewDescricao;
        private TextView textViewNecIdN, textViewCreatedAt, textViewAguardando;

        public NecessidadeHolder(@NonNull final View itemView) {
            super(itemView);

            this.textViewTipo          = itemView.findViewById(R.id.textViewTipoN);
            this.textViewJustificativa = itemView.findViewById(R.id.textViewJustificativa);
            this.textViewCreatedAt     = itemView.findViewById(R.id.textViewCreatedAt);
            this.textViewAguardando    = itemView.findViewById(R.id.textViewAguardandoN);
            this.textViewNecIdN        = itemView.findViewById(R.id.textViewNecIdN);

            itemView.setOnClickListener( itemViewOnClickListener );
        }

        private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),DetalheNecessidadeActivity.class);
                intent.putExtra("necId",((TextView) v.findViewById(R.id.textViewNecIdN)).getText());

                startActivity(intent);
            }
        };
    }

    public class NecessidadeAdapter extends RecyclerView.Adapter<NecessidadeHolder> {
        
        public List<Necessidade> necessidadeList;

        public NecessidadeAdapter(List<Necessidade> m){
            this.necessidadeList = m;
        }

        @NonNull
        @Override
        public NecessidadeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_necessidade,parent,false );

            return new NecessidadeHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull NecessidadeHolder holder, int position) {

            String tipo          = this.necessidadeList.get(position).getTipo();
            String justificativa = this.necessidadeList.get(position).getJustificativa();
            String descricao     = this.necessidadeList.get(position).getDescricao();
            String createdAt     = DataHelper.createdAtToBr( this.necessidadeList.get(position).getCreatedAt() );
            String id           = this.necessidadeList.get(position).getId();

            holder.textViewTipo.setText( tipo );
            holder.textViewJustificativa.setText( justificativa );
            holder.textViewCreatedAt.setText( createdAt );

            try {

                holder.textViewAguardando.setText( DataHelper.calcularPeriodoAguardandoDonativo( this.necessidadeList.get(position).getCreatedAt() ) );
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.textViewNecIdN.setText( id );
        }

        @Override
        public int getItemCount() {
            return this.necessidadeList.size();
        }

        public void setMovieList(List<Necessidade> necessidadeList) {
            this.necessidadeList = necessidadeList;
        }
    }
}
