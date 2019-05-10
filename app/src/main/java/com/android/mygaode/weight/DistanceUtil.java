package com.android.mygaode.weight;

/**
 * ================================================
 * 作    者：booob
 * 版    本：1.0
 * 创建日期：2018-07-25-0025 08:58
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DistanceUtil {

    public static String formateDistance(float distance) {
        String s;
//        if (distance > 1000) {
//            s = (float) (Math.round(distance / 10)) / 100 + "km";
//        } else {
//            s = (float) (Math.round(distance * 100)) / 100 + "m";
//        }

        if (distance > 1000) {
            s = (float) (Math.round(distance / 100)) / 10 + "km";
        } else {
            s = (int) (Math.round(distance * 10)) / 10 + "m";
        }
        return s;
    }
}
