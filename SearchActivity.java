package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    //Widget宣言
    private CallPost asyProgress;
    private String searchtext;

    private static final String search_KEY_TEXT = "_search";




    private static String[] hinmoku = {
      /*      "アイスノン",
            "アイスクリーム容器（プラスチック製）",
            "アイロン",
            "アイロン台",
            "空き缶（ｱﾙﾐ･ｽﾁｰﾙ）（飲み物の缶）",
            "空き缶（缶詰、お菓子やお茶などの入れ物）",
            "空きびん（食品用・飲み薬用） ",
            "空きびん（化粧品用・劇薬剤用） ",
            "空きびん（割れたもの）",
            "アコーディオンカーテン",
            "油（廃食油を固形化及び紙,布に浸したもの）",
            "油紙",
            "雨がっぱ",
            "雨戸（金属製、木製）",
            "雨戸（業者が取り外したもの）",
            "網",*/

    };
    private static String[] janre = {
        /*    "燃えるごみ",
            "容器包装プラスチック ",
            "その他の不燃物",
            "その他の不燃物、粗大ごみ",
            "金属類（飲料用の缶）",
            "金属類（その他）",
            "ビン類",
            "その他の不燃物",
            "その他の不燃物",
            "粗大ごみ",
            "燃えるごみ",
            "燃えるごみ",
            "その他の不燃物",
            "粗大ごみ",
            "収集できません",
            "金属類（その他）、その他の不燃物、粗大ごみ",*/

    };
    private static  String[] hosoku = {
       /*     "",
            "中を水洗いしてから出してください",
            "",
            "指定袋に入るものはその他の不燃物、入らないものは粗大ごみ",
            "中を水洗いしてから出してください",
            "中を水洗いしてから出してください",
            "中を水洗いしてから出してください　割れているものは新聞等で包みその他の不燃物へ",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "事業所ごみとして処理（廃棄物処理業者へ）",
            "金属製は金属類（その他）、指定袋に入らないものは粗大ごみ",*/
    };


    private static ListView slistView;
    private static Context applicationContext;
    static EditText scantext;
    private Button buttonpost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        scantext = findViewById(R.id.seach);
        buttonpost = findViewById(R.id.search_button);

        // PreferenceManagerを介してアプリのデフォルトのSharedPreferencesインスタンスを取得する
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        searchtext= preferences.getString(search_KEY_TEXT, null);
        scantext.setText(searchtext);

        // ListViewのインスタンスを生成
        slistView = findViewById(R.id.searchlist);
        applicationContext =this.getApplicationContext();

        asyProgress = new CallPost();
        asyProgress.excute();

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に
        // inflate するためにadapterに引数として渡す
        BaseAdapter adapter = new search_ListViewAdapter(this.getApplicationContext(),
                R.layout.gomilist, hinmoku,janre,hosoku);

        // ListViewにadapterをセット
        slistView.setAdapter(adapter);

        // クリックリスナーをセット
        slistView.setOnItemClickListener(this);

        //ホームボタン
        findViewById(R.id.HomeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        buttonpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyProgress = new CallPost();
                asyProgress.excute();
            }
        });

    }

    //http通信
    private static class CallPost {
        ExecutorService executorService;
        String res = "";

        public CallPost() {
            super();
            executorService = Executors.newSingleThreadExecutor();
        }

        private class TaskRun implements Runnable {

            @Override
            public void run() {
                String search = scantext.getText().toString() + "%";


                // HTTP 処理用オプジェクト
                OkHttpClient client = new OkHttpClient();

                // POST 用 FormBody の内容の作成
                FormBody.Builder formbodyBuilder = new FormBody.Builder();
                formbodyBuilder.add("item" , search);


                // 送信用ユニットの作成
                FormBody formbody = formbodyBuilder.build();

                // 送信用のデータを作成
                Request.Builder requestBuilder = new Request.Builder();
                String url = "http://ec2-3-211-42-248.compute-1.amazonaws.com/search.php";
                requestBuilder.url(url);
                requestBuilder.post(formbody);
                Request request = requestBuilder.build();

                // 受信用のオブジェクトの準備
                Call call = client.newCall(request);
                // 送信と受信
                try {
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

        void onPostExcute(){
            try {
                JSONArray array = new JSONArray(res);
                String item[]=new String[array.length()];
                String sup[]=new String[array.length()];
                String exp[]=new String[array.length()];

                for(int i=0;i<array.length();i++)
                {
                    JSONObject object= array.getJSONObject(i);
                    item[i] = object.getString("item");
                    sup[i] = object.getString("sup");
                    exp[i] = object.getString("exp");
                }
                hinmoku = item;
                janre = sup;
                hosoku = exp;
                //リストビューへデータ追加
                BaseAdapter adapter = new search_ListViewAdapter(applicationContext,
                        R.layout.gomilist, hinmoku,janre,hosoku);

                slistView.setAdapter(adapter);

                // クリックリスナーをセット
                slistView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

            } catch (Exception e){
                Log.d("Hoge", e.getMessage());
            }
        }

    }

    @Override
    //クリックされたら
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {
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