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

import com.br.tiago.roupas.R;
import com.br.tiago.roupas.activity.home.DetalheDonativoActivity;
import com.br.tiago.roupas.model.Donativo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonativoTabFragment extends Fragment {

    List<Donativo> donativoList = new ArrayList<>();
    DonativoAdapter donativoAdapter;
    RecyclerView recyclerView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_donativo_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle("Aguardando Doações");

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarDonativo);

        this.setFirebaseInstance();
        this.setDatabaseReference();

        this.recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewDonativos);

        if (this.recyclerView != null) {

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

            this.recyclerView.setLayoutManager(mLayoutManager);
        }
    }

    /*ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                Necessidade necessidade = snapshot.getValue(Necessidade.class);

                donativoList.add(necessidade);

                Toast.makeText(getContext(), "Tipo: "+necessidade.getTipo(), Toast.LENGTH_SHORT).show();
            }

            donativoAdapter = new NecessidadeAdapter(donativoList);

            recyclerView.setAdapter(donativoAdapter);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                if( snapshot.getKey().contentEquals("donativos") ){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        Donativo donativo = snapshot1.getValue(Donativo.class);

                        donativoList.add(donativo);
                    }
                }
            }

            donativoAdapter = new DonativoAdapter(donativoList);

            if (recyclerView != null) {
                recyclerView.setAdapter(donativoAdapter);
                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    };

    private void setDatabaseReference() {

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        String uuid = FirebaseAuth.getInstance().getUid();

        //this.mDatabaseReference = this.mFirebaseDatabase.getReference("necessidades/" + uuid);
        this.mDatabaseReference = this.mFirebaseDatabase.getReference("usuarios/" + uuid);
        //this.mDatabaseReference.addChildEventListener(childEventListener);
        this.mDatabaseReference.orderByChild("createdAt")
                               //.startAt("2018-11-30").endAt("2018-12-01")
                               //.limitToLast(4)
                               .addListenerForSingleValueEvent(valueEventListener);
    }

    private void setFirebaseInstance() {

        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    public class DonativoHolder extends RecyclerView.ViewHolder{

        public TextView textViewTipo, textViewJustificativa, textViewDescricao, textViewCreatedAt, textViewAguardando;

        public DonativoHolder(@NonNull final View itemView) {
            super(itemView);

            this.textViewTipo        = itemView.findViewById(R.id.textViewTipoDonativo);
            this.textViewJustificativa = itemView.findViewById(R.id.textViewJustificativaDonativo);
            this.textViewCreatedAt     = itemView.findViewById(R.id.textViewCreatedAtDonativo);
            this.textViewAguardando     = itemView.findViewById(R.id.textViewAguardandoDonativo);

            itemView.setOnClickListener( itemViewOnClickListener );
        }

        private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),DetalheDonativoActivity.class);
                intent.putExtra("tipo",((TextView) v.findViewById(R.id.textViewTipoDonativo)).getText());
                intent.putExtra("justificativa",((TextView) v.findViewById(R.id.textViewJustificativaDonativo)).getText());
                intent.putExtra("createdAt",((TextView) v.findViewById(R.id.textViewCreatedAtDonativo)).getText());

                startActivity(intent);

                /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new DetalheNecessidadeFragment());
                ft.commit();*/
            }
        };
    }

    public class DonativoAdapter extends RecyclerView.Adapter<DonativoHolder> {
        
        public List<Donativo> donativoList;

        public DonativoAdapter(List<Donativo> m){
            this.donativoList = m;
        }

        @NonNull
        @Override
        public DonativoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_donativo,parent,false );

            return new DonativoHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull DonativoHolder holder, int position) {

            String tipo        = this.donativoList.get(position).getTipo();
            String justificativa = this.donativoList.get(position).getJustificativa();
            String descricao     = this.donativoList.get(position).getDescricao();
            String createdAt     = this.donativoList.get(position).getCreatedAt();

            holder.textViewTipo.setText( tipo );
            holder.textViewJustificativa.setText( justificativa );
            holder.textViewCreatedAt.setText( createdAt );
            holder.textViewAguardando.setText( this.calcularPeriodoAguardandoDonativo(createdAt) );
        }

        @Override
        public int getItemCount() {
            return this.donativoList.size();
        }

        public void setMovieList(List<Donativo> donativoList) {
            this.donativoList = donativoList;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String calcularPeriodoAguardandoDonativo(String createdAt){
            return "dd/mm/yyyy";
            /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDate dtAtual = LocalDate.now();
            LocalDate dtCadastro = LocalDate.parse( createdAt, formatter );

            String dias  = Long.toString( ChronoUnit.DAYS.between(dtCadastro, dtAtual) );
            String meses = Long.toString( ChronoUnit.MONTHS.between(dtCadastro, dtAtual) );
            String anos  = Long.toString( ChronoUnit.YEARS.between(dtCadastro, dtAtual) );

            return anos + " anos, "+meses+" meses e "+dias+" dias";*/
        }
    }
}
