package com.example.loginactivity.adapter;
//横屏（2）

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginactivity.R;
import com.example.loginactivity.domain.Video;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Video> {

    private Context context;
    private int resourceId;
    private List<Video> listitem;

    private int orientationLandscape;

    public ListViewAdapter(Context context, int resourceId, List<Video> listitem, int orientationLandscape) {
        super(context, resourceId, listitem);
        this.context = context;
        this.resourceId = resourceId;
        this.listitem = listitem;
        this.orientationLandscape = orientationLandscape;
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Video getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.iv_list1);
        TextView title = convertView.findViewById(R.id.tv_list1_title);
        TextView content = convertView.findViewById(R.id.tv_list1_content);
        TextView from = convertView.findViewById(R.id.tv_list1_from);
        TextView time = convertView.findViewById(R.id.tv_list1_time);
        Video video = listitem.get(position);
        imageView.setImageResource(video.getImage());
        title.setText(video.getTitle());
        content.setText(video.getContent());
        from.setText(video.getSource());
        time.setText(video.getTime());
        title.setTextColor(video.getIsRead());
        content.setTextColor(video.getIsRead());
        from.setTextColor(video.getIsRead());
        time.setTextColor(video.getIsRead());

        //横屏设置
        if (orientationLandscape == ORIENTATION_LANDSCAPE) {
            //缩小图片
            //设置图片的位置
            ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
            margin9.setMargins(5, 5, 5, 5);//在左边距400像素，顶边距10像素的位置显示图片
            LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(margin9);
            layoutParams9.height = 200;//设置图片的高度
            layoutParams9.width = 200; //设置图片的宽度
            imageView.setLayoutParams(layoutParams9);
            //缩小字号
            title.setTextSize(19);
            content.setTextSize(12);
            from.setTextSize(12);
            time.setTextSize(12);
        }
        return convertView;
    }
}
