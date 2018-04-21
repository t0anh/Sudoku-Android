package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LadderboardActivity extends AppCompatActivity {
    private int difficulty = 0;
    private int screenWidth;
    private TextView txtEasy, txtNormal, txtHard, txtExtreme, txtSelectedDifficulty;
    private Typeface appFont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladderboard);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appFont = Typeface.createFromAsset(getAssets(), getString(R.string.app_font));

        Display screen = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        screenWidth = size.x;

        txtEasy = findViewById(R.id.txtEasy);
        txtNormal = findViewById(R.id.txtNormal);
        txtHard = findViewById(R.id.txtHard);
        txtExtreme = findViewById(R.id.txtExtreme);

        txtSelectedDifficulty = findViewById(R.id.txtSelectedDifficulty);
        txtSelectedDifficulty.setTypeface(appFont);

        adjustDifficultyTextView();
        txtExtreme.callOnClick();
    }

    @SuppressLint("ResourceAsColor")
    void adjustDifficultyTextView() {
        int width = screenWidth / 4;

        txtEasy.setWidth(width);
        txtEasy.setTypeface(appFont);
        txtEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 1;
                txtSelectedDifficulty.setText("Easy");
                txtSelectedDifficulty.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.EASY_COLOR));
                clearLadderboard();
                drawLadderboard(screenWidth / 12);
            }
        });


        txtNormal.setWidth(width);
        txtNormal.setTypeface(appFont);
        txtNormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                difficulty = 2;
                txtSelectedDifficulty.setText("Normal");
                txtSelectedDifficulty.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.NORMAL_COLOR));
                clearLadderboard();
                drawLadderboard(screenWidth / 12);
            }
        });


        txtHard.setWidth(width);
        txtHard.setTypeface(appFont);
        txtHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 3;
                txtSelectedDifficulty.setText("Hard");
                txtSelectedDifficulty.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.HARD_COLOR));
                clearLadderboard();
                drawLadderboard(screenWidth / 12);
            }
        });


        txtExtreme.setWidth(width);
        txtExtreme.setTypeface(appFont);
        txtExtreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 4;
                txtSelectedDifficulty.setText("Extreme");
                txtSelectedDifficulty.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.EXTREME_COLOR));
                clearLadderboard();
                drawLadderboard(screenWidth / 12);
            }
        });
    }

    void drawLadderboard(int w) {
        TableLayout table = findViewById(R.id.table_main);
        TableRow header = new TableRow(this);
        header.setClickable(false);

        TextView rankHeader = new LadderboardCell(this, "Rank", w + w / 2, appFont);
        header.addView(rankHeader);

        TextView nicknameHeader = new LadderboardCell(this, "Nickname", 3 * w, appFont);
        header.addView(nicknameHeader);

        TextView timeElapsedHeader = new LadderboardCell(this, "Time", w + w / 2, appFont);
        header.addView(timeElapsedHeader);

        TextView dateHeader = new LadderboardCell(this, "Date", 3 * w, appFont);
        header.addView(dateHeader);

        TextView noteHeader = new LadderboardCell(this, "Note", 3 * w, appFont);
        header.addView(noteHeader);

        table.addView(header);


        // read and show information
        DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM achievement WHERE difficulty = '" + difficulty + "'ORDER BY timeElapsed", null);
            cursor.moveToFirst();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        for (int r = 1; r <= 10; ++r) {
            TableRow newRow = new TableRow(this);
            String rank = "", nickname = "", time = "", date = "", note = "";
            if (cursor != null && !cursor.isAfterLast()) {
                rank = String.valueOf(r);
                nickname = cursor.getString(cursor.getColumnIndex("_nickname"));
                time = cursor.getString(cursor.getColumnIndex("timeElapsed"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                note = cursor.getString(cursor.getColumnIndex("note"));
                cursor.moveToNext();
            }

            TextView rankCell = new LadderboardCell(this, rank, w + w / 2, appFont);
            newRow.addView(rankCell);

            TextView nicknameCell = new LadderboardCell(this, nickname, 3 * w, appFont);
            newRow.addView(nicknameCell);

            TextView timeCell = new LadderboardCell(this, AppConverter.getTimeFormat(time), w + w / 2, appFont);
            newRow.addView(timeCell);

            TextView dateCell = new LadderboardCell(this, date, 3 * w, appFont);
            newRow.addView(dateCell);

            TextView noteCell = new LadderboardCell(this, note, 3 * w, appFont);
            newRow.addView(noteCell);

            table.addView(newRow);
        }
    }

    void clearLadderboard() {
        TableLayout table = findViewById(R.id.table_main);
        table.removeAllViews();
    }
}
