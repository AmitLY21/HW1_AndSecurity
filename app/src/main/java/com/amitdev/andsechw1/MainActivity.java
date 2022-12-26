package com.amitdev.andsechw1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout username;
    private TextInputLayout password;
    private Button btnNext;
    private Button btnCamera;
    private CheckBox cboxNotARobot;
    private boolean tookPhoto = false;
    private Bitmap imageBitmap;

    private final static int GET_IMAGE_CAPTURE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUsername() && checkPassword()) {
                    if (isConnected() && tookPhoto && approvedVolume() && isAirplaneModeOn()) {
                        movePage();
                    }
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, GET_IMAGE_CAPTURE);
            }
        });
    }

    private boolean approvedVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumePercentage = 100 * currentVolume / maxVolume;
        if (currentVolumePercentage <= 50)
            return true;
        return false;
    }


    //return true if enabled
    private boolean isAirplaneModeOn() {

        return Settings.System.getInt(this.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    }

    private boolean isConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    private void movePage() {
        Intent i = new Intent(getApplicationContext(), DestinationActivity.class);
        i.putExtra("BitmapImage", this.imageBitmap);
        startActivity(i);
    }

    private boolean checkPassword() {
        return Validator.PatternPassword(this.password.getEditText().getText().toString());
    }

    private boolean checkUsername() {
        return Validator.PatternUsername(this.username.getEditText().getText().toString());
    }

    private void findViews() {
        btnNext = findViewById(R.id.btnNext);
        btnCamera = findViewById(R.id.btnCamera);
        username = findViewById(R.id.edtUsername);
        password = findViewById(R.id.edtPassword);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.imageBitmap = (Bitmap) extras.get("data");
            this.tookPhoto = true;
        }
    }

}