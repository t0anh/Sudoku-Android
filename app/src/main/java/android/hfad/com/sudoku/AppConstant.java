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
    public static Point SCREEN_SIZE = null;
    public static Typeface APP_FONT = null;
    public static int BOX_LINE_SPACING = 4;
    public static int BOX_HEIGHT = 0;

    public static void init (Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        SCREEN_SIZE = new Point(metrics.widthPixels, metrics.heightPixels);
        Cell.CELL_HEIGHT = (SCREEN_SIZE.x - 120) / 9;
        BOX_HEIGHT = Cell.CELL_HEIGHT * 3 + 2 * BOX_LINE_SPACING;
        APP_FONT = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.app_font));
    }

    public static float convertDpToPixel (float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
