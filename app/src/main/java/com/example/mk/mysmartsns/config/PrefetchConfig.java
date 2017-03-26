package com.example.mk.mysmartsns.config;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gilsoo on 2017-03-26.
 */
public class PrefetchConfig {
    public static final String PREFS_NAME = "MyResumeDownloadPref";
    public static final String PREFS_KEY_PROGRESS = "Progress";
    public static final String Local_Name = "/Prefetch_Contents";

    public static boolean isPrefetchingMode = true;                     // 나중에 이 boolean값으로 기능 on/off
    public static Queue<String> prefetching_queue = new LinkedList<>();
}
