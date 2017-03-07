package com.example.mk.mysmartsns.config;

/**
 * Created by gilsoo on 2017-02-13.
 */
public class APIConfig {

    public static final String ip = "114.70.21.116";   //192.168.1.9
    public static final int port = 3001;
    public static final String baseUrl = String.format("http://%s:%d/", ip, port);      // 서버에 접근하는 기본 url
}
