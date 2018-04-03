package android.hfad.com.sudoku;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.TextView;

public class LadderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladderboard);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x / 4;


        TextView txtEasy = findViewById(R.id.txtEasy);
        txtEasy.setWidth(width);
        txtEasy.setBackgroundColor(Color.GRAY);

        TextView txtNormal = findViewById(R.id.txtNormal);
        txtNormal.setWidth(width);
        txtNormal.setBackgroundColor(Color.YELLOW);

        TextView txtHard = findViewById(R.id.txtHard);
        txtHard.setWidth(width);
        txtHard.setBackgroundColor(Color.GREEN);

        TextView txtExtreme = findViewById(R.id.txtExtreme);
        txtExtreme.setWidth(width);
        txtExtreme.setBackgroundColor(Color.RED);
    }
}
