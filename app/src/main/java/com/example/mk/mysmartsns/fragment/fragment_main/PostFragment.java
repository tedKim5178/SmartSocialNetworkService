package com.example.mk.mysmartsns.fragment.fragment_main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.adapter.RecyBigHashAdapter;
import com.example.mk.mysmartsns.config.MyConfig;
import com.example.mk.mysmartsns.interfaces.OnMyApiListener;
import com.example.mk.mysmartsns.network.info.BigHashInfo;
import com.example.mk.mysmartsns.network.info.SmallHashInfo;
import com.example.mk.mysmartsns.network.manager.InteractionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilsoo on 2017-02-13.
 */

public class PostFragment extends android.support.v4.app.Fragment implements View.OnClickListener, TextWatcher {
    private static final String TAG = PostFragment.class.getSimpleName();

    ProgressDialog progressDialog;
    ImageView uploadImage;
    ImageView imgDeleteContent;
    EditText uploadEditText;
    Button uploadButton;
    String imagePath;
    RecyclerView uploadBigHash;
    RecyBigHashAdapter adapter;
    List<BigHashInfo> bigHashList;
    List<SmallHashInfo> smallHashList;

    // image upload check variable -> 어떻게 업로드 할지 고민좀
    boolean isImageUpload;

    // '#' check variable
    ArrayList<SmallHashIndex> smallHashIndexList;
    boolean startTag;
    int startIndex, endIndex;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        uploadImage = (ImageView) view.findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(this);
        imgDeleteContent = (ImageView) view.findViewById(R.id.imgDeleteContent);
        imgDeleteContent.setOnClickListener(this);
        uploadEditText = (EditText) view.findViewById(R.id.uploadEditText);
        uploadEditText.addTextChangedListener(this);
        uploadButton = (Button) view.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(this);
        uploadBigHash = (RecyclerView) view.findViewById(R.id.uploadBigHash);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        uploadBigHash.setLayoutManager(linearLayoutManager);
        bigHashList = new ArrayList<>();
        adapter = new RecyBigHashAdapter(getActivity().getBaseContext(), bigHashList);
        uploadBigHash.setAdapter(adapter);

        smallHashList = new ArrayList<>();
        smallHashIndexList = new ArrayList<>();
        startTag = false;

