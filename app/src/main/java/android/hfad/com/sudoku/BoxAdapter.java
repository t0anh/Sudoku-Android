package android.hfad.com.sudoku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by tuana on 17-03-2018.
 */

public class BoxAdapter extends BaseAdapter {
    Context context;
    int boxIndex;

    public BoxAdapter (Context context, int boxIndex) {
        this.context = context;
        this.boxIndex = boxIndex;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        int row = (boxIndex / 3) * 3 + position / 3;
        int col = (boxIndex % 3) * 3 + position % 3;
        return GameActivity.cells[row][col];
    }
}
