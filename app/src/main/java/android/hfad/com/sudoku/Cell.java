package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
    static public final Map<Integer, Integer> maskToNumber = new HashMap<Integer, Integer>() {
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
    private final int box;

    public Cell(Context context, int position, int number) {
        super(context);
        this.position = position;
        this.mask = 0;
        setHeight(CELL_HEIGHT);
        setGravity(Gravity.CENTER);
        int row = position / 9;
        int col = position - row * 9;
        box = (row / 3) * 3 + col / 3;

        if (number <= 9) {
            isLocked = true;
            addNumber(number);
            setTextColor(Color.parseColor("#353535"));
        } else {
            isLocked = false;
            addNumber(0);
            setTextColor(Color.parseColor("#2d4fe4"));
        }

        setNoHighLight();
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
                setText(String.valueOf(maskToNumber.get(mask)));
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

    public int getNumber() {
        return maskToNumber.get(mask);
    }

    public void setHighLight() {
        setBackgroundResource(isLocked ? R.color.HIGHLIGHT_LOCKED_CELL_COLOR : R.color.HIGHLIGHT_EMPTY_CELL_COLOR);
    }

    public void setNoHighLight() {
        if (box % 2 == 0) {
            setBackgroundResource(R.color.EVEN_BOX_COLOR);
        } else {
            setBackgroundResource(R.color.ODD_BOX_COLOR);
        }
    }

    public void setNumber(int number) {
        mask = (1 << number);
        setText(String.valueOf(number));
    }
}
