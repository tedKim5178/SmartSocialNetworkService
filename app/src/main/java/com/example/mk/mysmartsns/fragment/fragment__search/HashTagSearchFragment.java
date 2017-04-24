package com.example.mk.mysmartsns.fragment.fragment__search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.ContentInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

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
    ArrayList<BigHashInfo> bighash_list;
    int smallhash_no;
    int bighash_no;
    int judge = 0;
    public static HashTagSearchFragment newInstance(String hash){
        HashTagSearchFragment fragment = new HashTagSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hash", hash);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static HashTagSearchFragment newInstance(String hash, ArrayList<BigHashInfo> bighash_list, int bighash_no, int smallhash_no, int judge){
        HashTagSearchFragment fragment = new HashTagSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hash", hash);
        bundle.putInt("smallhash_no", smallhash_no);

        Log.d(TAG, "빅해쉬카운트테스트 bighash_no newInstance : " + bighash_no);
        bundle.putInt("bighash_no", bighash_no);
        bundle.putInt("judge", judge);
        bundle.putSerializable("bighash_list", bighash_list);
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
            this.bighash_no = getArguments().getInt("bighash_no");

            this.smallhash_no = getArguments().getInt("smallhash_no");
            this.judge = getArguments().getInt("judge");
            Log.d(TAG, "리스트테스트 여기는?");

            if(judge == 0){

            }else{
                // 스몰해쉬일때
                bighash_list = (ArrayList<BigHashInfo>)getArguments().getSerializable("bighash_list");
            }
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
            // 네비게이션 타고 온 부분 searchedHash가 bighash인지 smallhash인지 알아야되잖아
            editTextSearchHashtag.setText(searchedhHash);
            getContentsFromNavigation(searchedhHash);
        }


        return view;
    }

    // 네비게이션에서 빅해쉬랑 스몰해쉬 어떻게 구분해주지..?
    // 여기선 그냥 hash가 들어간거 전부 올려주는 걸로!! 즉 where bighash or smallhash = hash 대충 이런식..!
    private void getContentsFromNavigation(String hash){
        // judge에 따라서
        if(judge == 0){
            // 빅해쉬 일경우
            InteractionManager.getInstance(getActivity().getBaseContext()).requestCountBigHashIncrease(MyConfig.myInfo.getUser_no(), bighash_no, new OnMyApiListener() {
                @Override
                public void success(Object response) {
                    Log.d(TAG, "빅해쉬카운트테스트 success " );
                }
                @Override
                public void fail() {
                    Log.d(TAG, "빅해쉬카운트테스트 fail " );
                }
            });
        }else{
            // 스몰해쉬 일경우
            Gson gson = new Gson();
            ArrayList arrayList = new ArrayList();
            for(int i=0; i<bighash_list.size(); i++){
                arrayList.add(bighash_list.get(i).getBighash_no());
            }
            String bighash = gson.toJson(arrayList);
            Log.d(TAG, "스몰해쉬카운트테스트 변수들 " + bighash + "," + smallhash_no);
            InteractionManager.getInstance(getActivity().getBaseContext()).requestCountSmallHashIncrease(MyConfig.myInfo.getUser_no(), bighash, smallhash_no, new OnMyApiListener() {
                @Override
                public void success(Object response) {
                    Log.d(TAG, "스몰해쉬카운트테스트 success " );
                }

                @Override
                public void fail() {
                    Log.d(TAG, "스몰해쉬카운트테스트 fail " );
                }
            });
        }


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
                    // 검색 이벤트
                    // hash랑 user_no 넘겨줌.
                    getContentsFromSearched(editTextSearchHashtag.getText().toString());
                break;
        }
    }

    public void getContentsFromSearched(String hash){

        InteractionManager.getInstance(getActivity().getBaseContext()).requestCountSearchedHashIncrease(MyConfig.myInfo.getUser_no(), hash, new OnMyApiListener() {
            @Override
            public void success(Object response) {
                Log.d(TAG, "빅해쉬카운트테스트 success " );
            }
            @Override
            public void fail() {
                Log.d(TAG, "빅해쉬카운트테스트 fail " );
            }
        });

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
    public void setDataChanged(int position, int the_number_of_comment){
        Toast.makeText(getContext(),"tqtqtq", Toast.LENGTH_SHORT).show();
        adapter.contentInfoList.get(position).setContent_comment_count(the_number_of_comment);
        adapter.notifyItemChanged(position);
    }
}
