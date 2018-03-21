package android.hfad.com.sudoku;

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
    static public int CELL_HEIGHT;
    static private final int[] indexOfNumber = {0, 0, 3, 6, 8, 11, 14, 16, 19, 22}; // in format cell string
    static public final Map<Integer, Integer> maskToValue = new HashMap<Integer, Integer>() {
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
    private int mask;
    private final int position;

    public Cell(Context context, int position, int number) {
        super(context);
        this.position = position;
        this.mask = 0;
        if(number <= 9) {
            isLocked = true;
            addNumber(number);
        }
        else {
            isLocked = false;
            addNumber(0);
        }

        setBackgroundResource(isLocked ? R.color.LOCKED_CELL_COLOR : R.color.EMPTY_CELL_COLOR);
        setHeight(CELL_HEIGHT);
        setGravity(Gravity.CENTER);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void addNumber(int number) {
        mask ^= (1 << number);
        if (mask != 0 && mask % 2 == 0) {
            int counter = Integer.bitCount(mask);
            if (counter > 1) {
                char[] format = "1  2  3\n4  5  6\n7  8  9".toCharArray();
                for (int x = 1; x <= 9; ++x) {
                    if ((mask >> x) % 2 == 0) {
                        format[indexOfNumber[x]] = ' ';
                    }
                }
                setTextSize(10);
                setText(String.valueOf(format));
            } else {
                setTextSize(CELL_DEFAULT_TEXT_SIZE);
                setText(String.valueOf(maskToValue.get(mask)));
            }
        } else {
            mask = 0;
            setText("");
        }
    }
    public int getPosition() {
        return position;
    }
    public int getMask() {
        return mask;
    }
    public int getValue () {return maskToValue.get(mask); }
    public void setLocked () {
        setTextSize(CELL_DEFAULT_TEXT_SIZE);
        setClickable(false);
        setBackgroundResource(R.color.LOCKED_CELL_COLOR);
    }
    public void setHighLight() {
        setBackgroundResource(isLocked ? R.color.HIGHLIGHT_LOCKED_CELL_COLOR : R.color.HIGHLIGHT_EMPTY_CELL_COLOR);
    }
    public void setNoHighLight() {
        setBackgroundResource(isLocked ? R.color.LOCKED_CELL_COLOR : R.color.EMPTY_CELL_COLOR);
    }

    public void setNumber(int number) {
        mask = (1 << number);
        setText(String.valueOf(number));
    }
}
