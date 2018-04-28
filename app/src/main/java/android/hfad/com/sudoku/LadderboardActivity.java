package android.hfad.com.sudoku;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LadderboardActivity extends Activity {
    private int difficulty = 4;
    private int one; // a width unit
    private TextView txtEasy, txtNormal, txtHard, txtExtreme, txtSelectedDifficulty;
    private TableLayout table;
    Typeface appFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladderboard);

        // hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // reference variables
        one = AppConstant.SCREEN_SIZE.x / 4;
        table = findViewById(R.id.table_main);
        appFont = AppConstant.APP_FONT;

        txtEasy = findViewById(R.id.txtEasy);
        txtNormal = findViewById(R.id.txtNormal);
        txtHard = findViewById(R.id.txtHard);
        txtExtreme = findViewById(R.id.txtExtreme);

        txtSelectedDifficulty = findViewById(R.id.txtSelectedDifficulty);

        txtSelectedDifficulty.setTypeface(appFont);
        adjustDifficultyTextView();
        txtExtreme.callOnClick();
    }

    void adjustDifficultyTextView() {
        txtEasy.setWidth(one);
        txtEasy.setTypeface(appFont);
        txtEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 1;
                txtSelectedDifficulty.setText("Easy");
                txtSelectedDifficulty.setTextColor(getResources().getColor(R.color.EASY_COLOR));
                drawLadderboard();
            }
        });


        txtNormal.setWidth(one);
        txtNormal.setTypeface(appFont);
        txtNormal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                difficulty = 2;
                txtSelectedDifficulty.setText("Normal");
                txtSelectedDifficulty.setTextColor(getResources().getColor(R.color.NORMAL_COLOR));
                drawLadderboard();
            }
        });


        txtHard.setWidth(one);
        txtHard.setTypeface(appFont);
        txtHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 3;
                txtSelectedDifficulty.setText("Hard");
                txtSelectedDifficulty.setTextColor(getResources().getColor(R.color.HARD_COLOR));
                drawLadderboard();
            }
        });


        txtExtreme.setWidth(one);
        txtExtreme.setTypeface(appFont);
        txtExtreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty = 4;
                txtSelectedDifficulty.setText("Extreme");
                txtSelectedDifficulty.setTextColor(getResources().getColor(R.color.EXTREME_COLOR));
                drawLadderboard();
            }
        });
    }

    void drawLadderboard() {
        // clear old data
        table.removeAllViews();

        // draw new table
        TableRow header = new TableRow(this);

        TextView rankHeader = new LadderboardCell(this, "Rank", one / 2);
        header.addView(rankHeader);

        TextView nicknameHeader = new LadderboardCell(this, "Nickname", one);
        header.addView(nicknameHeader);

        TextView timeElapsedHeader = new LadderboardCell(this, "Time", one / 2);
        header.addView(timeElapsedHeader);

        TextView dateHeader = new LadderboardCell(this, "Date", one);
        header.addView(dateHeader);

        TextView noteHeader = new LadderboardCell(this, "Note", one);
        header.addView(noteHeader);

        table.addView(header);


        // read and show information
        DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT * FROM achievement WHERE difficulty = '" + difficulty + "'ORDER BY elapsedSeconds", null);
            cursor.moveToFirst();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        for (int r = 1; r <= 10; ++r) {
            TableRow newRow = new TableRow(this);
            String rank = "", nickname = "", time = "", date = "", note = "";
            if (cursor != null && !cursor.isAfterLast()) {
                rank = String.valueOf(r);
                nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                time = cursor.getString(cursor.getColumnIndex("elapsedSeconds"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                note = cursor.getString(cursor.getColumnIndex("note"));
                cursor.moveToNext();
            }

            TextView rankCell = new LadderboardCell(this, rank, one / 2);
            newRow.addView(rankCell);

            TextView nicknameCell = new LadderboardCell(this, nickname, one);
            newRow.addView(nicknameCell);

            String text = (time == "") ? time : Timer.getTimeFormat(Integer.parseInt(time));
            TextView timeCell = new LadderboardCell(this, text, one / 2);
            newRow.addView(timeCell);

            TextView dateCell = new LadderboardCell(this, date, one);
            newRow.addView(dateCell);

            TextView noteCell = new LadderboardCell(this, note, one);
            newRow.addView(noteCell);

            table.addView(newRow);
        }
    }
}
