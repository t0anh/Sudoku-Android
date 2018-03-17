package sudoku.hfad.com.gridviewexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tuana on 17-03-2018.
 */

@SuppressLint("AppCompatCustomView")
public class NumpadButton extends TextView {
    static final String[] buttonText = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "X"};
    public boolean isMarked = false;
    private final int ID;

    @SuppressLint("ResourceAsColor")
    public NumpadButton(Context context, final int position) {
        super(context);
        setHeight(Cell.CELL_HEIGHT);
        setBackgroundResource(R.color.UNSELECTED);
        setText(buttonText[position]);
        setGravity(Gravity.CENTER);
        setTextSize(20);
        this.ID = position + 1;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.setValueToSelectedCell(ID % 10);
                GameActivity.updateNumpad();
            }
        });
    }
}
