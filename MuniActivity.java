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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MuniActivity<context> extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private static final String PREF_KEY_id = "_id";
    private static final String PREF_KEY_TEXT = "_text";


    //widget宣言
    private CallGet asyProgress;



   private static String[] zoneName = {
          //  "香美市",
         //   "香南市",
          //  "南国市",
          //  "高知市",
          //  "初期化",
    };
    private static Integer[]  zoneID = {
      //      1,
        //    2,
         //   3,
         //   4,
           // 0,
    };

    private ListView listView;
    private Context applicationContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muni);


        // ListViewのインスタンスを生成
        listView = findViewById(R.id.list_view);
        applicationContext =this.getApplicationContext();

        // BaseAdapter を継承したadapterのインスタンスを生成
        asyProgress = new CallGet();
        asyProgress.excute();

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
                this.getApplicationContext(), AreaActivity.class);

        // clickされたpositionのtextとphotoのID
        String name = zoneName[position];
        int Id = zoneID[position];

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

                String url = "http://ec2-3-211-42-248.compute-1.amazonaws.com/Getmuni.php";
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
            executorService.submit(new TaskRun());
        }

        void onPreExecute(){

        }

        void onPostExcute (){

            try {
                JSONArray array = new JSONArray(res);
                String muniname[]=new String[array.length()];
                Integer muniID[]=new Integer[array.length()];

                for(int i=0;i<array.length();i++)
                {
                    JSONObject object= array.getJSONObject(i);
                    muniname[i] = object.getString("mun_name");
                    muniID[i]  = object.getInt("mun_id");
                }
                zoneName = muniname;
                zoneID = muniID;
                BaseAdapter adapter = new ListViewAdapter(applicationContext,
                        R.layout.arealist, muniname);

                listView.setAdapter(adapter);

                // クリックリスナーをセット
                listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);


            } catch (Exception e){
                Log.d("Hoge", e.getMessage());
            }

        }



    }

}