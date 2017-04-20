package com.example.words.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;

import db.User;

/**
 * 登录Activity
 * 第一次启动应用的时候应该启动的活动
 * Created by 6gold on 2017/2/28.
 */

public class LoginActivity extends Activity implements View.OnClickListener,AsyncMsgRes {

    private EditText userId;
    private EditText userPassword;
    private Button deleteInput;
    private Button showPassword;
    private Button login;
    private Button forgetPassword;
    private Button register;
    Boolean showPass = false;//默认不显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);//设置layout

        //获取各个组件
        userId = (EditText) findViewById(R.id.et_user_id);
        userId.addTextChangedListener(myTextWacher);//输入用户名监听
        deleteInput = (Button) findViewById(R.id.btn_delete_input);

        userPassword = (EditText) findViewById(R.id.et_user_password);
        showPassword = (Button) findViewById(R.id.btn_show_password);

        login = (Button) findViewById(R.id.btn_login);

        forgetPassword = (Button) findViewById(R.id.btn_forgetPassword);
        register = (Button) findViewById(R.id.btn_login_register);

        //获取注册的用户名和密码
        Intent i = getIntent();
        if (i != null) {
            System.out.println("捕获intent");
            String rgtUserId = i.getStringExtra("userId");
            userId.setText(rgtUserId);
            String rgtUserPass = i.getStringExtra("userPass");
            userPassword.setText(rgtUserPass);
        } else {
            System.out.println("未捕获intent");
            userId.setText("");
            userPassword.setText("");
        }

        //组件监听
        deleteInput.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    //输入用户名监听
    TextWatcher myTextWacher =new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            //根据输入字数判断delete图标是否可见
            if(temp.length() > 0){
                deleteInput.setVisibility(View.VISIBLE);
            }
            else
                deleteInput.setVisibility(View.INVISIBLE);
        }
    };

    //按钮监听
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //删除已输入的用户名
            case R.id.btn_delete_input:
            {
                userId.setText("");//用户名输入框清空
                deleteInput.setVisibility(View.INVISIBLE);//delete图标不可见
                break;
            }
            //密码是否可见
            case R.id.btn_show_password:
            {
                //如果点击时图标为eye-close，则更改为eye-open图标并显示密码，更改showPass值为true
                if(!showPass){
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //显示密码
                    showPassword.setBackgroundResource(R.mipmap.eye_open);//更改按钮图标为eye-open
                    showPass = true;
                }
                else{
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//不显示密码
                    showPassword.setBackgroundResource(R.mipmap.eye_close);//更改图标按钮为eye-close
                    showPass = false;
                }
                break;
            }
            //登录事件处理
            case R.id.btn_login:
            {
                String userID = userId.getText().toString();//界面中用户输入的用户名
                String password = userPassword.getText().toString();//界面中用户输入的密码
                User user = new User();
                user.setUserID(userID);
                user.setPassword(password);
                MessagePack mp = new MessagePack();
                mp.setOrderType("0001");
                mp.setUser(user);
                ExMsgTask ETask = new ExMsgTask();
                ETask.setMessagePack(mp);
                ETask.setDelegate(LoginActivity.this);
                ETask.execute();
                break;
            }
            //忘记密码
            case R.id.btn_forgetPassword:
            {
                //处理忘记密码事件
                break;
            }
            //注册
            case R.id.btn_login_register:
            {
                //打开注册Activity
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                System.out.println("登录活动已销毁！");
                break;
            }
        }
    }

    @Override //回调函数 改UI
    public void processFinish(MessagePack outputPack) {
        Boolean isUserExist = outputPack.booleanResult.get(0);//判断用户是否存在的函数
        Boolean isPassRight = outputPack.booleanResult.get(1);//标记密码是否正确
            if (isUserExist) {
                if (isPassRight) {
                    //密码正确
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    System.out.println("登陆成功！");
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    System.out.println("登陆活动已销毁！");
                } else {
                    //密码不正确
                    Toast.makeText(this,R.string.login_wrong_pass, Toast.LENGTH_SHORT).show();
                    //清空当前已输入的密码
                    userPassword.setText("");
                }
            }
            else {
                //用户不存在
                Toast.makeText(this,R.string.login_user_notfound, Toast.LENGTH_SHORT).show();
                //清空已经输入东西
                userId.setText("");
                userPassword.setText("");
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
