package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kutsis.model.Masa;
import com.example.kutsis.model.User;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SureActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView textViewKalanSure, textViewKutuphane, textViewMasa;
    private Button btnSureyiUzat, btnMasayiBirak;
    private CountDownTimer timer;
    private boolean isTimerSet;
    private long kalanDakika,kalanSaniye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure);

        initComponents();
        registerHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            databaseReference.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User dbUser = (User) snapshot.getValue(User.class);
                    Long gecenSure = new Date().getTime() - dbUser.getLastReserveDate().getTime();
                    gecenSure = TimeUnit.MINUTES.convert(gecenSure,TimeUnit.MILLISECONDS);
                    if (dbUser.getReserve() && gecenSure<60L) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dbUser.getLastReserveDate());
                        calendar.add(Calendar.MINUTE,60);
                        Date expiredDate = calendar.getTime();
                        Date nowDate = new Date();
                        Long kalanSure = expiredDate.getTime() - nowDate.getTime();
                        kalanDakika = TimeUnit.MINUTES.convert(kalanSure,TimeUnit.MILLISECONDS);
                        kalanSaniye = TimeUnit.SECONDS.convert(kalanSure,TimeUnit.MILLISECONDS) - kalanDakika*60;
                        setTimer();

                        textViewKutuphane.setText(dbUser.getKutuphaneName());
                        textViewMasa.setText("Masa: " + dbUser.getMasaId());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Intent intent = new Intent(SureActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(SureActivity.this,  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        textViewKalanSure = findViewById(R.id.textViewKalanSureDynamic);
        textViewKutuphane = findViewById(R.id.textViewKutuphaneAdi);
        textViewMasa = findViewById(R.id.textViewMasaNumarasi);

        btnSureyiUzat = findViewById(R.id.buttonSureyiUzat);
        btnMasayiBirak = findViewById(R.id.buttonMasayiBirak);

        isTimerSet = false;
    }

    private void registerHandlers(){
        btnSureyiUzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // kullanıcının lastReserveDate'i update ediliyor.
                DatabaseReference userRef = databaseReference.child("users").child(mAuth.getCurrentUser().getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = (User) snapshot.getValue(User.class);
                        user.setLastReserveDate(new Date());
                        userRef.setValue(user);


                        // kullanıcının meşgul ettiği masanın son reserve date'i update ediliyor.
                        DatabaseReference tableRef = databaseReference.child("kutuphaneler").child(user.getKutuphaneKey()).child("masaList").child(String.valueOf(user.getMasaId()-1));
                        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Masa masa = (Masa) snapshot.getValue(Masa.class);
                                masa.setLastReserveDate(new Date());
                                tableRef.setValue(masa);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        kalanDakika=60;
                        kalanSaniye=0;
                        setTimer();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        btnMasayiBirak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userRef = databaseReference.child("users").child(mAuth.getCurrentUser().getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = (User) snapshot.getValue(User.class);
                        user.setReserve(false);
                        userRef.setValue(user);


                        DatabaseReference tableRef = databaseReference.child("kutuphaneler").child(user.getKutuphaneKey()).child("masaList").child(String.valueOf(user.getMasaId()-1));
                        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Masa masa = (Masa) snapshot.getValue(Masa.class);
                                masa.setReserve(false);
                                tableRef.setValue(masa);

                                Intent intent = new Intent(SureActivity.this, SelectionActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        kalanDakika=60;
                        kalanSaniye=0;
                        setTimer();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setTimer(){
        if(isTimerSet)
            timer.cancel();
        isTimerSet = true;
        timer = new CountDownTimer((kalanDakika*60+kalanSaniye)*1000,1000) {
            @Override
            public void onTick(long l)
            {
                kalanSaniye--;
                if(kalanSaniye == -1){
                    kalanSaniye = 59;
                    kalanDakika--;
                }
                String strKalanDakika = String.valueOf(kalanDakika);
                String strKalanSaniye = String.valueOf(kalanSaniye);
                if(kalanDakika < 10 )
                    strKalanDakika = "0" + strKalanDakika;
                if(kalanSaniye < 10 )
                    strKalanSaniye = "0" + strKalanSaniye;
                textViewKalanSure.setText(strKalanDakika+ " : " + strKalanSaniye);
            }

            @Override
            public void onFinish() {
                btnMasayiBirak.callOnClick();
            }
        }.start();
    }

}