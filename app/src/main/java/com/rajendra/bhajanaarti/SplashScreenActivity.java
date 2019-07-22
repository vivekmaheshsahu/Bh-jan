package com.rajendra.bhajanaarti;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.rajendra.bhajanaarti.activities.HomeActivity;
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils;
import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends Activity {

    Handler handler = new Handler();
    private final int MULTIPLE_PERMISSIONS = 10;
    private String[] permissions = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.VIBRATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(r, 2000);
    }

    Runnable r = new Runnable() {
        public void run() {
            if (checkPermissions()) {
                //  permissions  granted.
                determineNavigation();
            }
        }
    };

    private void determineNavigation(){
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }

    private boolean checkPermissions(){
        int result;
        List<String> listPermissionsNeeded = new ArrayList<String>();
        for (String p : permissions){
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded.add(p);
        }
        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MULTIPLE_PERMISSIONS:
                boolean permissionDenied = false;
                for (int granted : grantResults) {
                    if (granted == PackageManager.PERMISSION_DENIED) {
                        permissionDenied = true;
                        UserInterfaceUtils.showToast(this, getString(R.string.please_grant_all_permissions));
                        break;
                    }
                }
                if (!permissionDenied)
                    startActivity(new Intent(this, HomeActivity.class));
                this.finish();

                break;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        try {
            finish();
            if (handler != null) {
                handler.removeMessages(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
