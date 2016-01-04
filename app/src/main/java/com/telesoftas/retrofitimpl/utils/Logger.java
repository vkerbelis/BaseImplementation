package com.telesoftas.retrofitimpl.utils;

import android.util.Log;

import com.telesoftas.retrofitimpl.BuildConfig;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-10-14.
 */
@SuppressWarnings("unused")
public final class Logger {

    public static final String KEY_RESPONSE_OBJECT = "response_object";
    private static final String LOG_PREFIX = "Test_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    private Logger() {
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }
        return LOG_PREFIX + str;
    }

    public static void d(String tag, Throwable cause) {
        if (cause != null && cause.getMessage() != null && !cause.getMessage().isEmpty()) {
            d(tag, cause.getMessage());
        } else {
            Log.wtf(tag, cause);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, cause);
        }
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable cause) {
        Log.e(tag, msg, cause);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void w(String tag, Throwable cause) {
        Log.w(tag, cause);
    }
}
