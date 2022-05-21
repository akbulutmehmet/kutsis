package com.example.kutsis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kutsis.model.Kutuphane;
import com.example.kutsis.model.Masa;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectionActivity extends AppCompatActivity {

   private FloatingActionButton fab;
   private FirebaseAuth mAuth;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        initComponents();
        registerHandlers();
       // List<Masa> masaList = new ArrayList<>();
       // masaList.add(new Masa(1L,true,new Date()));
       // masaList.add(new Masa(2L,true,new Date()));
       // masaList.add(new Masa(3L,true,new Date()));
       // Kutuphane kutuphane = new Kutuphane(1L,masaList,"Mehmet Kütüphanesi");
       // databaseReference.child("kutuphaneler").push().setValue(kutuphane);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
        builder.setMessage(R.string.exitQuestion);
        builder.setNegativeButton(R.string.no, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);

            }
        });
        builder.show();
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(SelectionActivity.this,  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }
    private void initComponents() {
        fab = findViewById(R.id.floatingButton);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void registerHandlers() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this, QRActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.logoutMenu:
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                    builder.setMessage(R.string.logoutString);
                    builder.setNegativeButton(R.string.no,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SelectionActivity.this,R.string.logoutCancel,Toast.LENGTH_SHORT).show();
                    }
                });
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(SelectionActivity.this, R.string.logoutOk, Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                    break;
                case R.id.exitApp:
                    onBackPressed();
                    break;
            }
            return super.onOptionsItemSelected(item);
    }
}