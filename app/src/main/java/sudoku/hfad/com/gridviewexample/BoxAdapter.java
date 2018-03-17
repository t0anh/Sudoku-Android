package sudoku.hfad.com.gridviewexample;

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
    ArrayList<Cell> cells;

    public BoxAdapter (Context context, ArrayList<Cell> cells) {
        this.context = context;
        this.cells = cells;
    }

    @Override
    public int getCount() {
        return cells.size();
    }

    @Override
    public Object getItem(int i) {
        return cells.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return cells.get(position);
    }
}
