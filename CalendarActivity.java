package com.example.beta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarActivity  extends AppCompatActivity {
    private TextView titleText;
    private TextView areaText;
    private TextView todayText;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;


    private String area_name;
    private int area_id;
    private static final String PREF_KEY_id = "_id";
    private static final String PREF_KEY_TEXT = "_text";
    private static final String search_KEY_TEXT = "_search";

    //widget宣言
    private CalendarActivity.CallGet asyProgress;
    private static Integer[] day_id;
    private static Integer[] week_id;
    private static Integer[] genre_id;
    private EditText search;
    private Button search_bt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        titleText = findViewById(R.id.titleText);
        areaText = findViewById(R.id.areaText);
        todayText = findViewById(R.id.textToday);
        search = findViewById(R.id.search);
        search_bt=findViewById(R.id.search_buttom);

        calendarGridView = findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);
        titleText.setText(mCalendarAdapter.getTitle());
        todayText.setText(mCalendarAdapter.getTodayText());
        Intent intent = getIntent();

        // BaseAdapter を継承したadapterのインスタンスを生成
        asyProgress = new CalendarActivity.CallGet();
        asyProgress.excute();

        // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        area_id= preferences.getInt(PREF_KEY_id, 0);
        area_name= preferences.getString(PREF_KEY_TEXT, null);
        areaText.setText(area_name);

        setTodayText();

        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtext = search.getText().toString();
                // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // SharedPreferencesのEditorインスタンスに値を追加し、コミットする
                preferences.edit()
                        .putString(search_KEY_TEXT,searchtext)
                        .commit();
                Intent intent;
                intent= new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    //http通信
    private class CallGet {
        ExecutorService executorService;
        String res = "";

        public CallGet() {
            super();
            executorService = Executors.newSingleThreadExecutor();
        }

        private class TaskRun implements Runnable {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                String url = "http://ec2-3-211-42-248.compute-1.amazonaws.com/Getarea.php";
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);

                try{
                    Response response = call.execute();
                    res = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper())
                        .post(() -> onPostExcute());

            }
        }

        void excute() {
            onPreExecute();
            executorService.submit(new CalendarActivity.CallGet.TaskRun());
        }

        void onPreExecute(){

        }

        void onPostExcute (){

            try {
                JSONArray array = new JSONArray(res);
                 day_id=new Integer[array.length()];
                 week_id=new Integer[array.length()];
                genre_id=new Integer[array.length()];

                for(int i=0;i<array.length();i++)
                {
                    JSONObject object= array.getJSONObject(i);

                    day_id[i]  = object.getInt("day_id");
                    week_id[i]  = object.getInt("week");
                    genre_id[i]  = object.getInt("genre_id");
                }

            } catch (Exception e){
                Log.d("Hoge", e.getMessage());
            }
        }
    }

    static Integer[] getDay_id(){
        return (day_id);
    }
    static Integer[] getWeek(){
        return (week_id);
    }
    static Integer[] getGenre_id(){
        return (genre_id);
    }
    //オプションメニューの実装
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
//
    public String setTodayText(){
       todayText.setText(mCalendarAdapter.getTodayText());
        return null;
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
