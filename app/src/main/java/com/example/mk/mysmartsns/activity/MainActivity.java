package com.example.mk.mysmartsns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mk.mysmartsns.BottomNavigationViewHelper;
import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.fragment.fragment__search.HashTagSearchFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.LogFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.MyTimelineFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.PostFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.SearchFragment;
import com.example.mk.mysmartsns.fragment.fragment_main.TimelineFragment;

import butterknife.ButterKnife;

/**
 * Created by mk on 2017-02-02.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    //    private TimelineAdapter postAdapter;
//    private RecyclerView recyclerView;
//    android.app.FragmentManager fm;
//    android.app.FragmentTransaction fragmentTransaction;
//    Fragment test;
//    ActionBar actionBar;
//
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
    private android.support.v4.app.Fragment fragment;
    android.support.v4.app.FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Butterknife bind
        ButterKnife.bind(this);

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
                                if (!fragment.getTag().equals("post_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, new PostFragment(), "post_fragment");
                                    transaction.commit();
                                }
                                break;
                            case R.id.action_log:
                                if(bottomNavigationView.getMenu().getItem(0).isChecked())
                                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                                if (!fragment.getTag().equals("log_fragment")) {
//                                    if (fragment.getTag().equals("timeline_fragment")) {
//                                        transaction.addToBackStack(null);
//                                    }
                                    transaction.remove(fragment).replace(R.id.frame_layout, LogFragment.newInstance(), "log_fragment");
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
                        }

                        return true;
                    }
                }
        );
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
        Log.d(TAG, "오는지dftest");
        if(requestCode == 888){
            if(resultCode == RESULT_OK){
                Log.d(TAG, "onActivityReult : here comes");
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, MyTimelineFragment.newInstance(), "nav_my_timeline_fragment");
                transaction.addToBackStack("");
                transaction.commit();
            }
        }else if(requestCode == 0){
            Log.d(TAG, "오는지dftest" + requestCode);
            if(resultCode == RESULT_OK){
                Log.d(TAG, "오는지dftest" + requestCode);
//                Log.d(TAG, "오는지test" + data);
                int position = data.getIntExtra("position", 0);
                int the_number_of_comment = data.getIntExtra("the_number_of_comments", 0);
                Log.d(TAG, "오는지 onActiviyResult : " + position + " , " + the_number_of_comment);
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



}

