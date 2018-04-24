package android.hfad.com.sudoku;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class Numpad {
    static final int[] numpadPosition = {10, 0, 1, 2, 3, 4, 6, 7, 8, 9};

    Context mContext;
    GridView mGridView;

    public Numpad (Context context, GridView grid) {
        mContext = context;
        mGridView = grid;
        init();
    }

    private void init () {
        ArrayList<NumpadButton> buttons = new ArrayList<>();
        for (int pos = 0; pos < 12; ++pos) {
            buttons.add(new NumpadButton(mContext, pos));
        }

        NumpadAdapter numpadAdapter = new NumpadAdapter(mContext, buttons);
        mGridView.setAdapter(numpadAdapter);
    }

    public void setEnabled(boolean enabled) {
        mGridView.setEnabled(enabled);
    }

    public void update (int mask, boolean isMarked) {
        for (int x = 1; x <= 9; ++x) {
            NumpadButton button = (NumpadButton) mGridView.getChildAt(numpadPosition[x]);
            button.setState((mask >> x) % 2 == 1);
            button.setBackgroundResource(button.isOn() ? R.color.NUMPAD_BUTTON_MARKED_COLOR : R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
        }
        if (isMarked) {
            mGridView.getChildAt(5).setBackgroundResource(R.color.MARKED_CELL_COLOR);
        } else {
            mGridView.getChildAt(5).setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
        }
    }

    public void setVisibility(int visibility) {
        mGridView.setVisibility(visibility);
    }
}
