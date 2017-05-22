package com.example.words.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.words.R;

import java.util.List;

import db.ACID;
import db.Word;

/**
 * Created by 6gold on 2017/5/8.
 */

public class TestActivity extends Activity implements View.OnClickListener {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn = (Button) findViewById(R.id.btn_test);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
            {
                /*操作*/
                ACID acid = new ACID(v.getContext());
                List<Word> list = acid.getwordfromwb("test1",null);
                for (int i = 0;i<list.size();i++) {
                    System.out.println("第"+i+"个单词："+list.get(i));
                }
                break;
            }
        }
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
