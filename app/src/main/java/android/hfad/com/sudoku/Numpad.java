package android.hfad.com.sudoku;

import android.content.Context;
import android.widget.GridView;

/**
 * Created by tuana on 17-03-2018.
 */

public class Numpad extends GridView {
    private Cell target = null;

    public Numpad(Context context) {
        super(context);
    }

    public void setTarget (Cell cell) {
        target = cell;
    }
    public void setValueForTarget(int value) {
        if(target != null) {
            target.addNumber(value);
        }
    }
}
