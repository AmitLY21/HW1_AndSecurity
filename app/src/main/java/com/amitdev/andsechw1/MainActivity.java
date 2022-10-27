package com.amitdev.andsechw1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

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
                    Toast.makeText(getApplicationContext(), "Username and Password are VALID!", Toast.LENGTH_SHORT).show();
                    if (cboxNotARobot.isChecked() && tookPhoto) {
                        movePage();
                    } else
                        Toast.makeText(getApplicationContext(), "Checkbox not checked or You didn't take a photo", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Username or Password are NOT VALID!", Toast.LENGTH_SHORT).show();
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

    private void movePage() {
        Intent i = new Intent(getApplicationContext(), DestinationActivity.class);
        i.putExtra("BitmapImage",this.imageBitmap);
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
        cboxNotARobot = findViewById(R.id.cboxNotARobot);
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