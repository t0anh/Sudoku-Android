package android.hfad.com.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
    Button btnPlay, btnLadderboard, btnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppConverter.init(this);
        Typeface appFont = Typeface.createFromAsset(getAssets(), getString(R.string.app_font));
        /* hide the status bar */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setTypeface(appFont);

        btnLadderboard = findViewById(R.id.btnLadderBoard);
        btnLadderboard.setTypeface(appFont);

        btnResume = findViewById(R.id.btnResume);
        btnResume.setTypeface(appFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM GameState ORDER BY createdDate DESC LIMIT 1", null);
        float dp = AppConverter.convertDpToPixel(1, this);
        int btnWidth = (int) (170 * dp);
        int btnMargin = (int) (5 * dp);
        if(cursor != null && cursor.getCount() > 0) {
            btnResume.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams btnResumeParams = new RelativeLayout.LayoutParams(btnWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnResumeParams.addRule(RelativeLayout.BELOW, R.id.btnPlay);
            btnResumeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            btnResumeParams.setMargins(0, btnMargin, 0, btnMargin);
            btnResume.setLayoutParams(btnResumeParams);

            RelativeLayout.LayoutParams btnLadderboardParams = new RelativeLayout.LayoutParams(btnWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnLadderboardParams.addRule(RelativeLayout.BELOW, R.id.btnResume);
            btnLadderboardParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            btnLadderboardParams.setMargins(0, btnMargin, 0, btnMargin);;
            btnLadderboard.setLayoutParams(btnLadderboardParams);
        }
        else {
            btnResume.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(btnWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.btnPlay);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0, btnMargin, 0, btnMargin);
            btnLadderboard.setLayoutParams(params);
        }
    }

    public void onClickLadderboard(View view) {
        Intent intent = new Intent(this, LadderboardActivity.class);
        startActivity(intent);
    }

    public void onClickPlay(View view) {
        Intent intent = new Intent(this, DifficultyMenuActivity.class);
        startActivity(intent);
    }

    public void onClickResume(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        String id, status, difficulty, timeElapsed, solutionString, gridString;
        Cursor cursor;
        try {
            DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
            SQLiteDatabase database = DBHelper.getWritableDatabase();
            cursor = database.rawQuery("SELECT * FROM GameState ORDER BY createdDate DESC LIMIT 1", null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();

                status = cursor.getString(cursor.getColumnIndex("status"));
                difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
                timeElapsed = cursor.getString(cursor.getColumnIndex("timeElapsed"));
                solutionString = cursor.getString(cursor.getColumnIndex("solutionString"));
                gridString = cursor.getString(cursor.getColumnIndex("gridString"));
                id = cursor.getString(cursor.getColumnIndex("id"));

                // remove old data
                database.execSQL("DELETE FROM GameState WHERE 1");

                intent.putExtra("status", Integer.parseInt(status));
                intent.putExtra("difficulty", Integer.parseInt(difficulty));
                intent.putExtra("timeElapsed", Integer.parseInt(timeElapsed));
                intent.putExtra("solutionString", solutionString);
                intent.putExtra("gridString", gridString);

                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
