package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailWrapper,passwordWrapper;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();
        registerHandlers();
    }

    private void registerHandlers() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eposta = emailWrapper.getEditText().getText().toString();
                String sifre = passwordWrapper.getEditText().getText().toString();
                checkInputs(eposta, sifre);


                mAuth.createUserWithEmailAndPassword(eposta, sifre)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, mAuth.getCurrentUser().getEmail() + " kullanıcısı kayıt yaptı.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Kayıt başarısız.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            private void checkInputs(String emailInput, String passwordInput) {
                String email = emailInput;
                if(!email.endsWith("@gmail.com"))
                    emailWrapper.setError("Hatalı Mail Adresi");

                String password = passwordInput;
                if(password.length() < 6)
                    passwordWrapper.setError("Hatalı Şifre!");
            }
        });
    }

    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.mailWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        registerBtn = findViewById(R.id.btnRegister);
    }
}