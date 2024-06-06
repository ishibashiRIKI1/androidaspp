package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_KEY_id = "_id";
    private int area_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       area_id= preferences.getInt(PREF_KEY_id, 0);

        if (area_id == 0 ){
            Intent intent = new Intent(getApplication(), KiyakuActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplication(), CalendarActivity.class);
            startActivity(intent);
        }
    }
}
