package com.example.words.aty;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;
import com.example.words.util.FileUtil;
import com.example.words.util.SelectPicPopupWindow;

import java.io.ByteArrayOutputStream;
import java.io.File;

import db.User;
import db.Wordbook;

public class CreateBookActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private LinearLayout llChangeBookCover,llChangeBookTag;
    private EditText etBookName,etBookDescription;
    private ImageView ivBookCover;
    private Switch switchPrivate;
    private Button btnFinish;
    private ImageButton btnReturn;
    private TextView title;

    private Context mContext;
    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "bookcover_default.jpg";//头像文件名称
    private String urlpath;//图片本地路径
    private static ProgressDialog pd;//等待进度圈
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记

    private Wordbook newWordBook = new Wordbook();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);

        initViews();
        initDatas();
        initEvents();
    }

    public void initViews() {
        llChangeBookCover = (LinearLayout) findViewById(R.id.ll_change_book_cover);
        llChangeBookTag = (LinearLayout) findViewById(R.id.ll_change_book_tag);

        etBookName = (EditText) findViewById(R.id.et_create_book_name);
        etBookDescription = (EditText) findViewById(R.id.et_create_book_description);
        ivBookCover = (ImageView) findViewById(R.id.iv_create_book_cover);
        switchPrivate = (Switch) findViewById(R.id.switch_whether_private);

        btnFinish = (Button) findViewById(R.id.btn_finish_creating_book);
        btnReturn = (ImageButton) findViewById(R.id.btn_return);
        title = (TextView) findViewById(R.id.tv_topbar_title);
        title.setText("创建单词本");

        mContext = CreateBookActivity.this;
    }

    public void initDatas() {

    }

    public void initEvents() {
        llChangeBookCover.setOnClickListener(this);
        llChangeBookTag.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_book_cover:
                //选封面
                menuWindow = new SelectPicPopupWindow(mContext,itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_create_book),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                break;
            case R.id.ll_change_book_tag:
                //选标签
                break;

            case R.id.btn_finish_creating_book:
                //保存当前数据
                newWordBook.setName(etBookName.getText().toString());
                newWordBook.setDiscri(etBookDescription.getText().toString());
                newWordBook.setPrim(switchPrivate.isChecked());
                break;
            case R.id.btn_return:
                //如果创建成功，则返回单词本名字
                if (!etBookName.getText().equals(null)) {
                    Intent i = new Intent();
                    i.putExtra("newbookname",etBookName.getText().toString());
                    setResult(1,i);
                }
                finish();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

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
            ivBookCover.setImageDrawable(drawable);

            byte[] newBookCover = Bitmap2Bytes(photo);
            newWordBook.setPicture(newBookCover);

//            //上传至服务器
//            User user = new User();
//            user.setUserID("addme");
//            user.setpicture(usravatar);
//            MessagePack mp = new MessagePack();
//            mp.setOrderType("0004");
//            mp.setUser(user);
//            ExMsgTask ETask = new ExMsgTask();
//            ETask.setMessagePack(mp);
//            ETask.setDelegate(CreateBookActivity.this);
//            ETask.execute();
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
