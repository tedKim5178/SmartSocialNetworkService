package com.example.mk.mysmartsns.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.mysmartsns.BottomNavigationViewHelper;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.PrefetchConfig;
import com.example.mk.mysmartsns.fragment.fragment__search.HashTagSearchFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.PostFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.SearchFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.SettingFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.TimelineFragment;
import com.example.mk.mysmartsns.prefetch.Message;

import java.io.File;

/**
 * Created by mk on 2017-02-02.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private android.support.v4.app.Fragment fragment;
    android.support.v4.app.FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    private long backKeyPressedTime = 0;

    private String urlStr = "http://114.70.21.116:3001/prefetch/original/dodo.jpg";
    private String filename;
    private String fileExtension;

    private static LinearLayout headerLayout;
    private static ProgressBar progressPrefetch;
    private static TextView textPrefetch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // delete files prefetched before
        deletePrefetchedFiles();

        headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
        progressPrefetch = (ProgressBar)findViewById(R.id.progressPrefetch);
        progressPrefetch.setProgress(0);
        progressPrefetch.setMax(100);
        progressPrefetch.setIndeterminate(false);
        textPrefetch = (TextView)findViewById(R.id.textPrefetch);
        // ** setting progress mode
        PrefetchConfig.isPrefetchingShow = (getSharedPreferences(PrefetchConfig.NAME, Context.MODE_PRIVATE)).getBoolean(PrefetchConfig.PREFETCH_SHOW, false);
        onShowProgressbar(PrefetchConfig.isPrefetchingShow);


        // Butterknife bind
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        fragment = fragmentManager.findFragmentById(R.id.frame_layout);
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.action_timeline:
                                if (!fragment.getTag().equals("timeline_fragment")) {
                                    transaction.remove(fragment).replace(R.id.frame_layout, TimelineFragment.newInstance(), "timeline_fragment");
                                    transaction.commit();
                                }
                                break;
                            case R.id.action_search:
                                if(bottomNavigationView.getMenu().getItem(0).isChecked())
                                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                if (!fragment.getTag().equals("search_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, SearchFragment.newInstance(), "search_fragment");
                                    transaction.commit();
                                }
                                break;
                            case R.id.action_post:
                                if(bottomNavigationView.getMenu().getItem(0).isChecked())
                                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                Log.d(TAG, "프레그먼트 테스트 : " + fragment.getTag().equals("post_fragment"));
                                if (!fragment.getTag().equals("post_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, new PostFragment(), "post_fragment");
                                    transaction.commit();
                                }
                                break;

                            case R.id.action_mytimeline:
                                if(bottomNavigationView.getMenu().getItem(0).isChecked())
                                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                if (!fragment.getTag().equals("my_timeline_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "my_timeline_fragment");
                                    transaction.commit();
                                }
                                break;

                            case R.id.action_setting:
                                if(bottomNavigationView.getMenu().getItem(0).isChecked())
                                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                if (!fragment.getTag().equals("setting_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, SettingFragment.newInstance(), "setting_fragment");
                                    transaction.commit();

                                }
                                break;
                        }
                        return true;
                    }
                }
        );


        // 프레그먼트
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.frame_layout);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout, TimelineFragment.newInstance(), "timeline_fragment");
        transaction.commit();
    }
    @Override
    public void onBackPressed(){
//        Toast.makeText(this, fragment.getTag() +", "+ fragmentManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
        if(!fragmentManager.findFragmentById(R.id.frame_layout).getTag().equals("timeline_fragment")){
            if(fragmentManager.findFragmentById(R.id.frame_layout).getTag().equals("nav_search_fragment")
                    || fragmentManager.findFragmentById(R.id.frame_layout).getTag().equals("nav_my_timeline_fragment")){
//                fragmentManager.beginTransaction().setCustomAnimations(R.anim.activity_transition_end_enter, R.anim.activity_transition_end_exit);
                fragmentManager.popBackStack();
                return;
            }
            fragmentManager.beginTransaction().replace(R.id.frame_layout, TimelineFragment.newInstance(),"timeline_fragment")
                    .setCustomAnimations(R.anim.activity_transition_end_enter, R.anim.activity_transition_end_exit).commit();
            Menu menu = bottomNavigationView.getMenu();
            for(int i=0; i<menu.size(); i++) {
                if(i==0)
                    menu.getItem(i).setChecked(true);
                else
                    menu.getItem(i).setChecked(false);
            }
        }
        else{
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "한번더 누르면 종료합니다", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                Toast.makeText(getApplicationContext(), "한번더 누르면 종료합니다", Toast.LENGTH_SHORT).cancel();
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 888){
            if(resultCode == RESULT_OK){
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "nav_my_timeline_fragment");
                transaction.addToBackStack("");
                transaction.commit();
            }
        }else if(requestCode == 0){
            if(resultCode == RESULT_OK){
                int position = data.getIntExtra("position", 0);
                int the_number_of_comment = data.getIntExtra("the_number_of_comments", 0);
                fragmentManager = getSupportFragmentManager();
                fragment = fragmentManager.findFragmentById(R.id.frame_layout);
//                ((TimelineFragment)fragment).setDataChanged(position, the_number_of_comment);

                if(fragment instanceof TimelineFragment){
                    ((TimelineFragment)fragment).setDataChanged(position, the_number_of_comment);
                }else if(fragment instanceof HashTagSearchFragment){
                    ((HashTagSearchFragment)fragment).setDataChanged(position, the_number_of_comment);
                }else if(fragment instanceof MyTimelineFragment){
                    ((MyTimelineFragment)fragment).setDataChanged(position, the_number_of_comment);
                }
            }
        }
    }

    private void initUrl(){
        String file = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        fileExtension = file.substring(file.lastIndexOf("."));
        filename = file.substring(0, file.lastIndexOf("."));
        Log.d(TAG, "프리페칭테스트 : " + fileExtension + " , " + filename);
    }

    public void deletePrefetchedFiles(){
        String filePath = String.valueOf(Environment.getExternalStorageDirectory()) + PrefetchConfig.Local_Name;
        File file = new File(filePath);
        if(file != null){
            if(file.isDirectory()){
                String[] fileList = file.list();
                for(int i=0; i<fileList.length; i++){
                    File fileDelete = new File(filePath + "/" + fileList[i]);
                    fileDelete.delete();
                }
            }
        }
    }

    public static void updateProgressBar(Message message){
        if(progressPrefetch != null)
          progressPrefetch.setProgress((int)((message.getLength()*100)/message.getTotalLength()));
        textPrefetch.setText(message.getFileName());
    }

    public static void onShowProgressbar(boolean isShow){
        if(isShow){
//            headerLayout.setVisibility(View.VISIBLE);
            progressPrefetch.setVisibility(View.VISIBLE);
            textPrefetch.setVisibility(View.VISIBLE);
        }else{
//            headerLayout.setVisibility(View.INVISIBLE);
            progressPrefetch.setVisibility(View.INVISIBLE);
            textPrefetch.setVisibility(View.INVISIBLE);
        }
    }

}


