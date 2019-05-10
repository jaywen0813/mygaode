package com.android.mygaode.weight;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.mygaode.MainActivity;
import com.android.mygaode.MainApplication;


/**
 * UI工具类 资源操作
 */
public class UIUtils {


    public static Context getContext() {
        return getContext();
    }

    public static Thread getMainThread() {
        return getMainThread();
    }


    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return MainApplication.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }


    /**
     * 系统UI是否显示(状态栏和导航栏), 并设置状态栏透明
     *
     * @param activity
     * @param isShow   是否显示
     */
    public static void setSystemUIVisible(Activity activity, boolean isShow) {
        int uiFlags;
        if (isShow) {
            uiFlags = View.SYSTEM_UI_FLAG_VISIBLE;
            uiFlags |= 0x00001000;
            //            uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        } else {
            uiFlags = View.INVISIBLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            uiFlags |= 0x00001000;
            //            uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            //                    | View.SYSTEM_UI_FLAG_LAYOUT_FUL//
            //                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            //                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);

    }

    /**
     * activity 全屏切换
     *
     * @param activity
     * @param isFullScreen 是否要全屏
     */
    public static void fullScreen(Activity activity, boolean isFullScreen) {
        if (isFullScreen) {
            //取消标题栏
            try {
                activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            } catch (Exception e) {

            }
            //取消状态栏
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            try {
                activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            } catch (Exception e) {

            }
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
}
