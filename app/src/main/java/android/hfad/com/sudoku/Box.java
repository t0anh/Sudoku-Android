package android.hfad.com.sudoku;

import android.content.Context;
import android.view.Gravity;
import android.widget.GridView;


/**
 * Created by tuana on 14-03-2018.
 */

public class Box extends GridView {
    static int BOX_LINE_SPACING = 4;
    static int BOX_HEIGHT = Cell.CELL_HEIGHT * 3 + 2 * Box.BOX_LINE_SPACING;

    public Box(Context context) {
        super(context);
        setVerticalSpacing(BOX_LINE_SPACING);
        setHorizontalSpacing(BOX_LINE_SPACING);
        setNumColumns(3);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.color.GRID_BACKGROUND_COLOR);
        setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, BOX_HEIGHT));
    }
}
