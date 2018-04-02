package android.hfad.com.sudoku;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /* get screen size */
        Display display = getWindowManager().getDefaultDisplay();
        AppConstant.screenSize = new Point();
        display.getSize(AppConstant.screenSize);
        int width = AppConstant.screenSize.x / 2;

        Typeface defaultFont = Typeface.createFromAsset(getAssets(), AppConstant.defaultFontName);

        /* resize buttons */
        Button btnEasy = findViewById(R.id.btn_easy);
        btnEasy.setTypeface(defaultFont);
        btnEasy.setWidth(width);

        Button btnNormal = findViewById(R.id.btn_normal);
        btnNormal.setTypeface(defaultFont);
        btnNormal.setWidth(width);

        Button btnHard = findViewById(R.id.btn_hard);
        btnHard.setTypeface(defaultFont);
        btnHard.setWidth(width);

        Button btnExtreme = findViewById(R.id.btn_extreme);
        btnExtreme.setTypeface(defaultFont);
        btnExtreme.setWidth(width);

        /* set fullscreen */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.btn_easy: {
                AppConstant.difficulty = 1;
                break;
            }
            case R.id.btn_normal: {
                AppConstant.difficulty = 2;
                break;
            }
            case R.id.btn_hard: {
                AppConstant.difficulty = 3;
                break;
            }
            case R.id.btn_extreme: {
                AppConstant.difficulty = 4;
                break;
            }
            default: {
                AppConstant.difficulty = 0;
            }
        }
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
