package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by tuana on 17-03-2018.
 */

@SuppressLint("AppCompatCustomView")
public class NumpadButton extends TextView {
    final private int MARKED_COLOR = R.color.NUMPAD_BUTTON_MARKED_COLOR;
    final private int UNMARKED_COLOR = R.color.NUMPAD_BUTTON_UNMARKED_COLOR;
    final private String[] buttonText = {"1", "2", "3", "4", "5", "MARK",  "6", "7", "8", "9", "CLEAR", "UNDO"};
    final private int[] index = {1, 2, 3, 4, 5, 10, 6, 7, 8, 9, 0, 11};

    private int position;

    @SuppressLint("ResourceAsColor")
    public NumpadButton(Context context, int position) {
        super(context);
        this.position = position;
        setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
        setText(buttonText[position]);
        setGravity(Gravity.CENTER);
        setTypeface(AppConstant.APP_FONT);
        setHeight(Cell.CELL_HEIGHT);

        if(position == 10) {
            setTextSize(12);
            setTextColor(Color.BLACK);
        }
        else if(position == 5) {
            setTextSize(12);
            setTextColor(Color.BLACK);
        }
        else if(position == 11) {
            setTextSize(12);
            setTextColor(Color.BLACK);
        }
        else {
            setTextSize(20);
            setTextColor(Color.BLUE);
        }
    }

    public int getIndex() {
        return index[position];
    }

    @Override
    public boolean onTouchEvent (MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int number = getIndex();
            GameActivity.onPressNumpad(number);
        }
        return true;
    }

    public void setBackgroundColor(boolean marked) {
        setBackgroundResource(marked ? MARKED_COLOR : UNMARKED_COLOR);
    }
}
