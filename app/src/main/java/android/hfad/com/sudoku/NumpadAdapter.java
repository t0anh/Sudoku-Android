package android.hfad.com.sudoku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by tuana on 17-03-2018.
 */

public class NumpadAdapter extends BaseAdapter {
    Context context;
    ArrayList<NumpadButton> buttons;

    public NumpadAdapter (Context context, ArrayList<NumpadButton> buttons) {
        this.context = context;
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int i) {
        return buttons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return buttons.get(i);
    }
}
