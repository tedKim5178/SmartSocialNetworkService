package com.example.mk.mysmartsns.fragment.fragment_main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mk.mysmartsns.R;

/**
 * Created by mk on 2017-02-03.
 */

public class SettingFragment extends android.support.v4.app.Fragment {

    ImageView person_who_post;
    TextView text_about_log;
    ImageView image_I_post;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_setting, container, false);

        // 활동 로그를 보여주는 곳이다.
        // 리스트 뷰 형식으로 보여 줄 것이다. 모든 로그 기록을 다 보여주는가..?
        // 2주치만 보여줄것이다..! 그렇다면 서버에서 2주치에 해당하는 정보를 받아와야된다..!
        // 인스타그램도 인터넷 연결이 없으면 데이터를 못받아오는걸로 봐서 서버에서 정보를 받아오는것이다.
        // 서버에서 어떤 정보를 받아와야 할까..? 아무래도 로그를 모아두는 데이터베이스가 필요할까..?
        // 아무튼 서버에서 데이터를 받아왔다고 치자
        // 필요한것은 누가 나의 어떤 사진에 좋아요를 눌렀는지다..!
        // 문자열에 누가 에 대한 정보를 넣고 내 사진 정보를 URL로 받아온다고 가정하자
        // String 배열로 넣어줘야하나..? 서버에서 어떤식으로 넘어올지 잘 모르겠다..!
        // 좋아요인지, 댓글인지, 태그인지 판단해서 어쨋든 문장이 필요하다

        // 사진 + 텍스트 + 사진
        person_who_post = (ImageView)view.findViewById(R.id.person_who_post);
        text_about_log = (TextView) view.findViewById(R.id.text_about_log);
        image_I_post = (ImageView)view.findViewById(R.id.image_I_post);

        return view;
    }
}
