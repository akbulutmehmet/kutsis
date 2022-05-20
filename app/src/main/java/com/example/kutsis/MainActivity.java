package com.example.kutsis;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailWrapper,passwordWrapper;
    private Button loginBtn,signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        registerHandlers();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(this, currentUser.getEmail() + " giriş yaptı", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SelectionActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        }
    }

    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.emailWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupLoginBtn);
    }

    private void registerHandlers(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eposta = emailWrapper.getEditText().getText().toString();
                String sifre = passwordWrapper.getEditText().getText().toString();
                emailWrapper.setError(null);
                passwordWrapper.setError(null);
                if(eposta != null && sifre !=null && !eposta.equals("") && !sifre.equals("")) {

                    checkInputs(eposta, sifre);
                    mAuth.signInWithEmailAndPassword(eposta, sifre)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    }
                                    else {
                                        updateUI(null);
                                    }
                                }
                            });
                }
                else {
                    if(eposta.equals("") || eposta == null) {
                        emailWrapper.setError("Eposta adresinizi giriniz");
                    }
                    if(sifre.equals("") || sifre == null) {
                        passwordWrapper.setError("Eposta adresinizi giriniz");
                    }
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
    }
    
    private void checkInputs(String emailInput, String password){
        
        String email = emailInput;
        if(!email.endsWith("@gmail.com"))
            emailWrapper.setError("Hatalı Mail Adresi!");

        if(password.length() < 6)
            passwordWrapper.setError("Hatalı Şifre!");
    }
}