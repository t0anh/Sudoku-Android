package android.hfad.com.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by tuana on 19-03-2018.
 */

public class AppConverter {
    public static Point screenSize = null;
    public static Typeface appFont = null;

    public static void init (Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize = new Point(metrics.widthPixels, metrics.heightPixels);
        Cell.CELL_HEIGHT = (screenSize.x - 120) / 9;
        appFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.app_font));
    }

    public static float convertDpToPixel (float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String getTimeFormat (String time) {
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
