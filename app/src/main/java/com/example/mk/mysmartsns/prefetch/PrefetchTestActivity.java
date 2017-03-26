package com.example.mk.mysmartsns.prefetch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mk.mysmartsns.R;
import com.example.mk.mysmartsns.config.APIConfig;
import com.example.mk.mysmartsns.config.PrefetchConfig;

import java.util.Iterator;

public class PrefetchTestActivity extends AppCompatActivity {
    private final String TAG = PrefetchTestActivity.class.getSimpleName();
    TextView textPrefetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefetch_test);
//      'thumbnail_contents/' 자르고 보내야함!
        // ToDo. gilsoo_이런식으로 큐에다가 받을 이미지 담아논다. 그리고 그냥 시작하면 큐에서 하나씩 빼와서 받아옴, 완료되면 큐에서 제거
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-02-19-17-45-18]20150109_125245.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-03-04-16-19-02]1.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-03-04-16-21-31]1217780500_tMBpgmLU_1319513078_11.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-03-04-16-28-24]7.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-02-15-08-49-30]00995_HD.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-02-15-08-53-18]01410_HD.jpg");
        PrefetchConfig.prefetching_queue.offer("thumb_[2017-02-16-02-42-49]10848_HD.jpg");
        textPrefetch = (TextView)findViewById(R.id.textPrefetch);
        Iterator<String> iterator = PrefetchConfig.prefetching_queue.iterator();
        String text = "";
        while(iterator.hasNext()){
            text += iterator.next() + "\n";
        }
        textPrefetch.setText(text);

    }

    public void onPrefetchBtnClick(View v){
        switch(v.getId()){
            case R.id.btnStartPrefetch:
                PrefetchDownload.newInstance().initUrl(APIConfig.prefetchUrl + PrefetchConfig.prefetching_queue.peek(), getSharedPreferences(PrefetchConfig.PREFS_NAME, 0)).startPrefetching();
                break;
            case R.id.btnStopPrefetch:
                PrefetchDownload.newInstance().stopPrefetching();
                break;
            case R.id.btnChangeText:
                Iterator<String> iterator = PrefetchConfig.prefetching_queue.iterator();
                String text= "";
                while(iterator.hasNext()){
                    text += iterator.next() + "\n";
                }
                textPrefetch.setText(text);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Prferences 에 담을 필요 없을것 같음 -> 종료되고 다시 시작하면 프리페칭 받을 목록도 갱신될 확률이 매우 높기때문!
        SharedPreferences settings = getSharedPreferences(PrefetchConfig.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putInt(PrefetchConfig.PREFS_KEY_PROGRESS, progressBar.getProgress());
        editor.commit();
    }

}
