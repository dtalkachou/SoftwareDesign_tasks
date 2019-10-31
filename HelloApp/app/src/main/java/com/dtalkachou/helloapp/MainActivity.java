package com.dtalkachou.helloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    TelephonyManager telephonyManager;
    CoordinatorLayout coordinatorLayout;
    TextView versionName;
    TextView deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionName = findViewById(R.id.tvVersionName);
        versionName.setText(BuildConfig.VERSION_NAME);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = findViewById(R.id.tvDeviceId);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        displayDeviceId();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deviceId.setText(telephonyManager.getDeviceId());
                }
                else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            "To display Device ID, you must provide phone state permissions",
                            Snackbar.LENGTH_LONG);
                    snackbar.setAction("Accept", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayDeviceId();
                        }
                    });
                    snackbar.show();
                }
                return;
        }
    }

    private void displayDeviceId() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            deviceId.setText(telephonyManager.getDeviceId());
        } else {
            this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }
    }
}
