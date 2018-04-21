package android.hfad.com.sudoku;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveAchievementActivity extends AppCompatActivity {
    private TextView txtDifficulty, txtTimeElapsed;
    private EditText edtNickname, edtNote;
    private Button btnOK, btnCancel;
    private int secondsElapsed;
    private int difficulty;
    String difficultName, timeElapsed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveachivement);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Lưu kết quả");

        Intent intent = getIntent();

        timeElapsed = intent.getStringExtra("timeElapsed");
        secondsElapsed = intent.getIntExtra("secondsElapsed", -1);
        difficulty = intent.getIntExtra("difficulty", 0);
        difficultName = GameActivity.difficultName[difficulty];

        Typeface appFont = Typeface.createFromAsset(getAssets(), getString(R.string.app_font));

        txtDifficulty = findViewById(R.id.txtDifficulty);
        txtDifficulty.setText(String.format("%-15s %s", "Độ khó:", difficultName));
        txtDifficulty.setTypeface(appFont);

        txtTimeElapsed = findViewById(R.id.txtTimeElapsed);
        txtTimeElapsed.setText(String.format("%-15s %s", "Thời gian:", timeElapsed));
        txtTimeElapsed.setTypeface(appFont);

        edtNickname = findViewById(R.id.edtNickname);
        edtNickname.setTypeface(appFont);

        edtNote = findViewById(R.id.edtNote);
        edtNote.setTypeface(appFont);

        btnOK = findViewById(R.id.btnOK);
        btnOK.setText("Đồng ý");
        btnOK.setTypeface(appFont);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setText("Hủy bỏ");
        btnCancel.setTypeface(appFont);
    }

    public void onClickCancel(View view) {
        onBackPressed();
    }

    public void onClickOK(View view) {
        String nickname = String.valueOf(edtNickname.getText());
        if(nickname.length() > 0) {
            try {
                DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
                SQLiteDatabase database = DBHelper.getWritableDatabase();

                // insert into achievement table
                SimpleDateFormat formatFactory = new SimpleDateFormat("dd/MM/yyyy");
                ContentValues values = new ContentValues();
                values.put("_nickname", nickname);
                values.put("difficulty", difficulty);
                values.put("date", formatFactory.format(Calendar.getInstance().getTime()));
                values.put("note", String.valueOf(edtNote.getText()));
                values.put("timeElapsed", secondsElapsed);

                database.insert("achievement", null, values);

                Toast.makeText(this, "Đã xong!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                onBackPressed();
            }
        }
        else {
            Toast.makeText(this, "Nickname không được bỏ trống", Toast.LENGTH_LONG).show();
        }
    }
}
