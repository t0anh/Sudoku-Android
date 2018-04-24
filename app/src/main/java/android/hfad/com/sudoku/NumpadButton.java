package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tuana on 17-03-2018.
 */

@SuppressLint("AppCompatCustomView")
public class NumpadButton extends TextView {
    static final String[] buttonText = {"1", "2", "3", "4", "5", "MARK",  "6", "7", "8", "9", "CLEAR", "UNDO"};
    static final int[] number = {1, 2, 3, 4, 5, 10, 6, 7, 8, 9, 0, 11};
    private boolean isOn;
    private int position;

    @SuppressLint("ResourceAsColor")
    public NumpadButton(Context context, int position) {
        super(context);
        this.position = position;
        setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
        setText(buttonText[position]);
        setGravity(Gravity.CENTER);
        setTypeface(AppConverter.appFont);
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

    public boolean isOn() {
        return isOn;
    }
    public void setState(boolean state) {
        isOn = state;
    }
    public int getNumber() {
        return number[position];
    }

    @Override
    public boolean onTouchEvent (MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int number = getNumber();
            GameActivity.onPressNumpad(number);
        }
        return true;
    }
}
