package com.example.words.util;

/**
 * Created by 展鹏 on 2017/7/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.words.R;


public class SelectPicPopupWindow extends PopupWindow {

    private Button takePhotoBtn, pickPhotoBtn, cancelBtn;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_pickphoto, null);
        takePhotoBtn = (Button) mMenuView.findViewById(R.id.takePhotoBtn);
        pickPhotoBtn = (Button) mMenuView.findViewById(R.id.pickPhotoBtn);
        cancelBtn = (Button) mMenuView.findViewById(R.id.cancelBtn);
        // 锟斤拷锟矫帮拷钮锟斤拷锟斤拷
        cancelBtn.setOnClickListener(itemsOnClick);
        pickPhotoBtn.setOnClickListener(itemsOnClick);
        takePhotoBtn.setOnClickListener(itemsOnClick);

        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷View
        this.setContentView(mMenuView);
        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷目锟?
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷母锟?
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷傻锟斤拷
        this.setFocusable(true);
        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟藉动锟斤拷效锟斤拷
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实锟斤拷锟斤拷一锟斤拷ColorDrawable锟斤拷色为锟斤拷透锟斤拷
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 锟斤拷锟斤拷SelectPicPopupWindow锟斤拷锟斤拷锟斤拷锟斤拷谋锟斤拷锟?
        this.setBackgroundDrawable(dw);
        // mMenuView锟斤拷锟絆nTouchListener锟斤拷锟斤拷锟叫断伙拷取锟斤拷锟斤拷位锟斤拷锟斤拷锟斤拷锟窖★拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟劫碉拷锟斤拷锟斤拷
        mMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}

