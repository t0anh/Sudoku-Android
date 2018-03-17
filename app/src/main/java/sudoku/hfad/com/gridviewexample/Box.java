package sudoku.hfad.com.gridviewexample;

import android.content.Context;
import android.view.Gravity;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * Created by tuana on 14-03-2018.
 */

public class Box extends GridView {
    static final int BOX_LINE_SPACING = 3;

    public Box(Context context) {
        super(context);
        setVerticalSpacing(BOX_LINE_SPACING);
        setHorizontalSpacing(BOX_LINE_SPACING);
        setNumColumns(3);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.color.BOX_BACKGROUND_COLOR);
    }
}
