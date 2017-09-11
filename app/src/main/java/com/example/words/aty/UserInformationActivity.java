package com.example.words.aty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;
import com.example.words.util.BitmapByte;
import com.example.words.util.CircleImageView;
import com.example.words.util.FileUtil;
import com.example.words.util.SelectPicPopupWindow;

import java.io.ByteArrayOutputStream;
import java.io.File;

import db.User;

public class UserInformationActivity extends Activity implements View.OnClickListener, AsyncMsgRes {

    private Context mContext;
    private LinearLayout llUpdateAvatar, llUpdateNickname, llUpdateIntroduction;
    private CircleImageView civUserAvatar;
    private TextView tvUsername, tvUserNickname, tvUserIntroduction;
    private Button btnResetPsw;
    //更改头像相关
    private SelectPicPopupWindow menuWindow;//自定义的头像编辑弹出框
    private static final String IMAGE_FILE_NAME = "avatar_default.jpg";//头像文件名称
    private String urlpath;//图片本地路径
    private static ProgressDialog pd;//等待进度圈
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记

    byte[] userAvatar;
    String nickName;
    String introduction;
    //
    MessagePack messagePack = new MessagePack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        mContext = this;

        initDatas();
        initViews();
        initEvents();
    }

    public void initDatas() {

    }

    public void initViews() {
        llUpdateAvatar = (LinearLayout) findViewById(R.id.ll_update_avatar);
        llUpdateNickname = (LinearLayout) findViewById(R.id.ll_update_nickname);
        llUpdateIntroduction = (LinearLayout) findViewById(R.id.ll_update_introduction);

        civUserAvatar = (CircleImageView) findViewById(R.id.civ_user_avatar);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvUserNickname = (TextView) findViewById(R.id.tv_user_nickname);
        tvUserIntroduction = (TextView) findViewById(R.id.tv_user_introduction);

        btnResetPsw = (Button) findViewById(R.id.btn_reset_psw);

        //
        ExMsgTask MTask = new ExMsgTask();
        MessagePack mp = new MessagePack();
        User user = new User();
        user.setUserID("1111");
        mp.setUser(user);
        mp.setOrderType("0008");
        MTask.setMessagePack(mp);
        MTask.setDelegate(UserInformationActivity.this);
        MTask.execute();

        System.out.println("initView");




    }

    public void initEvents() {
        llUpdateAvatar.setOnClickListener(this);
        llUpdateNickname.setOnClickListener(this);
        llUpdateIntroduction.setOnClickListener(this);
        btnResetPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            //case：修改头像
            case R.id.ll_update_avatar: {
                menuWindow = new SelectPicPopupWindow(mContext,itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_user_information),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                break;
            }

            //case：修改昵称
            case R.id.ll_update_nickname: {

                final UpdateNameDialog.Builder builder = new UpdateNameDialog.Builder(this);
                builder.setTitle("更改昵称");
                builder.setNickname("yamasamaaa");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newNickname = ((EditText)builder.getContentView().
                                findViewById(R.id.et_update_name)).getText().toString();
                        //拿到EditText的值，后台修改昵称
                        ExMsgTask MTask = new ExMsgTask();
                        MessagePack mp = new MessagePack();
                        User user = new User();
                        user.setUserID("1111");
                        user.setNickname(newNickname);
                        mp.setUser(user);
                        mp.setOrderType("0006");
                        MTask.setMessagePack(mp);
                        MTask.setDelegate(UserInformationActivity.this);
                        MTask.execute();

                        if (true) {
                            tvUserNickname.setText(newNickname);
                        }
                        System.out.println("newNickname:"+newNickname);
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            }

            //case：修改个人介绍
            case R.id.ll_update_introduction: {
                final UpdateIntroductionDialog.Builder builder = new UpdateIntroductionDialog.Builder(this);
                builder.setTitle("编辑个人简介");
                builder.setIntroduction("yamasamaaa\nyamasamaaa\nyamasamaaa\nyamasamaaa\nyamasamaaa");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newIntroduction = ((EditText)builder.getContentView().
                                findViewById(R.id.et_update_introduction)).getText().toString();
                        //拿到EditText的值，后台修改个人简介
                        System.out.println("newIntroduction:"+newIntroduction);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            }

            //case：重置密码
            case R.id.btn_reset_psw:
            {
                final UpdatePswDialog.Builder builder = new UpdatePswDialog.Builder(this);
                builder.setTitle("重置密码");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldPsw = ((EditText)builder.getContentView().
                                findViewById(R.id.et_old_psw)).getText().toString();
                        String newPsw = ((EditText)builder.getContentView().
                                findViewById(R.id.et_new_psw)).getText().toString();
                        String newPsw2 = ((EditText)builder.getContentView().
                                findViewById(R.id.et_new_psw_again)).getText().toString();
                        //拿到EditText的值，后台修改密码
                        System.out.println("oldPsw:"+oldPsw+"; newPsw:"+newPsw+"; newPsw2:"+newPsw2);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            }
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()){
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(pickIntent,REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case REQUESTCODE_PICK:
                try{
                    System.out.println("pick+++++cut");
                    startPhotoZoom(data.getData());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_TAKE:
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:
                System.out.println("hhhhhhhhh"+data);
                if (data!=null){
                    setPicToView(data);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
        System.out.println("klhlkj hkj k--------startPhotoZoomEnd");
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            System.out.println("保存裁剪的图片");
            System.out.println("photo:"+photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            System.out.println("保存裁剪的图片成功");
            civUserAvatar.setImageDrawable(drawable);
            byte[] usravatar = Bitmap2Bytes(photo);

            //上传至服务器
            User user = new User();
            user.setUserID("1111");
            user.setpicture(usravatar);
            MessagePack mp = new MessagePack();
            mp.setOrderType("0004");
            mp.setUser(user);
            ExMsgTask ETask = new ExMsgTask();
            ETask.setMessagePack(mp);
            ETask.setDelegate(UserInformationActivity.this);
            ETask.execute();
        }
    }

    @Override
    public void processFinish(MessagePack outputPack) {
        switch (outputPack.getOrderType()){
            case "0004" :
                Toast.makeText(getApplicationContext(),"上传头像成功",Toast.LENGTH_SHORT).show();
                break;
            case "0006":
                Toast.makeText(getApplicationContext(),"修改昵称成功",Toast.LENGTH_SHORT).show();
                break;

            case "0008" :
                messagePack = outputPack;
                userAvatar = messagePack.getUser().getpicture();
                nickName = messagePack.getUser().getNickname();
                introduction = messagePack.getUser().getIntroduce();

                civUserAvatar.setImageBitmap((new BitmapByte()).Bytes2Bimap(userAvatar));
                tvUserNickname.setText(nickName);
                tvUserIntroduction.setText(introduction);
                break;

        }
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * @重写aty生命周期中的其它几个函数
     */
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



















