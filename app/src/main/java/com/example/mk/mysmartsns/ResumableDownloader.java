package com.example.mk.mysmartsns;

import android.util.Log;
import android.webkit.DownloadListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mk on 2017-03-26.
 */

public class ResumableDownloader {
    /**
     * last modified time for file
     */
    private String lastModified;

    private int timeout;
    private File downloadedFile;
    private boolean startNewDownload;
    /**
     * total length of the file
     */
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

    public ResumableDownloader(String lastModified, int status) {
        this.lastModified = lastModified;
        timeout = 9000;
        startNewDownload = true;
        this.status = status;
        statuses = new String[]{"Downloading", "Complete", "Pause", "Error"};
    }

    /**
     * @param urlStr           downloadFile url
     * @param toFile           downloadedFile path
     * @param downloadListener downloadFile progress listener
     * @throws IOException
     */
    public void downloadFile(String urlStr, String toFile, DownloadListener downloadListener) throws IOException {
        Log.d("gilsoo", toFile);
//        Log.d("gilsoo", "length : " + new File(toFile).length());

        prepareDownload(urlStr, toFile, downloadListener);
        // 준비 마쳤으면 다시 서버와 연결
        HttpURLConnection connection = createConnection(urlStr, downloadListener);
        setStatus(DOWNLOADING);
        // 이어받는거면
        if (!startNewDownload) {
//            connection.setRequestProperty("Range", "bytes="+String.valueOf(downloadedFile.length()) + "-");
            // 아마 header의 range라는 부분에 downloadFile.length(미리 받아온 파일의 길이)를 설정해주는 부분
            connection.setRequestProperty("Range", String.valueOf(downloadedFile.length()));
        }
        downloadListener.progressUpdate(new Message("ResponseCode: " + connection.getResponseCode() + "; file length:" +
                connection.getContentLength() + "\nResponseMessage: " + connection.getResponseMessage()));
        InputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
        FileOutputStream writer;
        long progressLength = 0;
        if (!startNewDownload) {
            // 이어받기면
            progressLength = downloadedFile.length();
            downloadListener.progressUpdate(new Message("resume download to: " + toFile));
            // append to exist downloadedFile
            writer = new FileOutputStream(toFile, true);
        } else {
            downloadListener.progressUpdate(new Message("new download to: " + toFile));
            writer = new FileOutputStream(toFile);
            // save remote last modified data to local
            lastModified = connection.getHeaderField("Last-Modified");
        }
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while (getStatus() == DOWNLOADING && (count = in.read(buffer)) != -1) {
                progressLength += count;
                writer.write(buffer, 0, count);
                // progress....
                downloadListener.progressUpdate(new Message((int) (progressLength * 100 / fileLength)));
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

    /**
     * rend a request to server & decide to start a new download or not
     *
     * @param urlStr           string url
     * @param toFile           to file path
     * @param downloadListener
     * @throws IOException
     */
    // 다운로드 시작하기 전에 새롭게 파일을 받아 올 것인지 아니면 이어받기를 할 것인지를 판단한다.
    private void prepareDownload(String urlStr, String toFile, DownloadListener downloadListener) throws IOException {
        Log.d("prepare", "===============================");
        downloadListener.progressUpdate(new Message("prepare download ..........."));
        HttpURLConnection conn = createConnection(urlStr, downloadListener);
        downloadedFile = new File(toFile);
        String remoteLastModified = conn.getHeaderField("Last-Modified");
        fileLength = conn.getContentLength();

//        startNewDownload = (!downloadedFile.exists() || downloadedFile.length() >= fileLength ||        // downloadedFile.length()>=fileLength ?
//                !remoteLastModified.equalsIgnoreCase(lastModified));
        // 다운로드 파일이 존재하지 않거나 다운로드 파일의 length가 보내질 length보다 크면
        startNewDownload = (!downloadedFile.exists() || downloadedFile.length() >= fileLength);
        Log.d("prepare", "url : " + urlStr);
        Log.d("prepare", "code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());
        conn.disconnect();
        downloadListener
                .progressUpdate(new Message("prepare finished .... start a new Download = " + startNewDownload));
        Log.d("prepare", "===============================");

    }

    /**
     * @param urlStr           url string
     * @param downloadListener
     * @return An URLConnection for HTTP
     * @throws IOException
     */
    private HttpURLConnection createConnection(String urlStr, DownloadListener downloadListener) throws IOException {
        downloadListener.progressUpdate(new Message("create new connection ...."));
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open connection to URL.
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);
//        Log.d("prepare", "code : " + conn.getResponseCode() + ", message : " + conn.getResponseMessage());
        return conn;
    }

    /**
     * @return status as a String
     */
    public String getStatusStr() {
        return statuses[getStatus()];
    }

    /**
     * @return status corresponding number
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastModified() {
        return lastModified;
    }
}
