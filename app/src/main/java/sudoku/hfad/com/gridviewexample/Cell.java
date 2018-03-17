package sudoku.hfad.com.gridviewexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuana on 14-03-2018.
 */
@SuppressLint("ResourceAsColor")
public class Cell extends android.support.v7.widget.AppCompatTextView {
    static public int CELL_DEFAULT_TEXT_SIZE = 18;
    static public int CELL_HEIGHT = 100;
    static private final int[] indexOfValue = {0, 0, 3, 6, 8, 11, 14, 16, 19, 22};
    static public final Map<Integer, Integer> toIndex = new HashMap<Integer, Integer>() {
        {
            put(0, 0);
            put(1, 0);
            put(2, 1);
            put(4, 2);
            put(8, 3);
            put(16, 4);
            put(32, 5);
            put(64, 6);
            put(128, 7);
            put(256, 8);
            put(512, 9);
        }
    };

    private boolean isLocked;
    private int mValues;


    public Cell(Context context, int value) {
        super(context);
        setHeight(CELL_HEIGHT);
        setGravity(Gravity.CENTER);
        setValue(value);
        setBackgroundResource((value == 0) ? R.color.EMPTY_CELL_COLOR : R.color.NON_EMPTY_CELL_COLOR);
        isLocked = (value != 0);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setGameActivitySelectedCell() {
        GameActivity.setSelectedCell(this);
    }

    public void setValue(int value) {
        mValues ^= (1 << value);
        if (mValues != 0 && mValues % 2 == 0) {
            int counter = Integer.bitCount(mValues);
            if (counter > 1) {
                char[] format = "1  2  3\n4  5  6\n7  8  9".toCharArray();
                for (int x = 1; x <= 9; ++x) {
                    if ((mValues >> x) % 2 == 0) {
                        format[indexOfValue[x]] = ' ';
                    }
                }
                setTextSize(10);
                setText(String.valueOf(format));
            } else {
                setTextSize(CELL_DEFAULT_TEXT_SIZE);
                setText(String.valueOf(toIndex.get(mValues)));
            }
        } else {
            mValues = 0;
            setText("");
        }
    }

    public int getValues() {
        return mValues;
    }
}
