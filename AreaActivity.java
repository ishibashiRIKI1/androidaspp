package com.example.beta;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AreaActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private static final String PREF_KEY_id = "_id";
    private static final String PREF_KEY_TEXT = "_text";

    //widget宣言
    private AreaActivity.CallGet asyProgress;


    private static String[] zoneName = {
         //  "宝町",
        //    "西本町",
           // "東本町"

    };
    private static Integer[]  zoneID = {
        //   1,
         //   2,
          //  3,
    };

    private ListView listView;
    private Context applicationContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        // ListViewのインスタンスを生成
        listView = findViewById(R.id.list_view);

        applicationContext =this.getApplicationContext();

        // BaseAdapter を継承したadapterのインスタンスを生成
        asyProgress = new AreaActivity.CallGet();
        asyProgress.excute();


        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new ListViewAdapter(this.getApplicationContext(),
                R.layout.arealist, zoneName);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);

        // クリックリスナーをセット
        listView.setOnItemClickListener(this);

    }

    @Override
    //クリックされたら
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        Intent intent = new Intent(
                this.getApplicationContext(), CalendarActivity.class);

        // clickされたpositionのtextとphotoのID
        String name = zoneName[position];
        Integer Id = zoneID[position];

        // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // SharedPreferencesのEditorインスタンスに値を追加し、コミットする
        preferences.edit()
                .putInt(PREF_KEY_id, Id)
                .putString(PREF_KEY_TEXT, name)
                .commit();

        // インテントにセット
        intent.putExtra("Name", name);
        intent.putExtra("ID", Id);

        // SubActivityへ遷移
        startActivity(intent);
    }
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
            executorService.submit(new AreaActivity.CallGet.TaskRun());
        }

        void onPreExecute(){

        }

        void onPostExcute (){

            try {
                JSONArray array = new JSONArray(res);
                String areaname[]=new String[array.length()];
                Integer areaID[]=new Integer[array.length()];

                for(int i=0;i<array.length();i++)
                {
                    JSONObject object= array.getJSONObject(i);
                    areaname[i] = object.getString("area_name");
                    areaID[i]  = object.getInt("area_id");
                }
                zoneName = areaname;
                zoneID = areaID;

                BaseAdapter adapter = new ListViewAdapter(applicationContext,
                        R.layout.arealist, areaname);

                listView.setAdapter(adapter);

                // クリックリスナーをセット
                listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);


            } catch (Exception e){
                Log.d("Hoge", e.getMessage());
            }

        }



    }
}

