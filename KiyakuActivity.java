package com.example.beta;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;



public class KiyakuActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiyaku);
        Button acceptbutton = findViewById(R.id.accept_bt);



        Intent intent = new Intent(getApplication(), MuniActivity.class);
        // lambda式
        acceptbutton.setOnClickListener(v -> startActivity(intent));
    }

}
