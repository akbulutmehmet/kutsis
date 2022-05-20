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
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectionActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        initComponents();
        registerHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
            }
        });
        builder.show();
    }

    private void initComponents() {
        fab = findViewById(R.id.floatingButton);
        mAuth = FirebaseAuth.getInstance();
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
            }
            return super.onOptionsItemSelected(item);
    }
}