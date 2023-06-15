package com.example.loginactivity.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.MainActivity;
import com.example.loginactivity.R;
import com.example.loginactivity.domain.User;
import com.example.loginactivity.util.UserOpenHelper;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    private LinearLayout ll_login_list1;
    private TextView tv_login_list1;
    private EditText et_login_list1;

    private LinearLayout ll_login_list2;
    private TextView tv_login_list2;
    private EditText et_login_list2;
    private LinearLayout ll_login_list3;
    private TextView tv_login_list3;
    private EditText et_login_list3;
    private LinearLayout ll_login_list4;
    private TextView tv_login_list4;
    private EditText et_login_list4;
    //登录
    private Button btn_login;
    //记住密码
    private CheckBox ck_remember;
    //验证码
    private Button btn_get_verifycode;
    private RadioGroup rg_login;
    private RadioButton rb_password;
    private RadioButton rb_register;
    private int checkedId;
    private UserOpenHelper userHelper;
    private List<User> listitem;
    private String log_phone;
    private String log_password;
    private String reg_phone;
    private String reg_code;
    private String reg_nikeName;
    private String reg_password;
    private int isLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //默认为登录
        checkedId = R.id.rb_password;

        rg_login = findViewById(R.id.rg_login);
        rb_password = findViewById(R.id.rb_password);
        rb_register = findViewById(R.id.rb_register);

        ll_login_list1 = findViewById(R.id.ll_login_list1);
        tv_login_list1 = findViewById(R.id.tv_login_list1);
        et_login_list1 = findViewById(R.id.et_login_list1);

        ll_login_list2 = findViewById(R.id.ll_login_list2);
        tv_login_list2 = findViewById(R.id.tv_login_list2);
        et_login_list2 = findViewById(R.id.et_login_list2);

        ll_login_list3 = findViewById(R.id.ll_login_list3);
        tv_login_list3 = findViewById(R.id.tv_login_list3);
        et_login_list3 = findViewById(R.id.et_login_list3);

        ll_login_list4 = findViewById(R.id.ll_login_list4);
        tv_login_list4 = findViewById(R.id.tv_login_list4);
        et_login_list4 = findViewById(R.id.et_login_list4);


        btn_get_verifycode = findViewById(R.id.btn_get_verifycode);

        btn_login = findViewById(R.id.btn_login);

        ck_remember = findViewById(R.id.ck_remember);

        Intent intent = getIntent();

        isLong = intent.getIntExtra("long",R.id.rb_password);
        if (isLong==R.id.rb_register){
            this.checkedId =  R.id.rb_register;
            tv_login_list2.setText(getString(R.string.verify_code));
            et_login_list2.setHint(getString(R.string.input_verify_code));
            ll_login_list3.setVisibility(View.VISIBLE);
            ll_login_list4.setVisibility(View.VISIBLE);
            tv_login_list4.setText(R.string.input_new_password);
            et_login_list4.setHint(R.string.input_new_password_hint);
            btn_get_verifycode.setVisibility(View.VISIBLE);
            btn_login.setText(getString(R.string.register));
            ck_remember.setVisibility(View.GONE);
        }

        //从数据库获取资源
        //1.创建/打开数据库，数据库名为NewsTable.db
        if (userHelper == null) {
            userHelper = UserOpenHelper.getInstance(this);
        }
        userHelper.openReadLink();
        userHelper.openWriteLink();

        if (listitem == null) {
            listitem = userHelper.queryAll();
        }

        //给rg_login设置监听器
        rg_login.setOnCheckedChangeListener(this);

        btn_get_verifycode.setOnClickListener(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedId == R.id.rb_password) {
                    log_phone = et_login_list1.getText().toString();
                    log_password = et_login_list2.getText().toString();
                    if (log_phone.length() < 11) {
                        Toast.makeText(getApplication(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean flag = true;
                        boolean unLogin = true;
                        for (User user : listitem) {
                            Log.d(TAG, "onClick: " + user);
                            if (Objects.equals(user.getPhoneNumber(), log_phone)) {
                                flag = false;
                            }
                            if (Objects.equals(user.getPhoneNumber(), log_phone) && Objects.equals(user.getPassword(), log_password)) {
                                unLogin = false;
                                //登录成功通报表扬

                                Intent intent = new Intent("loginSuccess");
                                intent.putExtra("nickName", user.getNikeName());
                                Log.d(TAG, "onClick: 发送广播");
                                sendBroadcast(intent);

                                Log.d(TAG, "onClick: 跳转");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("uid", user.getId()));

                            }
                        }
                        if (unLogin) {
                            Toast.makeText(getApplication(), "手机号和密码不匹配", Toast.LENGTH_SHORT).show();
                        }
                        if (flag) {
                            Toast.makeText(getApplication(), "用户名不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (checkedId == R.id.rb_register) {
                    reg_phone = et_login_list1.getText().toString();
                    reg_code = et_login_list2.getText().toString();
                    reg_nikeName = et_login_list3.getText().toString();
                    reg_password = et_login_list4.getText().toString();
                    Log.d(TAG, "onClick: 注册");
                    if (reg_code.contains("1") || reg_code.isEmpty()) {
                        Toast.makeText(getApplication(), "验证码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean flag = true;
                        for (User user : listitem) {
                            if (Objects.equals(user.getNikeName(), reg_nikeName)) {
                                Toast.makeText(getApplication(), "昵称已被注册", Toast.LENGTH_SHORT).show();
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            //成功注册
                            User user1 = new User(reg_phone, reg_password, reg_nikeName);
                            userHelper.openWriteLink();
                            userHelper.insert(user1);
                            Toast.makeText(getApplication(), "注册:" + reg_nikeName + "成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent("registerSuccess");
                            intent.putExtra("nickName", user1.getNikeName());
                            Log.d(TAG, "onClick: 发送广播");
                            sendBroadcast(intent);

                            //查一下id值
                            for (User user : userHelper.queryAll()) {
                                if (Objects.equals(user.getNikeName(), user1.getNikeName())) {
                                    user1 = user;
                                }
                            }
                            Log.d(TAG, "onClick: 跳转");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("uid", user1.getId()));
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //从数据库获取资源
        //创建/打开数据库，数据库名为UserTable.db
        if (userHelper == null) {
            Log.d(TAG, "onStart: userHelper");
            userHelper = UserOpenHelper.getInstance(this);
            userHelper.openReadLink();
            userHelper.openWriteLink();
        }
        //加载SharedPreferences
//        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.checkedId = checkedId;
        if (checkedId == R.id.rb_password) {
            tv_login_list2.setText(getString(R.string.login_password));
            et_login_list2.setHint(getString(R.string.input_password));
            ll_login_list3.setVisibility(View.GONE);
            ll_login_list4.setVisibility(View.GONE);
            btn_get_verifycode.setVisibility(View.GONE);

            btn_login.setText(getString(R.string.login));
            ck_remember.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_register) {
            tv_login_list2.setText(getString(R.string.verify_code));
            et_login_list2.setHint(getString(R.string.input_verify_code));
            ll_login_list3.setVisibility(View.VISIBLE);
            ll_login_list4.setVisibility(View.VISIBLE);
            tv_login_list4.setText(R.string.input_new_password);
            et_login_list4.setHint(R.string.input_new_password_hint);
            btn_get_verifycode.setVisibility(View.VISIBLE);

            btn_login.setText(getString(R.string.register));
            ck_remember.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        if (checkedId == R.id.rb_register) {
            if (v.getId() == R.id.btn_get_verifycode) {
                if (reg_phone.length() < 11) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    for (User user : listitem) {
                        if (Objects.equals(user.getPhoneNumber(), reg_phone)) {
                            Toast.makeText(this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}