        // download bighash info
        downloadBigHashInfo();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadImage:
                if (!isImageUpload) {         // 이미지가 안올라가 있을 때
                    onGetImageClick();
                }
                break;
            case R.id.imgDeleteContent:
                isImageUpload = false;
                uploadImage.setImageResource(android.R.drawable.ic_menu_camera);
                break;
            case R.id.uploadButton:             // 이미지 서버로 업로드
                if (isImageUpload)
                    uploadContents();
                else
                    Toast.makeText(getActivity().getBaseContext(), "사진을 선택 하여주세요", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 999) {
                Uri imageUri = data.getData();
                Log.d(TAG, "image url from album " + imageUri);

                // uri로 filepath가져오기
                Cursor cursor = getActivity().getBaseContext().getContentResolver().query(imageUri, null, null, null, null);
                cursor.moveToNext();
                String path = cursor.getString(cursor.getColumnIndex("_data"));
                cursor.close();
                Log.d(TAG, "image path from album " + path);
                imagePath = path;
                Glide.with(getActivity().getBaseContext()).load(imagePath).fitCenter().into(uploadImage);
                isImageUpload = true;
            }
        }
    }

    public void onGetImageClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 999);
    }

    // download the bighash info
    private void downloadBigHashInfo() {
        InteractionManager.getInstance(getActivity().getBaseContext()).requestContentBighashDownload(new OnMyApiListener() {
            @Override
            public void success(Object response) {
                bigHashList = (List<BigHashInfo>) response;
                adapter.addAllData(bigHashList);
            }

            @Override
            public void fail() {

            }
        });
    }

    private void downloadSmallHashInfo(String smallhash) {
        InteractionManager.getInstance(getActivity().getBaseContext()).requestContentSmallhashDownload(smallhash, new OnMyApiListener() {
            @Override
            public void success(Object response) {

            }

            @Override
            public void fail() {

            }
        });
    }

    // upload the image
    private void uploadContents() {
        String description = uploadEditText.getText().toString();

        ArrayList<Integer> bigList = new ArrayList<>();                    // BigHash 정보의 no 을 넘겨준다 (int)
        for (int i = 0; i < bigHashList.size(); i++)                        // check 된 BigHash 리스트에 저장
            if (bigHashList.get(i).isCheck())
                bigList.add(bigHashList.get(i).getBighash_no());

        Gson gson = new Gson();
        String jsonBigHash = gson.toJson(bigList);                             // bighash 정보를 json형태로 바꾼다.

        ArrayList<String> smallList = new ArrayList<>();
        for (int i = 0; i < smallHashIndexList.size(); i++)
            smallList.add(smallHashIndexList.get(i).getHash());
        String jsonSmallHash = gson.toJson(smallList);                          // smallhash 정보를 json형태로 바꾼다.

//        InteractionManager.getInstance(getActivity().getBaseContext()).requestIncreaseTotal(new OnMyApiListener() {
//            @Override
//            public void success(Object response) {
//                Log.d(TAG, "requestIncreaseTotal Success");
//            }
//
//            @Override
//            public void fail() {
//                Log.d(TAG, "requestIncreaseTotal Fail");
//            }
//        });
        InteractionManager.getInstance(getActivity().getBaseContext())
                .requestContentUpload(imagePath, description, String.valueOf(MyConfig.myInfo.getUser_no()), jsonBigHash, jsonSmallHash, new OnMyApiListener() {      // "1"->user_no
                    @Override
                    public void success(Object response) {
                        progressDialog.dismiss();
                        uploadEditText.setText(" ");
                        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, TimelineFragment.newInstance(), "timeline_fragment");
                        transaction.commit();
                    }
                    @Override
                    public void fail() {
                        progressDialog.dismiss();

                    }
                });
        progressDialog = ProgressDialog.show(new ContextThemeWrapper(getActivity()
                , R.style.AppTheme_PopupOverlay), "", "업로드 중입니다. ", false);
    }

    // TextWatcher overriding! -- hashtag기능!
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged() :: " + s + ", start : " + start + ", after : " + after + ", count : " + count);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged() :: " + s + ", start : " + start + ", before : " + before + ", count : " + count);
        if (count == 0) {                                                                          // detect backspace
            if (startTag) {                                                                       //**  태그 달고 있는 중
                if (start == startIndex - 1) {                                                      // # 이 지워진 경우
                    startTag = false;                                                            // 달고있는 태그 무효
                } else {
                    //ToDo. 서버에서 가져오기
                }
            }
            if (smallHashIndexList.size() != 0) {                                                       //**  태그를 안달고 있으나, 계속 지우다 태그를 만날때
                if (start == smallHashIndexList.get(smallHashIndexList.size() - 1).getEndIndex()) {          // 마지막에 저장된 해쉬의 endIndex값과 만난경우
                    startIndex = smallHashIndexList.get(smallHashIndexList.size() - 1).getStartIndex();
                    smallHashIndexList.remove(smallHashIndexList.size() - 1);                                // 기존에 저장된 해쉬태그 삭제후 다시생성
                    // bold 제거
                    uploadEditText.removeTextChangedListener(this);
                    SpannableStringBuilder sb = new SpannableStringBuilder();
                    sb.append(s);
                    sb.clearSpans();         // **
//                   Log.d(TAG, "sb - "+sb.toString()+"check the index - startIndex : " + startIndex + ", endIndex : " + endIndex);
                    for (int i = 0; i < smallHashIndexList.size(); i++) {
//                       Log.d(TAG, "check the index - startIndex : " + smallHashIndexList.get(i).getStartIndex() + ", endIndex : " + smallHashIndexList.get(i).getEndIndex());
                        sb.setSpan(new StyleSpan(Typeface.BOLD), smallHashIndexList.get(i).getStartIndex()-1, smallHashIndexList.get(i).getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    uploadEditText.setText(sb);
                    uploadEditText.addTextChangedListener(this);
                    uploadEditText.setSelection(uploadEditText.getText().length());

                    startTag = true;
                    //ToDo. 서버에서 가져오기
                }
            }

        } else {
            if (!startTag && s.charAt(start) == '#') {
                startIndex = start + 1;
                startTag = true;                                 // '#'을 만나면 flag -> true
            } else if (startTag && (s.charAt(start) == 32 || s.charAt(start) == '#')) {
                endIndex = start;

                if(startIndex == endIndex) {                      // #다음 공백이거나 #다음 또 #인 경우
                    if (s.charAt(start) == 32)                       // 공백을 만났을 때만 startTag -> false
                        startTag = false;
                    else if (s.charAt(start) == '#')                 // # 을만나면 여기서부터 다시 태그 시작
                        startIndex = start + 1;
                    return;
                }

                smallHashIndexList.add(new SmallHashIndex(startIndex, endIndex, s.toString().substring(startIndex, endIndex)));

                if (s.charAt(start) == 32)                       // 공백을 만났을 때만 startTag -> false
                    startTag = false;
                else if (s.charAt(start) == '#')                 // # 을만나면 여기서부터 다시 태그 시작
                    startIndex = start + 1;

                // Test
                for (int i = 0; i < smallHashIndexList.size(); i++)
                    Log.d(TAG, smallHashIndexList.get(i).getHash());

                // text bold로 바꾸기
                uploadEditText.removeTextChangedListener(this);
                SpannableStringBuilder sb = new SpannableStringBuilder();
                sb.append(s);
                sb.clearSpans();
                Log.d(TAG, "sb - " + sb.toString() + "check the index - startIndex : " + startIndex + ", endIndex : " + endIndex);
                for (int i = 0; i < smallHashIndexList.size(); i++) {
                    sb.setSpan(new StyleSpan(Typeface.BOLD), smallHashIndexList.get(i).getStartIndex()-1, smallHashIndexList.get(i).getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                uploadEditText.setText(sb);
                uploadEditText.addTextChangedListener(this);
                uploadEditText.setSelection(uploadEditText.getText().length());

            }
            if (startTag) {
                //ToDo. 서버에서 가져오기
//                new ContentManager(getActivity().getBaseContext(), new OnMyApiListener() {
//                    @Override
//                    public void success(Object response) {
//
//                    }
//
//                    @Override
//                    public void fail() {
//
//                    }
//                }).requestContentSmallhashDownload(s.toString().substring(startIndex));
//                InteractionManager.getInstance(getActivity().getBaseContext()).requestContentSmallhashDownload(s.toString().substring(startIndex), new OnMyApiListener() {
//                    @Override
//                    public void success(Object response) {
//
//                    }
//
//                    @Override
//                    public void fail() {
//
//                    }
//                });
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged() :: " + s);
    }

    private class SmallHashIndex {
        private int startIndex;
        private int endIndex;
        private String hash;

        public SmallHashIndex(int startIndex, int endIndex, String hash) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.hash = hash;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }

}
