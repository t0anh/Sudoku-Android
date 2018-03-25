package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.widget.TextView;

import static android.graphics.Color.*;

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
        setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
        setText(buttonText[position]);
        setTextColor(DKGRAY);
        setGravity(Gravity.CENTER);
        setTextSize(20);
        value = position + 1;
        if(position == 9) {
            setTextSize(12);
            SpannableString text = new SpannableString("CLEAR");
            text.setSpan(new UnderlineSpan(), 0, 5, 0);
            setText(text);
            setTextColor(Color.BLACK);
        }
        else {
            setTextColor(Color.parseColor("#2d4fe4"));
        }
    }

    public int getNumber() {
        return value % 10;
    }
}
