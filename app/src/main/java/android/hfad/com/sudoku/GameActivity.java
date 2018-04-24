package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;
import java.util.Stack;

import static android.widget.Toast.LENGTH_LONG;

public class GameActivity extends AppCompatActivity {
    static final int[] numberOfEmptyCells = {0, 30, 35, 45, 50};
    static final String[] difficultName = {"NONE", "Easy", "Normal", "Hard", "Extreme"};

    static SudokuGrid grid;
    static Numpad numpad;
    static Cell[][] cells = new Cell[9][9];
    static Cell selectedCell;
    static Stack<CellState> stack = new Stack<>();

    SudokuSolver solver = new SudokuSolver();
    boolean wannaBack = false;

    /* game state */
    private int[][] solution = new int[9][9];
    private int[][] cellMasks = new int[9][9];
    private int timeElapsed;
    private int difficulty;
    private static int status; // -3 game done | -2: auto solved | -1: auto fill | 0: playing | 1: player solved


    /* timer */
    private TextView timeText;
    private Handler handler;
    private Runnable runnable;

    private void generateData() {
        // generate a grid
        solution = solver.getRandomGrid(numberOfEmptyCells[difficulty]);

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                cellMasks[row][col] = (solution[row][col] > 9) ? 0 : solution[row][col];
            }
        }
        // set cell value
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                int number = cellMasks[row][col];
                int highlightColor = (number == 0) ? R.color.HIGHLIGHT_EMPTY_CELL_COLOR : R.color.HIGHLIGHT_LOCKED_CELL_COLOR;
                int defaultColor = (box % 2 == 0) ? R.color.EVEN_BOX_COLOR : R.color.ODD_BOX_COLOR;

                cells[row][col] = new Cell(this, row * 9 + col, highlightColor, defaultColor);
                cells[row][col].setNumber(number);
            }
        }
    }

    private void restoreData(String solutionString, String gridString) {
        // restore solution
        Scanner scanner = new Scanner(solutionString);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                solution[row][col] = scanner.nextInt();
            }
        }
        // restore cellMasks
        scanner = new Scanner(gridString);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                cellMasks[row][col] = scanner.nextInt();
            }
        }

        // set cell value
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                int mask = cellMasks[row][col];
                int highlightColor = (solution[row][col] > 9) ? R.color.HIGHLIGHT_EMPTY_CELL_COLOR : R.color.HIGHLIGHT_LOCKED_CELL_COLOR;
                int defaultColor = (box % 2 == 0) ? R.color.EVEN_BOX_COLOR : R.color.ODD_BOX_COLOR;

                cells[row][col] = new Cell(this, row * 9 + col, highlightColor, defaultColor);
                cells[row][col].setMask(mask);
            }
        }
    }



    private void initComponents() {
        grid = new SudokuGrid(this, (GridView) findViewById(R.id.grid_sudoku));
        numpad = new Numpad(this, (GridView) findViewById(R.id.grid_numpad));
        /* submit button */
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setTypeface(AppConverter.appFont);
        /* timeText */
        timeText = findViewById(R.id.txt_elapsed_time);
        timeText.setTypeface(timeText.getTypeface(), Typeface.BOLD);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        difficulty = bundle.getInt("difficulty", 0);
        status = bundle.getInt("status", 0);
        timeElapsed = bundle.getInt("timeElapsed", 0);
        String solutionString = bundle.getString("solutionString", "none");
        String gridString = bundle.getString("gridString", "none");

        if (solutionString.equals("none") || gridString.equals("none")) {
            generateData();
        } else {
            restoreData(solutionString, gridString);
        }
    }

    private void saveGame() {
        if (status < -1) return;
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                cellMasks[row][col] = cells[row][col].getMask();
            }
        }
        GameState state = new GameState(status, difficulty, timeElapsed, solution, cellMasks);
        try {
            DatabaseHelper DBHelper = DatabaseHelper.newInstance(this);
            SQLiteDatabase database = DBHelper.getWritableDatabase();
            database.insert("GameState", null, state.getContentValues());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        saveGame();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (wannaBack) {
            super.onBackPressed();
            return;
        }

        wannaBack = true;
        Toast.makeText(this, "Bấm 'BACK' lần nữa để quay lại menu chính", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wannaBack = false;
            }
        }, 3000);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_solve:
                onClickSolve();
                break;
            case R.id.action_reset:
                onClickReset();
                break;
            case R.id.action_autofill:
                onClickAutoFill();
                break;
            case R.id.action_tutorial:
                onClickTutorial();
                break;
            default:
                break;
        }

        return true;
    }

    private void onClickAutoFill() {
        if (status < -1) return;
        if (status == -1) {
            autoFill();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setMessage("Nếu sử dụng tính năng này, kết quả của bạn sẽ không được công nhận trên bảng xếp hạng. \nBạn có chắc chắn muốn sử dụng?")
                    .setTitle("Chú ý\n")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            status = -1;
                            autoFill();
                        }
                    })
                    .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    }

    private void onClickTutorial() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyTutorialTheme);
        Spannable message = SpannableWithImage.getTextWithImages(this, getString(R.string.tutorial), 50);

        int position = getString(R.string.tutorial).indexOf("Chú ý:");
        message.setSpan(new RelativeSizeSpan(1.2f), position, position + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setMessage(message).setTitle("Hướng dẫn").show();
    }

    private void autoFill() {
        boolean[][] rowSet = new boolean[9][10];
        boolean[][] colSet = new boolean[9][10];
        boolean[][] boxSet = new boolean[9][10];

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (cells[row][col].isLocked()) {
                    int box = (row / 3) * 3 + col / 3;
                    int number = cells[row][col].getNumber();
                    rowSet[row][number] = true;
                    colSet[col][number] = true;
                    boxSet[box][number] = true;
                }
            }
        }

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (!cells[row][col].isLocked()) {
                    int mask = 1022; // full numbers: 2^10 - 2.
                    int box = (row / 3) * 3 + col / 3;
                    for (int x = 1; x <= 9; ++x) {
                        if (rowSet[row][x] || colSet[col][x] || boxSet[box][x]) mask &= ~(1 << x);
                    }
                    cells[row][col].setMask(mask);
                }
            }
        }
        updateNumpad();
    }

    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadData();
        initComponents();

        // setup action bar title
        getSupportActionBar().setTitle(difficultName[difficulty]);

        // hide the status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // setup timer
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timeElapsed += 1;
                handler.postDelayed(this, 1000);

                int seconds = timeElapsed % 60;
                int minutes = (timeElapsed / 60) % 60;
                int hours = timeElapsed / 3600;

                if (hours == 0) {
                    timeText.setText(String.format("%02d:%02d", minutes, seconds));
                } else {
                    timeText.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }
            }
        };
        runnable.run();
    }

    public static void highlightNeighborCells(int index) {
        int row = index / 9;
        int col = index - row * 9;
        int box = (row / 3) * 3 + col / 3;

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                int k = (i / 3) * 3 + j / 3;
                if (i == row || j == col || k == box) {
                    cells[i][j].setHighLight();
                } else {
                    cells[i][j].setNoHighLight();
                }
            }
        }
    }

    public static void updateNumpad() {
        if (selectedCell != null) {
            numpad.update(selectedCell.getMask(), selectedCell.isMarked());
        }
    }

    public void onClickSubmit(View view) {
        if (!isLegalGrid()) {
            Toast.makeText(this, "Hãy hoàn thiện bảng", LENGTH_LONG).show();
            return;
        }
        int[][] grid = new int[9][9];
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                grid[row][col] = cells[row][col].getNumber();
            }
        }
        if (solver.checkValidGrid(grid)) {
            Toast.makeText(this, "Chúc mừng, đáp án chính xác", LENGTH_LONG).show();
            if (status >= 0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
                dialog.setMessage("Bạn có muốn lưu kết quả trên bảng xếp hạng?")
                        .setTitle("Thông báo\n")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveAchievement();
                            }
                        })
                        .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                handler.removeCallbacks(runnable);
                status = 1;
            }
            status = -3;
        } else {
            Toast.makeText(this, "Đáp án sai", LENGTH_LONG).show();
        }
    }

    private void saveAchievement() {
        Intent intent = new Intent(this, SaveAchievementActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("timeElapsed", timeText.getText());
        intent.putExtra("secondsElapsed", timeElapsed);
        startActivity(intent);
    }

    public void onClickSolve() {
        if(status >= 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setMessage("Nếu sử dụng tính năng này, kết quả của bạn sẽ không được công nhận trên bảng xếp hạng. \nBạn có chắc chắn muốn sử dụng?")
                    .setTitle("Chú ý\n")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            status = -2;
                            onClickSolve();
                        }
                    })
                    .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }

        if(status < 0) {
            status = -2;
            for (int row = 0; row < 9; ++row) {
                for (int col = 0; col < 9; ++col) {
                    /* remove marker */
                    solution[row][col] &= ~1024;

                    cells[row][col].setNumber(solution[row][col]);
                    if (!cells[row][col].isLocked()) {
                        cells[row][col].setTextColor(Color.RED);
                        cells[row][col].setTextSize(Cell.CELL_DEFAULT_TEXT_SIZE);
                    }
                    updateNumpad();
                }
            }
            handler.removeCallbacks(runnable);
        }
    }

    private void onClickReset() {
        if (status < -1) return;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isLocked()) {
                    cell.addNumber(0);
                }
            }
        }
        updateNumpad();
    }

    public boolean isLegalGrid() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (Integer.bitCount(cell.getMask()) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void onPressNumpad(int number) {
        if(status < -1) return;
        if (number < 10) {
            // add a number
            stack.push(selectedCell.getState());
            selectedCell.addNumber(number);
        } else if (number == 10) {
            // mark selected cell
            selectedCell.setMarked(!selectedCell.isMarked());
        } else if (number == 11) {
            // backup previous state
            if (!stack.isEmpty()) {
                CellState preState = stack.peek();
                stack.pop();
                int index = preState.cellIndex;
                int row = index / 9;
                int col = index - row * 9;
                cells[row][col].setMask(preState.mask);
            }
        }
        updateNumpad();
    }
}
