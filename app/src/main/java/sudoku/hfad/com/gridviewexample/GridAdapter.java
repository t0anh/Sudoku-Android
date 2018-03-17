package sudoku.hfad.com.gridviewexample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by tuana on 17-03-2018.
 */

public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<Box> boxes;

    public GridAdapter(Context context, ArrayList<Box> items) {
        this.context = context;
        this.boxes = items;
    }

    @Override
    public int getCount() {
        return boxes.size();
    }

    @Override
    public Object getItem(int i) {
        return boxes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Box box = boxes.get(i);
        // fix item row height
        int height = Cell.CELL_HEIGHT * 3 + 2 * Box.BOX_LINE_SPACING;
        box.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height));
        return box;
    }
}
