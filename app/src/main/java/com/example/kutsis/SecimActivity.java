package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kutsis.adapter.MasaRecyclerViewAdapter;
import com.example.kutsis.model.Kutuphane;
import com.example.kutsis.model.Masa;
import com.example.kutsis.model.User;
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

import co.dift.ui.SwipeToAction;

public class SecimActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private SwipeToAction swipeToAction;
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
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(llm);
                recyclerView.setHasFixedSize(true);
                MasaRecyclerViewAdapter adapter = new MasaRecyclerViewAdapter(adapterMasaList);

                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Masa>() {
                    @Override
                    public boolean swipeLeft(Masa masa) {
                        Vibrator vibrator = (Vibrator) SecimActivity.this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                        // Oreo sonrası yeni hali
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        else
                            vibrator.vibrate(500);
                        masa.setReserve(true);
                        masa.setLastReserveDate(new Date());
                        int newPositon = masaList.indexOf(masa);
                        masaList.set(newPositon,masa);
                        databaseReference.child("masaList").setValue(masaList);
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = (User) snapshot.getValue(User.class);
                                user.setKutuphaneKey(key);
                                user.setKutuphaneName(kutuphane.getKutuphaneName());
                                user.setMasaId(masa.getId());
                                user.setReserve(true);
                                user.setLastReserveDate(masa.getLastReserveDate());
                                userRef.setValue(user);
                                Intent intent = new Intent(SecimActivity.this,SureActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return true;
                    }
                    @Override
                    public boolean swipeRight(Masa masa) {
                        return swipeLeft(masa);
                    }
                    @Override
                    public void onClick(Masa masa) {
                        swipeLeft(masa);
                    }
                    @Override
                    public void onLongClick(Masa masa) {
                        swipeLeft(masa);
                    }
                });

                if(adapterMasaList.size()==0) {
                    Snackbar.make(recyclerView,"Boş masa yok!",Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}