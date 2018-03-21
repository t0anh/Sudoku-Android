package android.hfad.com.sudoku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by tuana on 17-03-2018.
 */

public class BoxAdapter extends BaseAdapter {
    Context context;
    int parentIndex;

    public BoxAdapter (Context context, int boxIndex) {
        this.context = context;
        this.parentIndex = boxIndex;
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
        int row = (parentIndex / 3) * 3 + position / 3;
        int col = (parentIndex % 3) * 3 + position % 3;
        return GameActivity.cells[row][col];
    }
}
