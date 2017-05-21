package com.example.mk.mysmartsns.prefetch;

/**
 * Created by gilsoo on 2017-05-20.
 */
public class Message {
    private long length;
    private long totalLength;
    private String fileName;

    public Message(long length, String fileName, long totalLength){
        this.length = length;
        this.totalLength = totalLength;
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }
}
