package com.example.kutsis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kutsis.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView textAd,textSoyad,textEmail,textKayitTarihi;
    private Button btnCikisYap;
    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        textAd = findViewById(R.id.textAd);
        textSoyad = findViewById(R.id.textSoyad);
        textEmail = findViewById(R.id.textEmail);
        textKayitTarihi = findViewById(R.id.textKayitTarihi);
        btnCikisYap = findViewById(R.id.btnCikisYap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponents();
        registerHandlers();
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dbUser = (User) snapshot.getValue(User.class);
                if(dbUser != null)  {
                    textAd.setText(dbUser.getName());
                    textSoyad.setText(dbUser.getSurName());
                    textEmail.setText(dbUser.getEmail());
                    SimpleDateFormat simpleDateFormat = null;
                    try {
                        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
                        textKayitTarihi.setText(simpleDateFormat.format(dbUser.getRegisterDate()));
                    } catch (Exception exception) {
                        exception.getMessage();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            Toast.makeText(ProfileActivity.this,  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }


    private void registerHandlers() {
        btnCikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage(R.string.logoutString);
                builder.setNegativeButton(R.string.no,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfileActivity.this,R.string.logoutCancel,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfileActivity.this, R.string.logoutOk, Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

    }
}
