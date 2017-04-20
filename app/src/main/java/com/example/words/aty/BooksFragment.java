package com.example.words.aty;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

import db.wordbook;

/**
 * MainActivity的BooksFragment子类
 * Created by 6gold on 2017/3/4.
 */

public class BooksFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,AsyncMsgRes {

    /*默认数据--------------------------------------------------------*/
    private ArrayList<String> testTitles1 = new ArrayList<String>();//排名前三的单词本标题
    private ArrayList<Bitmap> testImgs1 = new ArrayList<Bitmap>();//排名前三的单词本图片
    private ArrayList<String> testTitles2 = new ArrayList<String>();
    private ArrayList<Bitmap> testImgs2 = new ArrayList<Bitmap>();
    private ArrayList<String> testTitles3 = new ArrayList<String>();
    private ArrayList<Bitmap> testImgs3 = new ArrayList<Bitmap>();

    /*成员变量--------------------------------------------------------*/
    //topbar
    private ImageButton btnOpenLeftDrawer;
    //customSearchBox2
    private LinearLayout customSearchBox2;
    //hotTags
    private Button btnSearchBookCET4;
    private Button btnSearchBookCET6;
    private Button btnSearchBookKY;
    private Button btnSearchBookTOEFL;
    private Button btnSearchBookIELTS;
    private Button btnSearchBookGRE;
    private Button btnSearchBookGMAT;
    //备考单词本
    private ImageButton btnMoreBookBeikao;
    private GridView gvBookClass1;
    GridItemAdapter gridItemAdapter1;
    //趣味单词本
    private ImageButton btnMoreBookQuwei;
    private GridView gvBookClass2;
    GridItemAdapter gridItemAdapter2;
    //精品单词本
    private ImageButton btnMoreBookJingpin;
    private GridView gvBookClass3;
    GridItemAdapter gridItemAdapter3;


    /*相关函数--------------------------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab_books,container,false);

        ExMsgTask MTask = new ExMsgTask();
        MessagePack mp = new MessagePack();
        mp.setOrderType("0002");
        MTask.setMessagePack(mp);
        MTask.setDelegate(BooksFragment.this);
        MTask.execute();

        /*初始化操作--------------------------------------------------------*/
        //初始化topbar（抽屉按钮等）

        //初始化单词本搜索框
        customSearchBox2 = (LinearLayout) view.findViewById(R.id.ll_custom_serchbox2_static);
        customSearchBox2.setOnClickListener(this);

        //初始化hotTags
        btnSearchBookCET4 = (Button) view.findViewById(R.id.btn_searchbook_CET4);
        btnSearchBookCET6 = (Button) view.findViewById(R.id.btn_searchbook_CET6);
        btnSearchBookKY = (Button) view.findViewById(R.id.btn_searchbook_KY);
        btnSearchBookTOEFL = (Button) view.findViewById(R.id.btn_searchbook_TOEFL);
        btnSearchBookIELTS = (Button) view.findViewById(R.id.btn_searchbook_IELTS);
        btnSearchBookGRE = (Button) view.findViewById(R.id.btn_searchbook_GRE);
        btnSearchBookGMAT = (Button) view.findViewById(R.id.btn_searchbook_GMAT);

        //默认标题和封面
        for (int i = 0; i < 3; i++) {
            testTitles1.add("title");
            testTitles2.add("title");
            testTitles3.add("title");
        }
        Resources res=getResources();
        Bitmap bitmap=BitmapFactory.decodeResource(res, R.drawable.defualtcover);
        for (int i = 0; i < 3; i++) {
            testImgs1.add(bitmap);
            testImgs2.add(bitmap);
            testImgs3.add(bitmap);
        }


        //初始化备考单词本区域
        btnMoreBookBeikao = (ImageButton) view.findViewById(R.id.btn_morebook_beikao);
        gvBookClass1 = (GridView) view.findViewById(R.id.gv_book_class1);
        gridItemAdapter1 = new GridItemAdapter(testTitles1,testImgs1,this.getActivity());
        gvBookClass1.setAdapter(gridItemAdapter1);
        gvBookClass1.setOnItemClickListener(this);

        //初始化趣味单词本区域
        btnMoreBookQuwei = (ImageButton) view.findViewById(R.id.btn_morebook_quwei);
        gvBookClass2 = (GridView) view.findViewById(R.id.gv_book_class2);
        gridItemAdapter2 = new GridItemAdapter(testTitles2,testImgs2,this.getActivity());
        gvBookClass2.setAdapter(gridItemAdapter2);
        gvBookClass2.setOnItemClickListener(this);

