package com.sumit.whatsappstatussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {
    private final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (hasAlreadyGrantedPermissions()) {
            Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // onClick handler for allow button
    public void askPermission(View view) {
        ActivityCompat.requestPermissions(PermissionActivity.this, permissions, STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && hasGrantedAllPermissions(grantResults)) {
                Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please grant the permissions to use the app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasGrantedAllPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAlreadyGrantedPermissions() {
        int readPermission = ContextCompat.checkSelfPermission(PermissionActivity.this, permissions[0]);
        int writePermission = ContextCompat.checkSelfPermission(PermissionActivity.this, permissions[1]);
        return (readPermission == PackageManager.PERMISSION_GRANTED &&
                writePermission == PackageManager.PERMISSION_GRANTED);
    }
}