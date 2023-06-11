package com.example.loginactivity.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginactivity.R;
import com.example.loginactivity.adapter.ListViewAdapter;
import com.example.loginactivity.databinding.FragmentHomeBinding;
import com.example.loginactivity.domain.Video;
import com.example.loginactivity.util.VideoOpenHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {
    private VideoOpenHelper helper;
    private FragmentHomeBinding binding;
    private List<Video> listitem;
    private ListView listView;
    private static SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = binding.listContainer;

        //加载SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("config", MODE_PRIVATE);

        //从数据库获取资源
        //1.创建/打开数据库，数据库名为NewsTable.db
        if (helper == null) {
            helper = VideoOpenHelper.getInstance(getActivity());
            helper.openReadLink();
            helper.openWriteLink();
        }

        if (listitem == null) {
            listitem = helper.queryAll();
        }

        //把信息加入到Adapter
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.news_item, listitem, Configuration.ORIENTATION_LANDSCAPE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("news", listitem.get(position));
            startActivityForResult(intent, 1);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        //关闭数据库连接
        helper.closeLink();
    }

    //当通过startActivityForResult函数启动其他的安卓组件
    //该组件发生返回动作时，就会调用此函数
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.v("reStart", "requestCode" + requestCode + "resultCode" + resultCode);
        //判断是否匹配成功
        //requestCoded的值与resultCode的值不需要相等，
        //但requestCoded的值需要与函数参数的int requestCode值相等
        //resultCode的值需要与函数参数的int resultCode值相等
        if (requestCode == resultCode) {
            if (intent.getBooleanExtra("isOverTime", false)) {
                Log.v("", "OverTime");
                Bundle extras = intent.getExtras();
                Video videoBack = (Video) extras.get("news");
                Toast.makeText(getActivity(), "已阅读新闻:" + videoBack.getTitle(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < listitem.size(); i++) {
                    System.out.println("循环" + i + "     id:" + listitem.get(i).getId() + "title:" + listitem.get(i).getTitle());
                    if (listitem.get(i).getId().equals(videoBack.getId())) {
                        listitem.remove(listitem.get(i));
                        //改颜色
                        int grey = getResources().getColor(R.color.md_blue_grey_100);
                        videoBack.setIsRead(grey);
                        listitem.add(videoBack);
                        //SharedPreferences保存已看过的新闻ID
                        //id  -->  颜色
                        String readId = sharedPreferences.getString("readId", "");
                        System.out.println("readId:" + readId);
                        HashSet<String> array = new HashSet<>();
                        if (!readId.equals("")) {
                            for (String s : readId.split(" ")) {
                                array.add(s);
                            }
                        }
                        array.add(String.valueOf(videoBack.getId()));
                        StringBuilder sb = new StringBuilder();
                        Iterator<String> iterator = array.iterator();
                        while (iterator.hasNext()) {
                            sb.append(iterator.next()).append(" ");
                        }
                        array.add(String.valueOf(videoBack.getId()));
                        String writeId = sb.toString();
                        System.out.println("writeId:" + writeId);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("readId", writeId);
                        edit.commit();
                        break;
                    }
                }
                Collections.sort(listitem);
                ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.news_item, listitem, Configuration.ORIENTATION_LANDSCAPE);
                listView.setAdapter(adapter);
            }
        }
//        reload();
    }
}