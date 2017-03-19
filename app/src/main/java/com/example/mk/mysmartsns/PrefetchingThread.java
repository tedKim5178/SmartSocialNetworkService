package com.example.mk.mysmartsns;

import android.util.Log;

/**
 * Created by mk on 2017-03-17.
 */

public class PrefetchingThread implements Runnable{

    private static final String TAG = PrefetchingThread.class.getSimpleName();

    private final int STATE_BEFORE_RUN =0;
    private final int STATE_RUNNING = 1;
    private final int STATE_PAUSE = 2;
    private final int STATE_STOPPED = 3;

    private int status = STATE_BEFORE_RUN;

    Thread thread;
    @Override
    public void run() {

        Log.d(TAG, "Prefetching Thread test run is running");
        while(true){
            if(status == STATE_PAUSE){
                // pause면 아무것도 하지 말고
                try {
                    Log.d(TAG, "Prefetching Test Pause");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(status == STATE_STOPPED){
                Log.d(TAG, "Prefetching Test Stop");
                // stop이면 나가고
                break;
            }else{
                // 나머지는 다 작업하고
                processTask();
            }
        }
        Log.d(TAG, "Prefetching is stopped");
    }



    public int getStatus(){
        return status;
    }

    public void resume(){
        status = STATE_RUNNING;
    }

    public void pause(){
        status = STATE_PAUSE;
    }

    public void start(){
        if(status != STATE_BEFORE_RUN){
            // 에러
        }else{
            thread = new Thread(this);
            thread.start();
            status = STATE_RUNNING;
        }
    }

    public void stop(){
        if(status != STATE_STOPPED){
            thread.interrupt();
            status = STATE_STOPPED;
        }
    }

    public void processTask(){

    }
}
