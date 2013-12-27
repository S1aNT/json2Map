package me.s1ant.tools;

/**
 * Created by slant on 26.12.13.
 */


public class Constants {
    public static final String LOG_TAG = "json2Map_debug";
    public static final String PACKAGE_NAME = "me.s1ant.json2Map";
    public static final double LAT = 22.277561;
    public static final double LON = 114.176818;
    public static final int DEFAULT_ZOOM_LEVEL = 10;
    //таймаут, в течении которого мы ожидаем местоположения
    public static final int TIME_OUT = 100 * 1000;
    //30 секунд, для првоерки местоположения
    public static final long HALF_MINUTE = 30 * 1000;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    // колличество секунд для обновления
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    //колличество секунд, для обновления в активном режиме
    public static final int FAST_CEILING_IN_SECONDS = 1;
    // интервал обновления в милисекундах
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // Интервал  обновления, кода приложения видно
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int RQS_GooglePlayServices = 1;

    public static final String SHARED_PREFERENCES = "me.s1ant.json2Map.SHARED_PREFERENCES";
    public static final String KEY_UPDATES_REQUESTED = "me.s1ant.json2Map.KEY_UPDATES_REQUESTED";
}