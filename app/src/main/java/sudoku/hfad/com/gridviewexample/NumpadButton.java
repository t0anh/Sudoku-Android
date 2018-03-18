package sudoku.hfad.com.gridviewexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by tuana on 17-03-2018.
 */

@SuppressLint("AppCompatCustomView")
public class NumpadButton extends TextView {
    static final String[] buttonText = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "CLEAR"};
    public boolean isMarked = false;
    private int value;

    @SuppressLint("ResourceAsColor")
    public NumpadButton(Context context, int position) {
        super(context);
        setHeight(Cell.CELL_HEIGHT);
        setBackgroundResource(R.color.UNSELECTED);
        setText(buttonText[position]);
        setGravity(Gravity.CENTER);
        setTextSize((position == 9) ? 12 : 20);
        value = position + 1;
    }

    public int getIndex() {
        return value % 10;
    }
}
