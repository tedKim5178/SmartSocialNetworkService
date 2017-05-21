package com.example.mk.mysmartsns.config;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gilsoo on 2017-03-26.
 */
public class PrefetchConfig {
    public static final String NAME = "SMARTSNS_PREFETCH";
    public static final String PREFS_NAME = "MyResumeDownloadPref";
    public static final String PREFS_KEY_PROGRESS = "Progress";
    public static final String Local_Name = "/Prefetch_Contents";

    //setting key
    public static final String PREFETCH_SHOW = "IS_SHOW_PROGRESSBAR";
    public static final String PREFETCH_MODE= "IS_APPLY_PREFETCHMODE";
    public static final String DATA_MODE= "IS_APPLY_DATA_MODE";
    public static final String NETWORK_MODE= "IS_APPLY_NETWORK_MODE";

    public static boolean isPrefetchingMode = true;                     // 나중에 이 boolean값으로 기능 on/off
    public static boolean isPrefetchingShow = false;
    public static Queue<String> prefetching_queue = new LinkedList<>();

    public static boolean isPrefetching = true;
}
