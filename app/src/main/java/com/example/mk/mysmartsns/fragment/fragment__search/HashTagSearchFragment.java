package com.example.mk.mysmartsns.fragment.fragment__search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.TimelineAdapter;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;

import java.util.ArrayList;

/**
 * Created by mk on 2017-02-14.
 */

public class HashTagSearchFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    EditText editTextSearchHashtag;
    Button btnSearchHashtag;
    RecyclerView recySearchHashtag;
    TextView textSearchResultMsg;
    TimelineAdapter adapter;
    ArrayList<ContentInfo> list;
    String searchedhHash;

    public static HashTagSearchFragment newInstance(String hash){
        HashTagSearchFragment fragment = new HashTagSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hash", hash);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static HashTagSearchFragment newInstance(){
        return  new HashTagSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            searchedhHash = getArguments().getString("hash");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_hashtag_search, container, false);
        editTextSearchHashtag = (EditText)view.findViewById(R.id.editTextSearchHashtag);
        textSearchResultMsg = (TextView)view.findViewById(R.id.textSearchResultMsg);
        btnSearchHashtag = (Button)view.findViewById(R.id.btnSearchHashtag);
        btnSearchHashtag.setOnClickListener(this);
        recySearchHashtag = (RecyclerView)view.findViewById(R.id.recySearchHashtag);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recySearchHashtag.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new TimelineAdapter(getContext(), list, getActivity().getSupportFragmentManager());
        recySearchHashtag.setAdapter(adapter);

        if(searchedhHash != null) {
            editTextSearchHashtag.setText(searchedhHash);
            getSearchedContents(searchedhHash);
        }


        return view;
    }

    private void getSearchedContents(String hash){
        InteractionManager.getInstance(getActivity().getBaseContext()).requestSearchContents(hash, MyConfig.myInfo.getUser_no(), new OnMyApiListener() {
            @Override
            public void success(Object response) {
                list = (ArrayList<ContentInfo>)response;
                if(list.size() == 0) {
                    textSearchResultMsg.setVisibility(View.VISIBLE);
                    textSearchResultMsg.setText("검색결과가 없습니다.");
                    adapter.setListAll(list);
                    adapter.notifyDataSetChanged();
                }else {
                    textSearchResultMsg.setVisibility(View.GONE);
                    adapter.setListAll(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void fail() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSearchHashtag:
                if(editTextSearchHashtag.getText().toString() != null)
                    getSearchedContents(editTextSearchHashtag.getText().toString());
                break;
        }
    }

    public void setDataChanged(int position, int the_number_of_comment){
        Toast.makeText(getContext(),"tqtqtq", Toast.LENGTH_SHORT).show();
        adapter.contentInfoList.get(position).setContent_comment_count(the_number_of_comment);
        adapter.notifyItemChanged(position);
    }
}
