package android.hfad.com.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

/**
 * Created by tuana on 19-03-2018.
 */

public class AppConstant {

    public static float convertDpToPixel (float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String toSecondMinuteFormat (String time) {
        int totalSeconds;
        try {
            totalSeconds = Integer.parseInt(time);
        } catch (Exception e) {
            return "";
        }
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
