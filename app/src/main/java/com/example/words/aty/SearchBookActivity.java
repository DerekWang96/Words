package com.example.words.aty;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.words.AsyncMsgRes;
import com.example.words.ExMsgTask;
import com.example.words.MessagePack;
import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

import db.Wordbook;

/**
 * 查看更多单词本的Activity，由BooksFragment里面的空间触发
 * Created by 6gold on 2017/3/27.
 */

public class SearchBookActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener,AsyncMsgRes {

    /*数据--------------------------------------------------------*/
    private ArrayList<String> bookTitles = new ArrayList<String>();//单词本搜索结果——标题
    private ArrayList<String> bookAuthors = new ArrayList<String>();//单词本搜索结果——作者
    private ArrayList<Bitmap> bookCovers = new ArrayList<Bitmap>();//单词本搜索结果——封面
    private ArrayList<Integer> bookWordNumbers = new ArrayList<Integer>();//单词本搜索结果——词汇量
    private ArrayList<Integer> bookFavNumbers = new ArrayList<Integer>();//单词本搜索结果——收藏量
    private ArrayList<Integer> bookDownNumbers = new ArrayList<Integer>();//单词本搜索结果——下载量

    /*成员变量--------------------------------------------------------*/
    //topbar
    private ImageButton btnReturn;
    //customSearchBox2
    private EditText etBookName;
    private ImageButton btnSearchBook;
    //hotTags
    private Button btnSearchBookCET4;
    private Button btnSearchBookCET6;
    private Button btnSearchBookKY;
    private Button btnSearchBookTOEFL;
    private Button btnSearchBookIELTS;
    private Button btnSearchBookGRE;
    private Button btnSearchBookGMAT;
    //查询结果ListView
    private ListView lvSearchBookResult;
    private ListItemAdapter listItemAdapter;


