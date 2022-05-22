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
                        Long sure = expiredDate.getTime() - nowDate.getTime();
                        Log.d("sure : ",sure+"");

                        kalanDakika = TimeUnit.MINUTES.convert(sure,TimeUnit.MILLISECONDS);
                        sure = sure - 60*60*1000;
                        kalanSaniye = TimeUnit.SECONDS.convert(sure,TimeUnit.MILLISECONDS);
                        Log.d("dakika : ",kalanDakika+"");
                        Log.d("saniye : ",kalanSaniye+"");
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


    }

    private void registerHandlers(){
        btnSureyiUzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnMasayiBirak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setTimer(long dakika,long saniye){
        timer.cancel();
        kalanDakika = dakika;
        kalanSaniye = saniye;
        timer = new CountDownTimer(dakika,1000) {
            @Override
            public void onTick(long l) {
                kalanSaniye--;
                if(kalanSaniye == -1){
                    kalanSaniye = 59;
                    kalanDakika--;
                }
            }

            @Override
            public void onFinish() {
                btnMasayiBirak.callOnClick();
            }
        }.start();
    }

}
