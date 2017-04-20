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
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;

import db.ACID;
import db.User;

/**
 * 注册Activity
 * Created by 6gold on 2017/3/2.
 */

public class RegisterActivity extends Activity implements View.OnClickListener,AsyncMsgRes {

    private EditText userId;//注册用户名
    private TextView usernameUnavailable;//用户名不可用
    private EditText userPassword1;//注册密码
    private EditText userPassword2;//再次输入的密码
    private Button deleteInput;
    private Button showPassword1;
    private Button showPassword2;
    private Button register;
    Boolean showPass1 = false;//默认不显示密码
    Boolean showPass2 = false;//默认不显示密码
    Boolean rgtSuccess = false;//创建时默认注册失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_register);//设置layout

        //获取各个组件
        userId = (EditText) findViewById(R.id.et_rgt_id);
        userId.addTextChangedListener(myTextWacher);//输入用户名监听
        deleteInput = (Button) findViewById(R.id.btn_rgt_delete_input);
        usernameUnavailable = (TextView) findViewById(R.id.tv_username_unavailable);
        usernameUnavailable.setVisibility(View.INVISIBLE);//最初提示文字不可见

        userPassword1 = (EditText) findViewById(R.id.et_rgt_password_1);
        userPassword2 = (EditText) findViewById(R.id.et_rgt_password_2);
        showPassword1 = (Button) findViewById(R.id.btn_show_password_1);
        showPassword2 = (Button) findViewById(R.id.btn_show_password_2);

        register = (Button) findViewById(R.id.btn_register);

        //组件监听
        deleteInput.setOnClickListener(this);
        showPassword1.setOnClickListener(this);
        showPassword2.setOnClickListener(this);
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
            //判断用户名是否有效
            Boolean isUsernameAvailable = isUsernameAvailable(temp);//判断用户名是否有效的函数，返回一个Boolean值
            if (isUsernameAvailable) {
                usernameUnavailable.setText(R.string.rgt_username_available);
                usernameUnavailable.setVisibility(View.VISIBLE);
            } else {
                usernameUnavailable.setText(R.string.rgt_username_unavailable);
                usernameUnavailable.setVisibility(View.VISIBLE);
            }
        }
    };

    //按钮监听
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //删除已输入的用户名
            case R.id.btn_rgt_delete_input:
            {
                userId.setText("");//用户名输入框清空
                deleteInput.setVisibility(View.INVISIBLE);//delete图标不可见
                break;
            }

            //注册密码是否可见
            case R.id.btn_show_password_1:
            {
                //如果点击时图标为eye-close，则更改为eye-open图标并显示密码，更改showPass值为true
                if(!showPass1){
                    userPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //显示密码
                    showPassword1.setBackgroundResource(R.mipmap.eye_open);//更改按钮图标为eye-open
                    showPass1 = true;
                }
                else{
                    userPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());//不显示密码
                    showPassword1.setBackgroundResource(R.mipmap.eye_close);//更改图标按钮为eye-close
                    showPass1 = false;
                }
                break;
            }

            //再次确认的密码是否可见
            case R.id.btn_show_password_2:
            {
                //如果点击时图标为eye-close，则更改为eye-open图标并显示密码，更改showPass值为true
                if(!showPass2){
                    userPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //显示密码
                    showPassword2.setBackgroundResource(R.mipmap.eye_open);//更改按钮图标为eye-open
                    showPass2 = true;
                }
                else{
                    userPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());//不显示密码
                    showPassword2.setBackgroundResource(R.mipmap.eye_close);//更改图标按钮为eye-close
                    showPass2 = false;
                }
                break;
            }

            //确认注册按钮
            case R.id.btn_register: {
                String userID = userId.getText().toString();
                String password1 = userPassword1.getText().toString();
                String password2 = userPassword2.getText().toString();
                if (password1.equals(null) || password2.equals(null)) {
                    Toast.makeText(this, R.string.rgt_pass_uncomplete, Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(password2)) {
                    Toast.makeText(this, R.string.rgt_pass_uncompatible, Toast.LENGTH_SHORT).show();
                    userPassword2.setText("");//清空第二个密码框
                } else {
                    User user = new User();
                    user.setUserID(userID);
                    user.setPassword(password1);
                    MessagePack mp = new MessagePack();
                    mp.setOrderType("0000");
                    mp.setUser(user);
                    ExMsgTask ETask = new ExMsgTask();
                    ETask.setMessagePack(mp);
                    ETask.setDelegate(RegisterActivity.this);
                    ETask.execute();
                    break;
                }
                break;
            }
        }
    }

    public Boolean isUsernameAvailable(CharSequence temp) {
        Boolean isUsernameAvailable = true;
        if (userId.getText() == null) {
            isUsernameAvailable = false;
        }
//        if(temp在数据库中已经存在了){
//            isUsernameAvailable = false;
//        }
        return isUsernameAvailable;
    }

    @Override
    public void processFinish(MessagePack outputPack) {
        System.out.println("调用回调函数.");
        Boolean isUserExist = outputPack.booleanResult.get(0);//判断用户是否存在的函数
        Boolean isAddSuccsee = outputPack.booleanResult.get(1);//标记是否添加成功
        if (!isUserExist) {
            if (isAddSuccsee) {
                ACID acid = new ACID(RegisterActivity.this);
                acid.addUserIDandpassword(outputPack.getUser().getUserID(),
                        outputPack.getUser().getPassword(),
                        "userdb");
                System.out.println("添加数据库成功.");

                Toast.makeText(this,R.string.rgt_success, Toast.LENGTH_SHORT).show();
                System.out.println("注册成功！");
                rgtSuccess = true;
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("userId",userId.getText().toString());
                intent.putExtra("userPass",userPassword1.getText().toString());
                startActivity(intent);
                RegisterActivity.this.finish();
                System.out.println("注册活动已销毁！");
            }
        } else {
            Toast.makeText(this,R.string.rgt_user_already_exists, Toast.LENGTH_SHORT).show();
            userId.setText("");
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
        if (!rgtSuccess) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            System.out.println("登录活动已创建！");
        }
    }
}