        //初始化精品单词本区域
        btnMoreBookJingpin = (ImageButton) view.findViewById(R.id.btn_morebook_jingpin);
        gvBookClass3 = (GridView) view.findViewById(R.id.gv_book_class3);
        gridItemAdapter3 = new GridItemAdapter(testTitles3,testImgs3,this.getActivity());
        gvBookClass3.setAdapter(gridItemAdapter3);
        gvBookClass3.setOnItemClickListener(this);

        return view;
    }

    //OnClickListener响应函数
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_custom_serchbox2_static://启动搜索界面
            {
                Intent intent = new Intent(this.getActivity(),MoreBookActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_searchbook_CET4://查看四级单词本
            {
                Intent intent = new Intent(this.getActivity(),MoreBookActivity.class);
                //传递标签到下个Activity
                startActivity(intent);
                break;
            }
            case R.id.btn_morebook_beikao://查看备考单词本
            {
                Intent intent = new Intent(this.getActivity(),MoreBookActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_morebook_quwei://查看趣味单词本
            {
                Intent intent = new Intent(this.getActivity(),MoreBookActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_morebook_jingpin://查看精品单词本
            {
                Intent intent = new Intent(this.getActivity(),MoreBookActivity.class);
                startActivity(intent);
                break;
            }
            //onther cases
        }
    }

    //OnItemClickListener响应函数
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this.getActivity(), "item"+position, Toast.LENGTH_SHORT).show();
    }

    @Override//回调函数
    public void processFinish(MessagePack outputPack) {
        List<wordbook> bookList = outputPack.getListwordbook();

        for (int i=0;i<3;i++) {
            testTitles1.set(i,bookList.get(i).getName());
            testImgs1.set(i,Bytes2Bitmap(bookList.get(i).getPicture()));
            System.out.println("回调标题"+testTitles1);
            System.out.println("回调图片"+testImgs1);
        }
        for (int i=0;i<3;i++) {
            testTitles2.set(i,bookList.get(i+3).getName());
            testImgs2.set(i,Bytes2Bitmap(bookList.get(i+3).getPicture()));
        }
        for (int i=0;i<3;i++) {
            testTitles3.set(i,bookList.get(i+6).getName());
            testImgs3.set(i,Bytes2Bitmap(bookList.get(i+6).getPicture()));
        }

        gridItemAdapter1.notifyDataSetChanged();
        gridItemAdapter1 = new GridItemAdapter(testTitles1,testImgs1,this.getActivity());
        gvBookClass1.setAdapter(gridItemAdapter1);

        gridItemAdapter2.notifyDataSetChanged();
        gridItemAdapter2 = new GridItemAdapter(testTitles2,testImgs2,this.getActivity());
        gvBookClass2.setAdapter(gridItemAdapter2);

        gridItemAdapter3.notifyDataSetChanged();
        gridItemAdapter3 = new GridItemAdapter(testTitles3,testImgs3,this.getActivity());
        gvBookClass3.setAdapter(gridItemAdapter3);

    }

    //【工具函数】Bytes2Bitmap
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    //【工具函数】Bitmap2Bytes
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
    //GridItem类
    public class GridItem {
        private String title;
        private Bitmap image;

        public GridItem() {
            super();
        }

        public GridItem(String title,Bitmap image) {
            super();
            this.title = title;
            this.image = image;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setImageID(Bitmap image) {
            this.image = image;
        }

        public Bitmap getImageID() {
            return image;
        }
    }
    //GridItemAdapter类
    public class GridItemAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<GridItem> gridItemList;

        public GridItemAdapter(ArrayList<String> titles, ArrayList<Bitmap> images, Context context) {
            super();
            gridItemList = new ArrayList<GridItem>();
            inflater = LayoutInflater.from(context);
            for (int i = 0;i<images.size();i++){
                GridItem picture = new GridItem(titles.get(i),images.get(i));
                gridItemList.add(picture);
            }
        }

        @Override
        public int getCount() {
            if (gridItemList != null) {
                return gridItemList.size();
            } else {return 0;}
        }

        @Override
        public Object getItem(int position) {
            return gridItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item_book_class1,null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_booksquare_title_class1);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.imgv_booksquare_class1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(gridItemList.get(position).getTitle());
            viewHolder.image.setImageBitmap(gridItemList.get(position).getImageID());
            return convertView;
        }
    }
    //ViewHolder类
    public class ViewHolder {
        public ImageView image;
        public TextView title;
    }
}