    /*相关函数--------------------------------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);

        initViews();//初始化控件
        initEvents();//初始化事件
    }

    public void initViews() {

        //初始化topbar（抽屉按钮等）

        //初始化单词本搜索框
        etBookName = (EditText) findViewById(R.id.et_input_book);
        etBookName.addTextChangedListener(bookNameWatcher);
        btnSearchBook = (ImageButton) findViewById(R.id.btn_search_book);

        //初始化hotTags
        btnSearchBookCET4 = (Button) findViewById(R.id.btn_searchbook2_CET4);
        btnSearchBookCET6 = (Button) findViewById(R.id.btn_searchbook2_CET6);
        btnSearchBookKY = (Button) findViewById(R.id.btn_searchbook2_KY);
        btnSearchBookTOEFL = (Button) findViewById(R.id.btn_searchbook2_TOEFL);
        btnSearchBookIELTS = (Button) findViewById(R.id.btn_searchbook2_IELTS);
        btnSearchBookGRE = (Button) findViewById(R.id.btn_searchbook2_GRE);
        btnSearchBookGMAT = (Button) findViewById(R.id.btn_searchbook2_GMAT);

        //初始化查询结果
        lvSearchBookResult = (ListView) findViewById(R.id.lv_searchbook_result);
        listItemAdapter = new ListItemAdapter(bookTitles,bookAuthors,bookCovers,
                bookWordNumbers,bookFavNumbers,bookDownNumbers,this);
        lvSearchBookResult.setAdapter(listItemAdapter);
        lvSearchBookResult.setOnItemClickListener(this);

        //初始化数据
        Resources res=getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.defualtcover);
//        for (int i = 0; i < 10; i++) {
//            bookTitles.add("title");
//            bookAuthors.add("搞事组");
//            bookCovers.add(bitmap);
//            bookWordNumbers.add(0);
//            bookFavNumbers.add(0);
//            bookDownNumbers.add(0);
//        }

    }

    public void initEvents() {
        btnSearchBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_book:
                //do something
                ExMsgTask MTask = new ExMsgTask();
                MessagePack mp = new MessagePack();
                mp.setOrderType("0003");
                mp.setTaskType("0001");
                MTask.setMessagePack(mp);
                MTask.setDelegate(SearchBookActivity.this);
                MTask.execute();
                //获取单词本的相关信息

                break;
            //onther cases
        }
    }

    //OnItemClickListener响应函数
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "item"+position, Toast.LENGTH_SHORT).show();
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
    //ListItem类
    public class ListItem {
        private String title;
        private String publisher;
        private Bitmap cover;
        private int numOfWords;
        private int numOfFavorite;
        private int numOfDownload;

        public ListItem() { super(); }

        public ListItem(String title,String publisher,Bitmap cover,
                        int numOfWords,int numOfFavorite,int numOfDownload) {
            super();
            this.title = title;
            this.publisher = publisher;
            this.cover = cover;
            this.numOfWords = numOfWords;
            this.numOfFavorite = numOfFavorite;
            this.numOfDownload = numOfDownload;
        }

        public String getTitle() { return title;}
        public String getPublisher() { return publisher;}
        public Bitmap getCover() { return cover;}
        public int getNumOfWords() { return numOfWords;}
        public int getNumOfFavorite() { return numOfFavorite;}
        public int getNumOfDownload() { return numOfDownload;}
    }

    //GridItemAdapter类
    public class ListItemAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<ListItem> listItems;

        public ListItemAdapter (ArrayList<String> titles, ArrayList<String> publishers, ArrayList<Bitmap> covers,
                                ArrayList<Integer> wordNumbers, ArrayList<Integer> favNumbers, ArrayList<Integer> downNumbers, Context context) {
            super();
            listItems = new ArrayList<ListItem>();
            inflater = LayoutInflater.from(context);
            for (int i=0;i<titles.size();i++) {
                ListItem book = new ListItem(titles.get(i),publishers.get(i),covers.get(i),
                        wordNumbers.get(i), favNumbers.get(i),downNumbers.get(i));
                listItems.add(book);
            }
        }

        @Override
        public int getCount() {
            if (listItems != null) {
                return listItems.size();
            } else {return 0;}
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_book,null);
                viewHolder = new ViewHolder();
                viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.iv_booklist_cover);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_booklist_title);
                viewHolder.tvPublisher = (TextView) convertView.findViewById(R.id.tv_booklist_publisher);
                viewHolder.tvNumOfWords = (TextView) convertView.findViewById(R.id.tv_booklist_amount);
                viewHolder.tvNumOfFavorite = (TextView) convertView.findViewById(R.id.tv_booklist_favorite);
                viewHolder.tvNumOfDownload = (TextView) convertView.findViewById(R.id.tv_booklist_download);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(listItems.get(position).getTitle());
            viewHolder.ivCover.setImageBitmap(listItems.get(position).getCover());
            viewHolder.tvPublisher.setText(listItems.get(position).getPublisher());
            viewHolder.tvNumOfWords.setText(String.valueOf(listItems.get(position).getNumOfWords()));
            viewHolder.tvNumOfFavorite.setText(String.valueOf(listItems.get(position).getNumOfFavorite()));
            viewHolder.tvNumOfDownload.setText(String.valueOf(listItems.get(position).getNumOfDownload()));
            return convertView;
        }
    }

    //ViewHolder类
    public class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvPublisher;
        public TextView tvNumOfWords;
        public TextView tvNumOfFavorite;
        public TextView tvNumOfDownload;
    }

    //输入书名监听
    TextWatcher bookNameWatcher = new TextWatcher() {
        private String temp;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            temp = s.toString();
            System.out.println("改变的String："+s);
            ExMsgTask MTask = new ExMsgTask();
            MessagePack mp = new MessagePack();
            if(!temp.equals("")){
                clearResults();
                mp.strPack.add(temp);
                System.out.println(mp.strPack.get(0));
                mp.setTaskType("0002");
                mp.setOrderType("0003");
                MTask.setMessagePack(mp);
                MTask.setDelegate(SearchBookActivity.this);
                MTask.execute();
            }
            else {
                System.out.println("empty string.");
            }

        }
    };
    @Override//回调函数
    public void processFinish(MessagePack outputPack) {
        List<Wordbook> bookList = outputPack.getListwordbook();
        System.out.println("bookList size:"+bookList.size());
        Resources res=getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.defualtcover);
        for (int i = 0; i < bookList.size(); i++) {
            bookTitles.add("title");
            bookAuthors.add("搞事组");
            bookCovers.add(bitmap);
            bookWordNumbers.add(0);
            bookFavNumbers.add(0);
            bookDownNumbers.add(0);
            bookTitles.set(0,bookList.get(0).getName());
            bookAuthors.set(0,bookList.get(0).getAuthor());
            bookCovers.set(0,Bytes2Bitmap(bookList.get(0).getPicture()));
            bookWordNumbers.set(0,bookList.get(0).getWordnumber());
            bookFavNumbers.set(0,bookList.get(0).getCollectnumber());
            bookDownNumbers.set(0,bookList.get(0).getDownnumber());
        }
        listItemAdapter.notifyDataSetChanged();
        listItemAdapter = new ListItemAdapter(bookTitles,bookAuthors,bookCovers,
                bookWordNumbers,bookFavNumbers,bookDownNumbers,this);
        lvSearchBookResult.setAdapter(listItemAdapter);
    }

    public void clearResults() {
        bookTitles.clear();
        bookAuthors.clear();
        bookCovers.clear();
        bookWordNumbers.clear();
        bookFavNumbers.clear();
        bookDownNumbers.clear();
    }
}
