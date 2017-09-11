package com.example.words.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.words.R;

import java.util.ArrayList;
import java.util.List;

import db.Wordbook;

/**
 * Created by 6gold on 2017/7/31.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Wordbook> list;
    private List<Boolean> checks;
//    private boolean[] checks;

    public RecyclerViewAdapter(List<Wordbook> list) {
        this.list = list;
        checks = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            checks.add(new Boolean("flase"));
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_bookName, tv_bookAuthor;
        private ImageView iv_cover;
        private CheckBox checkBox;
        private ImageButton btnDrag;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_bookName = (TextView) itemView.findViewById(R.id.tv_booklist3_title);
            tv_bookAuthor = (TextView) itemView.findViewById(R.id.tv_booklist3_publisher);
            iv_cover = (ImageView) itemView.findViewById(R.id.iv_booklist3_cover);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_booklist3_wordbook_name);
            btnDrag = (ImageButton) itemView.findViewById(R.id.btn_drag);
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book3, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Wordbook wordbook = list.get(position);
        holder.tv_bookName.setText(wordbook.getName());
        holder.tv_bookAuthor.setText(wordbook.getAuthor());
        holder.iv_cover.setImageBitmap(new BitmapByte().Bytes2Bitmap(wordbook.getPicture()));
        final int pos = position;//********
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checks.set(position,isChecked);
            }
        });//********
        holder.checkBox.setChecked(checks.get(pos));//********
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setList(List<Wordbook> list) {
        this.list = list;
    }

    public List<Wordbook> getList() {
        return list;
    }

    public List<Boolean> getChecks() {
        return checks;
    }

    public void setItemChecked(int position, boolean b) {
        checks.set(position,b);
    }

    public void setChecks(List<Boolean> checks) {
        this.checks = checks;
    }


}
