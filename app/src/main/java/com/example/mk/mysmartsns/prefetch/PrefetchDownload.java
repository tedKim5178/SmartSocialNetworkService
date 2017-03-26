package com.example.mk.mysmartsns.prefetch;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.mk.mysmartsns.config.PrefetchConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by gilsoo on 2017-03-26.
 */
public class PrefetchDownload {

    private static final String TAG = PrefetchDownload.class.getSimpleName();
    private String urlStr;
    private String filename;
    private String fileExtension;
//    private final String PREFS_NAME = PrefetchConfig.PREFS_NAME;
//    private final String PREFS_KEY_PROGRESS = PrefetchConfig.PREFS_KEY_PROGRESS;
//    private final String Local_Name = PrefetchConfig.Local_Name;
    private File fileDir;
    private ResumeDownloader mDownloader;
    private SharedPreferences settings;

    private AsyncTask asyncTask;
    private boolean asytaskFinished = true;
    private static PrefetchDownload prefetchDownload;


    public PrefetchDownload() {
        fileDir = new File(String.valueOf(Environment.getExternalStorageDirectory()) + PrefetchConfig.Local_Name);
        mDownloader = new ResumeDownloader(mDownloader.PAUSE);

    }

    public static PrefetchDownload newInstance() {
        if(prefetchDownload == null)
            prefetchDownload = new PrefetchDownload();
        return prefetchDownload;
    }

    /**
     * 인자 url정보, SharePreferences
     * @param urlStr
     * @param settings
     */
    public PrefetchDownload initUrl(String urlStr, SharedPreferences settings) {
        this.urlStr = urlStr;
        this.settings = settings;
        String file = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        fileExtension = file.substring(file.lastIndexOf("."));
        filename = file.substring(0, file.lastIndexOf("."));
        //int progress = settings.getInt(PREFS_KEY_PROGRESS, 0);
        Log.d(TAG, "file extension: " + fileExtension + "; file name: " + filename);
        return prefetchDownload;
    }

    public void startPrefetching() {
        if (asytaskFinished && mDownloader.getStatus() != mDownloader.DOWNLOADING) {
            startDownload();
        }
        Log.d(TAG, "status: " + mDownloader.getStatusStr());

    }

    public void stopPrefetching() {
        if (mDownloader.getStatus() == mDownloader.DOWNLOADING) {
            mDownloader.setStatus(mDownloader.PAUSE);
            asyncTask.cancel(true);
            Log.d(TAG, "paused & asyncTask is cancelled - " + asyncTask.isCancelled());
        }
        Log.d(TAG, "status: " + mDownloader.getStatusStr());
    }

    private void startDownload() {
        asytaskFinished = false;
        asyncTask = new AsyncTask<URL, Void, ResumeDownloader>() {

            protected ResumeDownloader doInBackground(URL... params) {
                try {
//                    Log.d(TAG, "fileDir : " + fileDir + ", fileDir(Abs) : " + fileDir.getAbsolutePath() + ", filename : " + filename + ", fileExtension : " +fileExtension);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    mDownloader.downloadFile(urlStr,
                            (new File(fileDir.getAbsolutePath(), filename + fileExtension)).getAbsolutePath(),
                            new ResumeDownloadListener() {
                                public void progressUpdate() {
//                                    publishProgress();
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG, "File not found !");
                } catch (IOException e) {
                    e.printStackTrace();
                    mDownloader.setStatus(mDownloader.ERROR);
                    Log.d(TAG, e.getMessage().substring(0, 50));
                }
                return mDownloader;
            }

            protected void onProgressUpdate() {
                super.onProgressUpdate();
            }

            protected void onPostExecute(ResumeDownloader o) {
                super.onPostExecute(o);
                Log.d(TAG, "Async task finished");
                PrefetchConfig.prefetching_queue.poll();        // 완료된 프레페칭 콘텐츠 큐에서 제거
                asytaskFinished = true;
            }

            protected void onCancelled() {
                super.onCancelled();
                Log.d(TAG, "async Task is cancelled: " + asyncTask.isCancelled());
                asytaskFinished = true;
            }
        }.execute();
    }

}
