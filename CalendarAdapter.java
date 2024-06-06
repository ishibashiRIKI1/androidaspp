package com.example.beta;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter  extends BaseAdapter {
    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;
    private String TodayText="今日はゴミの日ではありません";
    private TextView todayText;


    private CalendarActivity CA;

    private Integer[]  gomiweek= {
            0,0,1,2,3,4,0,3,
    };

    private Integer[]  gomidayofweek= {
           2,5,6,6,6,6,3,3,
    };

    private Integer[]  gomijan= {
            1,1,2,3,4,5,6,7,
    };
    Calendar calendar = Calendar.getInstance();

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public TextView dateText;
        public ImageView dateImage;
    }

    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
    }

    @Override
    public int getCount() {
        return dateArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell, null);
            holder = new ViewHolder();
            holder.dateText = convertView.findViewById(R.id.dateText);
            holder.dateImage = convertView.findViewById(R.id.dateImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth()/7 - (int)dp, (parent.getHeight() - (int)dp * mDateManager.getWeeks() ) / mDateManager.getWeeks()-35);
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));
        holder.dateImage.setImageResource(R.drawable.emptyimg);

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray.get(position))){
            convertView.setBackgroundColor(Color.parseColor("#FFFAF3"));
        }else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        //日曜日を赤、土曜日を青に
        int colorId;
        //曜日
        switch (mDateManager.getDayOfWeek(dateArray.get(position))){
            case 1:
                colorId = Color.RED;
                break;

            case 7:
                colorId = Color.BLUE;
                break;

            default:
                colorId = Color.BLACK;
                break;
        }

     //   gomidayofweek = CA.getDay_id();
     //  gomiweek = CA.getWeek();
        //  gomijan=CA.getGenre_id();


        //ゴミ出しの画像

        for(int i=0;i<gomiweek.length;i++){
            if(mDateManager.getDayOfWeek(dateArray.get(position))==gomidayofweek[i]){
                if(mDateManager.getWeek(dateArray.get(position)) == gomiweek[i] || 0 == gomiweek[i]) {
                    switch (gomijan[i]){
                        case 1:
                            holder.dateImage.setImageResource(R.drawable.moe);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日は燃えるごみの日です";
                        }
                                break;
                        case 2:
                            holder.dateImage.setImageResource(R.drawable.kankinzoku);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日は缶・金属の日です";
                            }
                            break;
                        case 3:
                            holder.dateImage.setImageResource(R.drawable.bin);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日はビンの日です";
                            }
                            break;
                        case 4:
                            holder.dateImage.setImageResource(R.drawable.sonotanohunen);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日はその他不燃物の日です";
                            }
                            break;
                        case 5:
                            holder.dateImage.setImageResource(R.drawable.pet);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日はペットボトルの日です";
                            }
                            break;
                        case 6:
                            holder.dateImage.setImageResource(R.drawable.notmoe);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日は燃えないごみの日です";
                            }
                            break;
                        case 7:
                            holder.dateImage.setImageResource(R.drawable.kamirui);
                            if(mDateManager.getDay(dateArray.get(position)) == calendar.get(Calendar.DATE) ){
                                TodayText = "今日は紙・衣類の日です";
                            }
                            break;
                        default:
                            break;


                    }
                }
            }

        }


        holder.dateText.setTextColor(colorId);

        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    //表示月を取得
    public String getTitle(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }
    //今日のごみを取得
    public String getTodayText(){
        return TodayText;
    }



}
