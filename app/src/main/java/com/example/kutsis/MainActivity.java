package com.example.kutsis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailWrapper,passwordWrapper;
    private Button loginBtn,signupBtn,resetPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initComponents();
        registerHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if(user.isEmailVerified()) {

                Toast.makeText(MainActivity.this, user.getEmail() + " giriş yaptı", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SelectionActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, EmailVerificationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }

        }
        else{
            Toast.makeText(MainActivity.this,  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.emailWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupLoginBtn);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);

    }

    private void registerHandlers(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eposta = emailWrapper.getEditText().getText().toString().trim();
                String sifre = passwordWrapper.getEditText().getText().toString().trim();
                Boolean epostaCheck = epostaKontrol(eposta);
                Boolean sifreCheck = sifreKontrol(sifre);
                if(epostaCheck && sifreCheck) {
                    mAuth.signInWithEmailAndPassword(eposta, sifre)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    }
                                    else {
                                        String exceptionMessage = task.getException().getLocalizedMessage();
                                        Toast.makeText(MainActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean epostaKontrol (String eposta) {
        if(TextUtils.isEmpty(eposta)) {
            emailWrapper.setError("Lütfen email giriniz");
            return false;
        }
        if(!eposta.endsWith("@gmail.com")) {
            emailWrapper.setError("Lütfen gmail kullanınız");
            return false;
        }
        emailWrapper.setError(null);
        return true;
    }
    private Boolean sifreKontrol (String sifre) {
        if(sifre.length() < 6) {
            passwordWrapper.setError("En az 6 karekter giriniz!");
            return false;
        }
        passwordWrapper.setError(null);
        return true;

    }
}