package android.hfad.com.sudoku;

import android.content.Intent;
import android.graphics.Point;
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
        setContentView(R.layout.activity_main);

        /* setup action bar */
        getSupportActionBar().setTitle("Menu");

        /* get screen size */
        Display display = getWindowManager().getDefaultDisplay();
        AppConstant.screenSize = new Point();
        display.getSize(AppConstant.screenSize);
        int width = AppConstant.screenSize.x / 2;

        /* resize buttons */
        Button btnEasy = findViewById(R.id.btn_easy);
        btnEasy.setWidth(width);

        Button btnNormal = findViewById(R.id.btn_normal);
        btnNormal.setWidth(width);

        Button btnHard = findViewById(R.id.btn_hard);
        btnHard.setWidth(width);

        Button btnNightmare = findViewById(R.id.btn_nightmare);
        btnNightmare.setWidth(width);

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
            case R.id.btn_nightmare: {
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