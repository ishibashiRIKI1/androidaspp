package com.example.beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class search_ListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView hinmokutext;
        TextView janretext;
        TextView hosokutext;

    }

    private final LayoutInflater inflater;
    private final int itemLayoutId;
    private final String[] Hinmoku;
    private final String[] Janre;
    private final String[] Hosoku;


    search_ListViewAdapter(Context context, int itemLayoutId,
                    String[] hinmoku,String[] janre,String[] hosoku) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.Hinmoku = hinmoku;
        this.Janre = janre;
        this.Hosoku = hosoku;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.hinmokutext = convertView.findViewById(R.id.hinnmokuText);
            holder.janretext = convertView.findViewById(R.id.ganreText);
            holder.hosokutext = convertView.findViewById(R.id.hosokuText);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.hinmokutext.setText(Hinmoku[position]);
        holder.janretext.setText(Janre[position]);
        holder.hosokutext.setText(Hosoku[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return Hinmoku.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}