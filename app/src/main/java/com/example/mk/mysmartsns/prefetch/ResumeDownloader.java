package com.example.mk.mysmartsns.prefetch;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gilsoo on 2017-03-26.
 */
public class ResumeDownloader {
    private static final String TAG = ResumeDownloader.class.getSimpleName();
    private int timeout;
    private File downloadedFile;
    private boolean startNewDownload;

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

    public ResumeDownloader(int status) {
        timeout = 9000;                     // 9초동안 응답없으면 종료
        startNewDownload = true;
        this.status = status;
        statuses = new String[]{"Downloading", "Complete", "Pause", "Error"};
    }

    public void downloadFile(String urlStr, String toFile, ResumeDownloadListener downloadListener) throws IOException {
        Log.d(TAG, toFile);
//        Log.d(TAG, "length : " + new File(toFile).length());

        prepareDownload(urlStr, toFile, downloadListener);
        HttpURLConnection connection = createConnection(urlStr, downloadListener);
        setStatus(DOWNLOADING);
        if (!startNewDownload) {
//            connection.setRequestProperty("Range", "bytes="+String.valueOf(downloadedFile.length()) + "-");
            connection.setRequestProperty("Range", String.valueOf(downloadedFile.length()));
        }
        downloadListener.progressUpdate();
        InputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        FileOutputStream writer;
        long progressLength = 0;
        if (!startNewDownload) {
            progressLength = downloadedFile.length();
            downloadListener.progressUpdate();
            // append to exist downloadedFile
            writer = new FileOutputStream(toFile, true);
        } else {
            downloadListener.progressUpdate();
            writer = new FileOutputStream(toFile);
            // save remote last modified data to local
        }
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while (getStatus() == DOWNLOADING && (count = in.read(buffer)) != -1) {
                progressLength += count;
                writer.write(buffer, 0, count);
                // progress....
                downloadListener.progressUpdate();
                if (progressLength == fileLength) {
                    progressLength = 0;
                    setStatus(COMPLETE);
                }
            }
        } finally {
            writer.close();
            in.close();
            connection.disconnect();
        }
    }

    private void prepareDownload(String urlStr, String toFile, ResumeDownloadListener downloadListener) throws IOException {
        Log.d(TAG, "=============== prepare ================");
        downloadListener.progressUpdate();
        HttpURLConnection conn = createConnection(urlStr, downloadListener);
        downloadedFile = new File(toFile);
        fileLength = conn.getContentLength();

//        startNewDownload = (!downloadedFile.exists() || downloadedFile.length() >= fileLength ||        // downloadedFile.length()>=fileLength ?
//                !remoteLastModified.equalsIgnoreCase(lastModified));
        startNewDownload = (!downloadedFile.exists() || downloadedFile.length() >= fileLength);
        Log.d(TAG, "url : " + urlStr);
        Log.d(TAG, "code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());
        conn.disconnect();
        downloadListener.progressUpdate();
        Log.d(TAG, "=========================================");

    }

    private HttpURLConnection createConnection(String urlStr, ResumeDownloadListener downloadListener) throws IOException {
        downloadListener.progressUpdate();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open connection to URL.
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);
//        Log.d("prepare", "code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());
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
