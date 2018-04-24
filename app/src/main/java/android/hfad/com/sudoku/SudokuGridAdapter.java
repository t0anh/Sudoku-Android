package android.hfad.com.sudoku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by tuana on 17-03-2018.
 */

public class SudokuGridAdapter extends BaseAdapter {
    Context mContext;
    Box[] mBoxes;

    public SudokuGridAdapter(Context context, Box[] boxes) {
        mContext = context;
        mBoxes = boxes;
    }

    @Override
    public int getCount() {
        return mBoxes.length;
    }

    @Override
    public Object getItem(int i) {
        return mBoxes[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return mBoxes[i];
    }
}
