package android.hfad.com.sudoku;

import android.content.Context;
import android.widget.GridView;

public class SudokuGrid {
    Context mContext;
    GridView mGridView;
    Box[] mBoxes = new Box[9];

    public SudokuGrid(Context context, GridView view) {
        mContext = context;
        mGridView = view;

        for (int i = 0; i < 9; ++i) {
            mBoxes[i] = new Box(context);
            BoxAdapter adapter = new BoxAdapter(context, i);
            mBoxes[i].setAdapter(adapter);
        }

        SudokuGridAdapter adapter = new SudokuGridAdapter(mContext, mBoxes);
        mGridView.setAdapter(adapter);
    }

}
