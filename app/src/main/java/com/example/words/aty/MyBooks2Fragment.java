package com.example.words.aty;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.MessagePack;
import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

import db.ACID;
import db.Wordbook;

/**
 * Created by 6gold on 2017/5/22.
 */

public class MyBooks2Fragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    /*数据--------------------------------------------------------*/
    private ArrayList<String> bookTitles = new ArrayList<String>();         //单词本——标题
    private ArrayList<String> bookAuthors = new ArrayList<String>();        //单词本——作者
    private ArrayList<Bitmap> bookCovers = new ArrayList<Bitmap>();         //单词本——封面
    private ArrayList<Integer> bookWordNumbers = new ArrayList<Integer>();   //单词本——词汇量

    /*成员变量--------------------------------------------------------*/
    View view;
    //查询结果ListView
    private ListView lvMybooksList1;
    private ListItemAdapter listItemAdapter;

    /*相关函数--------------------------------------------------------*/

    /*@重写onCreateView()方法
     * 方法名：onCreate(......)
     * 功    能：创建活动
     * 参    数：......
     * 返回值：无
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_mybooks1,container,false);

        /*初始化操作--------------------------------------------------------*/

        initViews();
        initEvents();
        initDatas();

        return view;
    }

    /*
     * 方法名：initViews()
     * 功    能：初始化控件
     * 参    数：无
     * 返回值：无
     */
    public void initViews(){
        lvMybooksList1 = (ListView) view.findViewById(R.id.lv_mybooks_1);
    }

    /*
     * 方法名：initDatas()
     * 功    能：初始化数据(包括适配器的初始化)
     * 参    数：无
     * 返回值：无
     */
    public void initDatas(){
        Resources res=getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.defualtcover);
        ACID acid = new ACID(this.getActivity());
        List<Wordbook> wordbookList = acid.getwordbooklist("收藏");
        for(int i = 0; i<wordbookList.size(); i++){
            bookTitles.add(wordbookList.get(i).getName());
            bookAuthors.add(wordbookList.get(i).getAuthor());
            bookCovers.add(Bytes2Bitmap(wordbookList.get(i).getPicture()));
            bookWordNumbers.add(wordbookList.get(i).getWordnumber());
        }

        System.out.println("啦啦啦啦啦啦啦"+lvMybooksList1);
        listItemAdapter = new ListItemAdapter(bookTitles,bookAuthors,
                bookWordNumbers,bookCovers,this.getActivity());
        lvMybooksList1.setAdapter(listItemAdapter);

    }

    /*
     * 方法名：initEvents()
     * 功    能：初始化事件(点击事件等)
     * 参    数：无
     * 返回值：无
     */
    public void initEvents(){
        lvMybooksList1.setOnItemClickListener(this);
    }


    /*@重写OnItemClickListener()方法
     * 方法名：OnItemClickListener(......)
     * 功    能：处理点击Item事件，此处简单toast它的index
     * 参    数：View v - 被点击的View
     * 返回值：无
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this.getActivity(), "item"+position, Toast.LENGTH_SHORT).show();
    }

    /*
       * 方法名：Bytes2Bitmap(byte[] b)
       * 功    能：图片格式转换-Bytes转Bitmap
       * 参    数：byte[] b - byte格式的图片
       * 返回值：无
       */
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /*
     * 方法名：drawableToBitmap(Drawable drawable)
     * 功    能：图片格式转换-drawable转Bitmap
     * 参    数：Drawable drawable - Drawable格式的图片
     * 返回值：无
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;
    }



    /*相关类--------------------------------------------------------*/

    //ListItem类
    public class ListItem{
        private String title;
        private String author;
        private Integer wordnumber;
        private Bitmap cover;

        public ListItem() {super();}

        public ListItem(String title, String author, Integer wordnumber, Bitmap cover){
            super();
            this.title = title;
            this.author = author;
            this.wordnumber = wordnumber;
            this.cover = cover;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public void setWordnumber(Integer wordnumber) {
            this.wordnumber = wordnumber;
        }

        public Integer getWordnumber() {
            return wordnumber;
        }

        public void setCover(Bitmap cover) {
            this.cover = cover;
        }

        public Bitmap getCover() {
            return cover;
        }
    }

    //ListItemAdapter类
    public class ListItemAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<ListItem> listItemList;

        public ListItemAdapter(ArrayList<String> titles, ArrayList<String> authors,
                               ArrayList<Integer> wordnumbers, ArrayList<Bitmap> covers,
                               Context context){
            listItemList = new ArrayList<ListItem>();
            inflater = LayoutInflater.from(context);
            for (int i = 0; i<covers.size();i++){
                ListItem item = new ListItem(titles.get(i),authors.get(i),
                        wordnumbers.get(i),covers.get(i));
                listItemList.add(item);
            }

        }

        @Override
        public int getCount() {
            if (listItemList != null) {
                return listItemList.size();
            } else {return 0;}
        }

        @Override
        public Object getItem(int position) {
            return listItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_book2,null);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_booklist2_title);
                viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_booklist2_publisher);
                viewHolder.tvWordNumber = (TextView) convertView.findViewById(R.id.tv_booklist2_amount);
                viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.iv_booklist2_cover);
                System.out.println("tvTitle"+viewHolder.tvTitle);
                convertView.setTag(viewHolder);
                System.out.println("测试viewHolder:"+viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            System.out.println("测试listItemList"+listItemList);

            viewHolder.tvTitle.setText(listItemList.get(position).getTitle());
            viewHolder.tvAuthor.setText(listItemList.get(position).getAuthor());
            viewHolder.tvWordNumber.setText(String.valueOf(listItemList.get(position).getWordnumber()));
            viewHolder.ivCover.setImageBitmap(listItemList.get(position).getCover());
            return convertView;
        }
    }

    //ViewHolder类
    public class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvWordNumber;
    }
}
