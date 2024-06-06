package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class JichitaiActivity extends AppCompatActivity {

    private int area_id;
    private static final String PREF_KEY_id = "_id";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jichitai);

        // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        area_id= preferences.getInt(PREF_KEY_id, 0);


        findViewById(R.id.HomeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    //オプションメニューの実装
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // オプションメニューの選択時処理
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId) {
            case R.id.notice_menu:
                intent= new Intent(getApplication(), AlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.search_menu:
                intent= new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.area_menu:
                intent= new Intent(getApplication(), MuniActivity.class);
                startActivity(intent);
                break;
            case R.id.contactMun_menu:
                intent = new Intent(getApplication(), JichitaiActivity.class);
                startActivity(intent);
                break;
            case R.id.contactUs_menu:
                intent = new Intent(getApplication(), FormActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}

