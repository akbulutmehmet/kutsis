package com.example.kutsis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecimActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secim);

        Intent intent = getIntent();
        String message = intent.getStringExtra(QRActivity.MESSAGE_LIBRARYID);

        textView=findViewById(R.id.textView);
        textView.setText(message);

    }
}