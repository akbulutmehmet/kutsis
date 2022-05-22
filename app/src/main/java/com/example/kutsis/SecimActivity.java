package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kutsis.adapter.MasaRecyclerViewAdapter;
import com.example.kutsis.model.Kutuphane;
import com.example.kutsis.model.Masa;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SecimActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private void initComponents() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView = findViewById(R.id.masaRecyclerView);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(SecimActivity.this,  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secim);
        initComponents();
        registerHandlers();
        Intent intent = getIntent();
        String message = intent.getStringExtra(QRActivity.MESSAGE_LIBRARYID);
        loadData(message);

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kutuphane kutuphane = (Kutuphane) snapshot.getValue(Kutuphane.class);
                List<Masa> masaList = kutuphane.getMasaList();
                for(int i=0;i<masaList.size();i++) {
                    Masa masa = masaList.get(i);
                    Date date = new Date();
                    Date lastReserveDate = masa.getLastReserveDate();
                    Long gecenSure = date.getTime() - lastReserveDate.getTime();
                    gecenSure = TimeUnit.MINUTES.convert(gecenSure,TimeUnit.MILLISECONDS);
                    Log.d("sure : ",gecenSure.toString());
                    if(masa.getReserve() && gecenSure<=60L) {
                        masaList.remove(masa);
                        i--;
                    }
                }
                if(masaList.size()>0) {
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setHasFixedSize(true);
                    MasaRecyclerViewAdapter adapter = new MasaRecyclerViewAdapter(masaList);
                    adapter.setOnItemClickListener(new MasaRecyclerViewAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {

                            databaseReference.child("masaList").child(position+"").child("reserve").setValue(true);
                            databaseReference.child("masaList").child(position+"").child("lastReserveDate").setValue(new Date());
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {
                            onItemClick(position,v);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                }
                else {
                    Toast.makeText(SecimActivity.this,"Boş Masa Yok",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        */
    }

    private void registerHandlers() {


    }

    private void loadData (String key) {
        databaseReference = databaseReference.child("kutuphaneler").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kutuphane kutuphane = (Kutuphane) snapshot.getValue(Kutuphane.class);
                List<Masa> masaList = kutuphane.getMasaList();
                List<Masa> adapterMasaList = new ArrayList<>();
                Iterator<Masa> iterator = masaList.iterator();
                while (iterator.hasNext()) {
                    Masa masa = iterator.next();
                    Long gecenSure = new Date().getTime() - masa.getLastReserveDate().getTime();
                    gecenSure = TimeUnit.MINUTES.convert(gecenSure,TimeUnit.MILLISECONDS);
                    if(!masa.getReserve() || gecenSure>60L) {
                        adapterMasaList.add(masa);
                    }
                }
                if(adapterMasaList.size()>0) {
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setHasFixedSize(true);
                    MasaRecyclerViewAdapter adapter = new MasaRecyclerViewAdapter(adapterMasaList);
                    adapter.setOnItemClickListener(new MasaRecyclerViewAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Masa masa = adapterMasaList.get(position);
                            masa.setReserve(true);
                            masa.setLastReserveDate(new Date());
                            int newPositon = masaList.indexOf(masa);
                            masaList.set(newPositon,masa);
                            databaseReference.child("masaList").setValue(masaList);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {
                            onItemClick(position,v);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                }
                else {
                    Snackbar.make(recyclerView,"Boş masa yok!",Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}