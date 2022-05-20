package com.example.kutsis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button emailVerificationBtn;
    private Button emailverificationSendMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        initComponents();
        registerHandlers();
    }

    private void registerHandlers() {
        emailVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.reload();
                updateUI(user);
            }
        });
        emailverificationSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.sendEmailVerification();
                Snackbar.make(view,"Email Gönderildi!",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if(user.isEmailVerified()) {
                Toast.makeText(EmailVerificationActivity.this, user.getEmail() + " email adresi doğrulandı", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SelectionActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }else {

                Toast.makeText(EmailVerificationActivity.this,"Epostanız doğrulanmadı!",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(getApplicationContext(),  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailVerificationBtn = findViewById(R.id.emailVerificationBtn);
        emailverificationSendMail = findViewById(R.id.emailVerificationSendMail);
    }
}
