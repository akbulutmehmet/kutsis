package com.example.kutsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.ArrayList;

public class QRActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    public static final String MESSAGE_LIBRARYID = "com.example.kutsis.LIBRARYID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        handlePermissions(new String[]{
                Manifest.permission.CAMERA
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(QRActivity.this, SecimActivity.class);
                        intent.putExtra(MESSAGE_LIBRARYID,result.getText());
                        Log.d("deneme",result.getText());
                        startActivity(intent);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void handlePermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> notGrantedPerms = new ArrayList<>();
            for (String p : permissions) {
                if (this.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED)
                    notGrantedPerms.add(p);
            }
            permissions = notGrantedPerms.toArray(new String[0]);
            if (permissions != null && permissions.length > 0)
                this.requestPermissions(permissions, 701);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 701) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String p : permissions) {
                    String msg = "";
                    if (this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED)
                        msg = "Permission Granted for " + p;
                    else{
                        Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }


                }
            }
        }
    }
}