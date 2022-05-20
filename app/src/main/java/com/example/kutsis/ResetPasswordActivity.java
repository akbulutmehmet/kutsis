package com.example.kutsis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputLayout emailWrapper;
    private Button resetPasswordBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        initComponents();
        registerHandlers();
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
    private void initComponents () {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.resetEmail);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
    }
    private void registerHandlers() {
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eposta = emailWrapper.getEditText().getText().toString().trim();
                Boolean epostaCheck = epostaKontrol(eposta);
                if(epostaCheck) {
                    mAuth.sendPasswordResetEmail(eposta);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                    builder.setMessage("Sıfırlama Maili Gönderildi!");

                    builder.setPositiveButton(R.string.login_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(ResetPasswordActivity.this, "Yönlendiriliyorsunuz", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }

        });
    }
}
