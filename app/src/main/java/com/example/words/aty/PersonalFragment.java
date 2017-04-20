package com.example.words.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.words.R;

/**
 * MainActivity的PersonalFragment子类
 * Created by 6gold on 2017/3/4.
 */

public class PersonalFragment extends Fragment {

    /*相关函数--------------------------------------------------------*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_personal,container,false);
        return view;
    }
}
