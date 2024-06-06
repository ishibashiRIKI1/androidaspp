package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import java.util.Calendar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.DialogFragment;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Locale;



public class AlarmActivity extends FragmentActivity
        implements TimePickerDialog.OnTimeSetListener {

    private AlarmManager am;
    private PendingIntent pending;
    private final int requestCode = 1;
    private TextView text_timepick;
    private int Hour = 0;
    private int Minute = 0;
    private boolean today = true;
    private boolean yesterday = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        text_timepick = findViewById(R.id.text_timepick);

        Button buttonStart = this.findViewById(R.id.button_start);
        buttonStart.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            Switch Switchtoday = findViewById(R.id.switchtoday);
            Switchtoday.setOnCheckedChangeListener(new onCheckedChageListener());


            // 時間設定
            calendar.set(Calendar.HOUR_OF_DAY, Hour);
            calendar.set(Calendar.MINUTE, Minute);
            calendar.set(Calendar.SECOND, 0);



            Intent intent = new Intent(getApplicationContext(), AlarmNotification.class);
            intent.putExtra("RequestCode", requestCode);

            pending = PendingIntent.getBroadcast(
                    getApplicationContext(), requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // アラームをセットする
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            if(am != null && today || yesterday){
                am.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pending);

                // トーストで設定されたことをを表示
                Toast.makeText(getApplicationContext(),
                        "設定しました", Toast.LENGTH_SHORT).show();

                Log.d("debug", "start");
            }else if(am != null ) {
                Toast.makeText(getApplicationContext(),
                        "機能がオフになっています", Toast.LENGTH_SHORT).show();

            }
        });

        //ホームボタンが押されたら
        findViewById(R.id.HomeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Hour = hourOfDay;
        Minute = minute;
        // 通知予定時間表示
        String str = String.format(Locale.JAPAN, "%02d:%02d", Hour, Minute);
        text_timepick.setText(str);


    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePick();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }
    //on off
    public class onCheckedChageListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                today = true;
            } else {
                today = false;
            }
        }
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