package com.example.words.aty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;
import com.example.words.util.CircleImageView;
import com.example.words.util.FileUtil;
import com.example.words.util.SelectPicPopupWindow;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import db.User;

/**
 * 主Activity
 * 由LoginActivity启动（登录成功后要进入的活动）
 * Created by 6gold on 2017/2/28.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener ,AsyncMsgRes {

    /*成员变量--------------------------------------------------------*/
    private ViewPager myViewPager;                              //vp
    private FragmentPagerAdapter myViewPagerAdapter;            //vp适配器
    private List<Fragment> myViewPagerFragments;                //vp存放的Fragment集合
    private LinearLayout tabBooks, tabHome, tabPersonal;        //Fragments对应布局
    private ImageButton imgBtnBooks, imgBtnHome, imgBtnPersonal;//Fragments对应按钮
    private ImageButton btnOpenLeftMenu;                        //打开左侧抽屉按钮

    private Context mContext;
    private SlidingMenu menu;
    private CircleImageView avatar;
    private TextView username;
    private SelectPicPopupWindow menuWindow;//自定义的头像编辑弹出框
    private static final String IMAGE_FILE_NAME = "avatar_default.jpg";//头像文件名称
    private String urlpath;//图片本地路径
    private static ProgressDialog pd;//等待进度圈
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记

    /*相关函数--------------------------------------------------------*/
    /*@重写onCreate()方法
     * 方法名：onCreate(Bundle savedInstanceState)
     * 功    能：创建活动
     * 参    数：Bundle savedInstanceState - ?
     * 返回值：无
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉头部
        setContentView(R.layout.activity_main);         //设置layotu
        initViews();    //初始化控件
        initEvents();   //初始化事件
        initDatas();    //初始化数据

        //初始化完成之后，先将三个button置浅灰色，然后默认选择tab1(即主页)，对应的图标高亮
        resetImgs();
        selectTab(1);
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    private void initViews() {
        myViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        tabBooks = (LinearLayout) findViewById(R.id.id_tab_books);
        tabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        tabPersonal = (LinearLayout) findViewById(R.id.id_tab_personal);

        imgBtnBooks = (ImageButton) findViewById(R.id.id_tab_books_img);
        imgBtnHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        imgBtnPersonal = (ImageButton) findViewById(R.id.id_tab_personal_img);

        btnOpenLeftMenu = (ImageButton) findViewById(R.id.btn_open_leftmenu);

        mContext = MainActivity.this;

        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.left_menu_shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单的视图宽度
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        // 为滑动菜单设置布局
        menu.setMenu(R.layout.leftmenu);

        avatar = (CircleImageView) findViewById(R.id.civAvatar);
        username = (TextView) findViewById(R.id.tvUsername);
    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    private void initEvents() {
        //设置底栏三个tab的点击事件
        tabBooks.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabPersonal.setOnClickListener(this);
        avatar.setOnClickListener(this);
        btnOpenLeftMenu.setOnClickListener(this);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    private void initDatas() {
        myViewPagerFragments = new ArrayList<>();
        //将各个Fragment加入集合中
        myViewPagerFragments.add(new BooksFragment());
        myViewPagerFragments.add(new HomeFragment());
        myViewPagerFragments.add(new PersonalFragment());

        //初始化vp适配器 - myViewPagerAdapter
        myViewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override //获取页面i
            public Fragment getItem(int position) {
                return myViewPagerFragments.get(position);
            }

            @Override //获取页面总数
            public int getCount() {
                return myViewPagerFragments.size();
            }
        };
        //设置适配器
        myViewPager.setAdapter(myViewPagerAdapter);

        //设置vp的切换监听
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override //页面选中事件
            public void onPageSelected(int position) {
                resetImgs();            //重置底栏图标
                selectTab(position);    //选择一个页面显示
            }

            @Override //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /*@重写onClick()方法
     * 方法名：onClick(View v)
     * 功    能：处理按钮点击事件
     * 参    数：View v - 按钮的View
     * 返回值：无
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //根据点击的Tab切换不同的页面以及对应的ImageButton为主题色
            case R.id.id_tab_books:
                resetImgs();//先将三个button置浅灰色
                selectTab(0);//再选择tab0，对应的button高亮
                break;
            case R.id.id_tab_home:
                resetImgs();//先将三个button置浅灰色
                selectTab(1);//再选择tab1，对应的button高亮
                break;
            case R.id.id_tab_personal:
                resetImgs();//先将三个button置浅灰色
                selectTab(2);//再选择tab2，对应的button高亮
                break;
            case R.id.civAvatar:
                menuWindow = new SelectPicPopupWindow(mContext,itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                break;
            case R.id.btn_open_leftmenu:
                menu.toggle();
                break;
            //......
        }
    }

    /*
     * 方法名：selectTab(int i)
     * 功    能：选择底栏Tab对应的页面
     * 参    数：int i - 所选择页面的index
     * 返回值：无
     */
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为主题色
        switch (i) {
            case 0:
                imgBtnBooks.setImageResource(R.mipmap.tab_book);
                break;
            case 1:
                imgBtnHome.setImageResource(R.mipmap.tab_home);
                break;
            case 2:
                imgBtnPersonal.setImageResource(R.mipmap.tab_personal);
                break;
        }
        //设置当前点击的Tab所对应的Fragment为Fragment数组里的第i项
        myViewPager.setCurrentItem(i);
    }

    /*
     * 方法名：resetImgs()
     * 功    能：将三个Button设置为浅灰色
     * 参    数：无
     * 返回值：无
     */
    private void resetImgs() {
        imgBtnBooks.setImageResource(R.mipmap.tab_book_normal);
        imgBtnHome.setImageResource(R.mipmap.tab_home_normal);
        imgBtnPersonal.setImageResource(R.mipmap.tab_personal_normal);
    }

    // 设置通过物理菜单打开滑动菜单
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_MENU:
                menu.toggle();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode,event);
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
            avatar.setImageDrawable(drawable);

            String usrname = username.getText().toString();
            byte[] usravatar = Bitmap2Bytes(photo);


            //上传至服务器
            User user = new User();
            user.setUserID("addme");
            user.setpicture(usravatar);
            MessagePack mp = new MessagePack();
            mp.setOrderType("0004");
            mp.setUser(user);
            ExMsgTask ETask = new ExMsgTask();
            ETask.setMessagePack(mp);
            ETask.setDelegate(MainActivity.this);
            ETask.execute();
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

    @Override
    public void processFinish(MessagePack outputPack) {
        Toast.makeText(getApplicationContext(),"上传头像成功",Toast.LENGTH_SHORT).show();
    }

}