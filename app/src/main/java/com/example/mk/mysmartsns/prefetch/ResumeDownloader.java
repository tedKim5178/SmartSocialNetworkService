package com.example.mk.mysmartsns.prefetch;

import android.util.Log;

import com.example.mk.mysmartsns.config.PrefetchConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by gilsoo on 2017-03-26.
 */
public class ResumeDownloader {
    private static final String TAG = ResumeDownloader.class.getSimpleName();
    private int timeout;
    private File downloadedFile;
    private boolean startNewDownload;                   // 새로 시작되거나, 이어 받거나 확인 flag
    private boolean isCompletedDownload;                // 이미 파일이 로컬에 존재하는지 확인 flag

    private long fileLength = 0;


    // CONSTANT
    public static final int DOWNLOADING = 0;
    public static final int COMPLETE = 1;
    public static final int PAUSE = 2;
    public static final int ERROR = 3;
    public static final int BUFFER_SIZE = 8 * 1024;
    /**
     * downloading status: downloading; complete; pause; error;
     */
    private int status;
    private String[] statuses;

//    private ProgressBarListener progressBarListener;

    public ResumeDownloader(int status) {
        timeout = 9000;                     // 9초동안 응답없으면 종료
        startNewDownload = true;
        this.status = status;
        statuses = new String[]{"Downloading", "Complete", "Pause", "Error"};
    }

    public void downloadFile(String urlStr, String toFile, ResumeDownloadListener downloadListener, ProgressBarListener progressBarListener) throws IOException {
        Log.d(TAG, toFile);
        prepareDownload(urlStr, toFile, downloadListener);
        HttpURLConnection connection = createConnection(urlStr, downloadListener);
        setStatus(DOWNLOADING);
        if (!startNewDownload) {                // 새로받는게 아니라면 서버로부터 범위 가져와야 함 - 현재 로컬에 있는 파일 크기를 보내준다.
            connection.setRequestProperty("Range", String.valueOf(downloadedFile.length()));
        }
//        downloadListener.progressUpdate();
        InputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        FileOutputStream writer;
        long progressLength = 0;
        if (!startNewDownload && !isCompletedDownload) {                    // 이어 받는 거라면
            progressLength = downloadedFile.length();
//  //        downloadListener.progressUpdate();
            writer = new FileOutputStream(toFile, true);                    // append flag = true; (이어서 쓰기)
        } else {                                    // 새로 받는 거라면
//            downloadListener.progressUpdate();
            writer = new FileOutputStream(toFile);
            // save remote last modified data to local
        }
        try  {
            if(isCompletedDownload){
                setStatus(COMPLETE);
            }else {
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;

                while (getStatus() == DOWNLOADING && (count = in.read(buffer)) != -1) {
                    progressLength += count;
                    writer.write(buffer, 0, count);
                    // progress....
//                    downloadListener.progressUpdate();
                    progressBarListener.progress(new Message(progressLength, toFile, fileLength));
                    if (progressLength == fileLength) {
                        progressLength = 0;
                        setStatus(COMPLETE);
                    }
                }
            }
        } finally {
            writer.close();
            in.close();
            connection.disconnect();
        }
    }

    private void prepareDownload(String urlStr, String toFile, ResumeDownloadListener downloadListener) throws IOException {
        Log.d(TAG, "========= prefetch_list==============");
        Iterator<String> iter = PrefetchConfig.prefetching_queue.iterator();
        while(iter.hasNext()){
            Log.d(TAG, iter.next());
        }
        Log.d(TAG, "=============== prepare ================");
//        downloadListener.progressUpdate();
        HttpURLConnection conn = createConnection(urlStr, downloadListener);
        downloadedFile = new File(toFile);
        fileLength = conn.getContentLength();

//        startNewDownload = (!downloadedFile.exists() || downloadedFile.length() >= fileLength ||        // downloadedFile.length()>=fileLength ?
//                !remoteLastModified.equalsIgnoreCase(lastModified));
        startNewDownload = (!downloadedFile.exists());                                                      // 파일이 없으면 새로받기
        isCompletedDownload = (downloadedFile.exists() && downloadedFile.length() >= fileLength);           // 파일이 있다 -> 다운로드가 완료 or 미완료

        Log.d(TAG, "url : " + urlStr);
        Log.d(TAG, "code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());
        Log.d(TAG, "startNewDownload : "+startNewDownload);
        Log.d(TAG, "isCompletedDownload : " + isCompletedDownload);
        conn.disconnect();
//        downloadListener.progressUpdate();
        Log.d(TAG, "=========================================");

    }

    private HttpURLConnection createConnection(String urlStr, ResumeDownloadListener downloadListener) throws IOException {
//        downloadListener.progressUpdate();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open connection to URL.
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);
//        Log.d(TAG, "createConnection ::: - code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());           // ** 이거를 하고 뒤에 setRquest 하면 오류난다. 기억하자
        return conn;
    }

    public String getStatusStr() {
        return statuses[getStatus()];
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
