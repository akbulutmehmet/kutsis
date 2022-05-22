package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kutsis.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailWrapper,passwordWrapper,nameWrapper,surnameWrapper;
    private Button registerBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
            if(user.isEmailVerified()) {
                Toast.makeText(RegisterActivity.this, user.getEmail() + " email adresi doğrulandı", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),  "Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
        }
    }
    private void registerHandlers() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eposta = emailWrapper.getEditText().getText().toString().trim();
                String sifre = passwordWrapper.getEditText().getText().toString().trim();
                String name = nameWrapper.getEditText().getText().toString().trim();
                String surname = surnameWrapper.getEditText().getText().toString().trim();
                Boolean epostaCheck = epostaKontrol(eposta);
                Boolean sifreCheck = sifreKontrol(sifre);

                if(epostaCheck && sifreCheck && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)) {
                    mAuth.createUserWithEmailAndPassword(eposta, sifre)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, mAuth.getCurrentUser().getEmail() + " kullanıcısı kayıt yaptı.", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.sendEmailVerification();
                                        User userRealTime = new User(eposta,name,surname);
                                        databaseReference.child("users").child(user.getUid()).setValue(userRealTime);
                                        Intent intent = new Intent(RegisterActivity.this, EmailVerificationActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        String exceptionMessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


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
    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.mailWrapper);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        nameWrapper = findViewById(R.id.nameWrapper);
        surnameWrapper = findViewById(R.id.surnameWrapper);
        registerBtn = findViewById(R.id.btnRegister);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}