package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.loginactivity.BroadcastReceiver.MyReceiver;
import com.example.loginactivity.databinding.ActivityMainBinding;
import com.example.loginactivity.domain.Star;
import com.example.loginactivity.domain.User;
import com.example.loginactivity.domain.Video;
import com.example.loginactivity.util.StarOpenHelper;
import com.example.loginactivity.util.UserOpenHelper;
import com.example.loginactivity.util.VideoOpenHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private VideoOpenHelper videoHelper;
    private UserOpenHelper userHelper;
    private StarOpenHelper starHelper;
    private static SharedPreferences sharedPreferences;
    private Long uid;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "title:如何破解迷茫，找到生活方向？");
        Log.i(TAG, "content:集合了年轻人最关注的难题，从克服自卑、消除内耗，到如何戒掉手机，爱上学习，应有尽有，曾帮助了一些朋友，今天整理出来给大家，方便按需查找，喜欢的话不妨三连起来，防止下次要看找不到啦！\n" +
                "大家好，先感谢你们的三连支持呀！\n" +
                "\n" +
                "我把这一年做的干货视频基本都整理出来了，覆盖范围较广，希望能解决你们的烦恼，成为一个比较全面的工具检索~不过这只是我内容的冰山一角，我还会输出许多对大家有用的干货、观点解决等，这波关注不亏（捂嘴笑\n" +
                "\n" +
                "对啦，也可以关注我的公众号「铁木君」，回复相应关键词，能获取许多免费又好用的资源哦~比如：\n" +
                "\n" +
                "回复“读书”，我给你分享3000本精选书籍+铁木君私人高分书单。\n" +
                "回复“技能”，送你我这些年购买、收集的写作、软件基操、摄影等珍贵教程");
        Log.i(TAG, "url: http://mingyi.fun:9000/video/v6.mp4");
        Log.i(TAG, "image:"+R.drawable.img);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("loginSuccess");
        registerReceiver(myReceiver, intentFilter);
        Log.d(TAG, "onCreate: 注册广播");

        Intent intent = getIntent();

        if (intent != null) {
            Log.d(TAG, "onCreate: 获取uid");
            uid = intent.getLongExtra("uid", 0);
            //缓存到sharedPreferences中
            //加载SharedPreferences
            sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("uid", String.valueOf(uid));
            edit.commit();
        }

        // 创建一个Bundle对象，并将要传递的值放入其中
        Bundle bundle = new Bundle();
        Log.d(TAG, "onCreate: " + uid);
        bundle.putString("uid", String.valueOf(uid));

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        //往HomeFragment传值
        navController.navigate(R.id.navigation_home, bundle);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //从数据库获取资源
        //1.创建/打开数据库，数据库名为VideoTable.db
        if (videoHelper == null) {
            videoHelper = VideoOpenHelper.getInstance(this);
        }
        //从数据库获取资源
        //2.创建/打开数据库，数据库名为UserTable.db
        if (userHelper == null) {
            userHelper = UserOpenHelper.getInstance(this);
        }
        //从数据库获取资源
        //2.创建/打开数据库，数据库名为StarTable.db
        if (starHelper == null) {
            starHelper = StarOpenHelper.getInstance(this);
        }

        videoHelper.openReadLink();
        videoHelper.openWriteLink();

        userHelper.openReadLink();
        userHelper.openWriteLink();

        starHelper.openReadLink();
        starHelper.openWriteLink();

        //加载SharedPreferences
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //加载数据库资源
        Resource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onStop: 注销");
        //关闭数据库连接
        videoHelper.closeLink();
        userHelper.closeLink();
        starHelper.closeLink();
        //注销广播
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void Resource() {
        //如果数据库为空，则加载资源
        if (videoHelper.queryAll().isEmpty()) {
            // 设置适配器的图片资源
            int[] imageId = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5};
            // 设置标题
            String[] title = new String[]{"【硬核社会学】996、内卷、打工人：马克思为什么是对的", "构建一个简单视频播放器", "99%的人不知道这些渠道，能帮你找到所有想要资源！", "求求你啦~求求你啦！帮帮小菲喵！", "【东雪莲】我咋看你咋像罕见"};
            String[] context = new String[]{"996、内卷、打工人现象的本质是什么？资本主义的底层逻辑是什么？为什么资本主义还没灭亡？2万字硬核论文，这可能是B站最学术、最硬核的马克思思想解析。\n" + "资本主义的现状与批判：\n" + "03:37     人性论\n" + "10:44    资本主义之性质\n" + "17:53    劳动分工\n" + "23:22    四种异化\n" + "35:21    剩余价值理论\n" + "40:15    为什么我们不反抗\n" + "下一期：马克思想错了吗？马克思主义的挑战与危机", "视频源代码：https://github.com/philipplackner/VideoPlayerCompose", "本期推荐：\n" + "书享家、学吧导航、科塔学术、HiPPTer、Seeseed、阿猫阿狗导航、创造狮导航、addog、199it、雪球导航、打假导航、搜狗网址导航、一个开始、虫部落\n" + "\n" + "传送：\n" + "第2期：BV1TN411d7FL", "塔菲\uD83E\uDD24\uD83E\uDD24为了你我高考不考了\uD83D\uDE24，我要成为第一个走出考场的人\uD83D\uDE21，对全国说，关注永雏塔菲，关注永雏塔菲谢谢喵！\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70", "wǒ bú xìn nǐ huì zhè mē wú liáo dē bǎ wǒ zhè jù huà dú yí biàn . rú guǒ nǐ zhēn dē dú lē . wǒ zhǐ xiǎng gào sù ni . gěi wǒ diǎn zàn."};
            String[] source = new String[]{"学院派", "Garimi", "框框，取景", "永雏塔菲", "東雪莲"};
            //时间
            String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String[] url = new String[]{"http://mingyi.fun:9000/video/v1.mp4", "http://mingyi.fun:9000/video/v2.mp4", "http://mingyi.fun:9000/video/v3.mp4", "http://mingyi.fun:9000/video/v4.mp4", "http://mingyi.fun:9000/video/v5.mp4"};
            //黑色
            int blackColor = getResources().getColor(R.color.bg_black);
            int[] likes = new int[]{95, 104, 93, 3, 5};
            int[] star = new int[]{65, 64, 114514, 64, 64};

            // 将上述资源保存到表中
            for (int i = 0; i < title.length; i++) {
                //将数据插入到数据库对应的表中
                Video video = new Video(title[i], context[i], source[i], time, url[i], star[i], likes[i], imageId[i], blackColor);
                if (videoHelper.insert(video) > 0) {
                    Toast.makeText(this, "添加数据:" + video.getTitle() + ",请重新加载app", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "添加数据失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (userHelper.queryAll().isEmpty()) {
            Log.d(TAG, "Resource: 进入添加用户");
            User user = new User("13408890905", "admin", "管理员");
            if (userHelper.insert(user) > 0) {
                Toast.makeText(this, "添加用户:" + user.getNikeName() + ",请重新加载app", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "添加用户失败", Toast.LENGTH_LONG).show();
            }
        }
        if (starHelper.queryAll().isEmpty()) {
            Log.d(TAG, "Resource: 进入添加关联关系");
            Star star = new Star(1, 3);
            if (starHelper.insert(star) > 0) {
                Toast.makeText(this, "添加收藏:" + star + ",请重新加载app", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "添加收藏失败", Toast.LENGTH_LONG).show();
            }
        }

        String readId = sharedPreferences.getString("readId", "");
        HashSet<String> array = new HashSet<>();
        if (!readId.equals("")) {
            for (String s : readId.split(" ")) {
                array.add(s);
            }
        }

        for (String s : array) {
            int grey = getResources().getColor(R.color.md_blue_grey_100);
            long id = Long.parseLong(s);
            Video video = videoHelper.queryById(id);
            video.setIsRead(grey);
            videoHelper.updateById(video);
        }
    }
}