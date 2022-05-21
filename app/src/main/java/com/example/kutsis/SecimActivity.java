package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kutsis.adapter.*;
import com.example.kutsis.model.Kutuphane;
import com.example.kutsis.model.Masa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SecimActivity extends AppCompatActivity {

    private  TextView textView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private void initComponents() {
        //textView=findViewById(R.id.textView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView = findViewById(R.id.masaRecyclerView);
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

        databaseReference = databaseReference.child("kutuphaneler").child(message);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kutuphane kutuphane = (Kutuphane) snapshot.getValue(Kutuphane.class);
                List<Masa> masaList = kutuphane.getMasaList();
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(llm);
                recyclerView.setHasFixedSize(true);
                MasaRecyclerViewAdapter adapter = new MasaRecyclerViewAdapter(masaList);
                adapter.setOnItemClickListener(new MasaRecyclerViewAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Toast.makeText(SecimActivity.this,position+"",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {
                        Toast.makeText(SecimActivity.this,position+"",Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void registerHandlers() {


    }


}