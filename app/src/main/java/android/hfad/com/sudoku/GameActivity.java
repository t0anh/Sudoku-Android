package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;

public class GameActivity extends AppCompatActivity {
    private final int[] numberOfEmptyCells = {0, 30, 35, 40, 45};
    private final String[] difficultName = {"NONE", "Easy", "Normal", "Hard", "Extreme"};
    static final int[] numpadPosition = {10, 0, 1, 2, 3, 4, 6, 7, 8, 9};

    static GridView sudoku, numpad;
    static Box[] boxes;
    static Cell[][] cells = new Cell[9][9];
    static Cell selectedCell;
    static Typeface defaultFont;

    private int[][] gridValues;
    private int timeElapsed;
    private SudokuSolver solver = new SudokuSolver();
    private TextView timeText;
    private Handler handler;
    private Runnable runnable;
    private int gameState;
    Stack<CellState> cookie = new Stack<>();

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Bấm 'BACK' lần nữa để quay lại menu chính", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
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
        if (gameState == -1) return;
        if (gameState == -2) {
            autoFill();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setMessage("Nếu sử dụng tính năng này, kết quả của bạn sẽ không được công nhận trên bảng xếp hạng. \nBạn có chắc chắn muốn sử dụng?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gameState = -2;
                            autoFill();
                        }
                    })
                    .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setIcon(R.drawable.ic_tutorial)
                    .show();
        }
    }

    private void onClickTutorial() {
        /*
        * source: https://stackoverflow.com/questions/25521685/how-to-insert-drawables-in-text
        * Note: A custom SpanableString with image
        * */
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
                    int mask = 1022; // full numbers
                    int box = (row / 3) * 3 + col / 3;
                    for (int x = 1; x <= 9; ++x) {
                        if (rowSet[row][x] || colSet[col][x] || boxSet[box][x]) mask &= ~(1 << x);
                    }
                    cells[row][col].setMask(mask);
                }
            }
        }
    }

    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* setup action bar title */
        getSupportActionBar().setTitle(difficultName[AppConstant.difficulty]);

        /* hide the status bar */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* compute cell height */
        int width = AppConstant.screenSize.x;
        Cell.CELL_HEIGHT = (width - 120) / 9;


        /* Submit button */
        Button submitButton = findViewById(R.id.submit_button);
        defaultFont = Typeface.createFromAsset(getAssets(), AppConstant.defaultFontName);
        submitButton.setTypeface(defaultFont);

         /* generate a sudoku */
        gridValues = solver.getRandomGrid(numberOfEmptyCells[AppConstant.difficulty]);

        /* fill values to cells */
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                if (box % 2 == 0) {
                    cells[row][col] = new Cell(this, row * 9 + col, gridValues[row][col], R.color.EVEN_BOX_COLOR);
                } else {
                    cells[row][col] = new Cell(this, row * 9 + col, gridValues[row][col], R.color.ODD_BOX_COLOR);
                }
            }
        }

        /* setup sudoku gridview */
        boxes = new Box[9];
        int boxHeight = Cell.CELL_HEIGHT * 3 + 2 * Box.BOX_LINE_SPACING;
        for (int i = 0; i < 9; ++i) {
            boxes[i] = new Box(this);
            BoxAdapter adapter = new BoxAdapter(this, i);
            boxes[i].setAdapter(adapter);
            boxes[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cell cell = (Cell) view;

                    if (cell.isLocked()) {
                        numpad.setVisibility(View.INVISIBLE);
                    } else {
                        int mask = cell.getMask();
                        for (int x = 1; x <= 9; ++x) {
                            if ((mask >> x) % 2 == 1) {
                                numpad.getChildAt(numpadPosition[x]).setBackgroundResource(R.color.NUMPAD_BUTTON_MARKED_COLOR);
                            } else {
                                numpad.getChildAt(numpadPosition[x]).setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
                            }
                        }
                        numpad.setVisibility(View.VISIBLE);
                    }

                    setSelectedCell(cell);
                    updateNumpad();
                }
            });
            boxes[i].setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, boxHeight));
        }
        sudoku = findViewById(R.id.main_grid);
        GridAdapter adapter = new GridAdapter(this);
        sudoku.setAdapter(adapter);

        /* setup numpad */
        ArrayList<NumpadButton> buttons = new ArrayList<>();
        for (int pos = 0; pos < 12; ++pos) {
            buttons.add(new NumpadButton(this, pos));
        }
        numpad = findViewById(R.id.numpad);
        NumpadAdapter numpadAdapter = new NumpadAdapter(this, buttons);
        numpad.setAdapter(numpadAdapter);
        numpad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NumpadButton button = (NumpadButton) view;
                int number = button.getNumber();
                if (number < 10) {
                    // add a number
                    cookie.push(selectedCell.getState());
                    selectedCell.addNumber(number);
                } else if (number == 10) {
                    // mark selected cell
                    selectedCell.setMarked(!selectedCell.isMarked());
                } else if (number == 11) {
                    // backup previous state
                    if (!cookie.isEmpty()) {
                        CellState preState = cookie.peek();
                        cookie.pop();
                        int index = preState.cellIndex;
                        int row = index / 9;
                        int col = index - row * 9;
                        cells[row][col].setState(preState);
                    }
                }
                updateNumpad();
            }
        });

        /* timeText */
        timeElapsed = 0;
        timeText = findViewById(R.id.time);
        timeText.setTypeface(timeText.getTypeface(), Typeface.BOLD);

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
                if (cells[i][j].isMarked()) {
                    cells[i][j].setBackgroundResource(R.color.MARKED_CELL_COLOR);
                    continue;
                }
                int k = (i / 3) * 3 + j / 3;
                if (i == row || j == col || k == box) {
                    cells[i][j].setHighLight();
                } else {
                    cells[i][j].setNoHighLight();
                }
            }
        }
    }

    static public void setSelectedCell(Cell cell) {
        selectedCell = cell;
        highlightNeighborCells(cell.getIndex());
        if (!cell.isLocked()) {
            cell.setBackgroundResource(cell.isMarked() ? R.color.MARKED_CELL_COLOR : R.color.TARGET_CELL_COLOR);
        }
    }

    public static void updateNumpad() {
        if (selectedCell != null) {
            int mask = selectedCell.getMask();
            for (int x = 1; x <= 9; ++x) {
                NumpadButton button = (NumpadButton) numpad.getChildAt(numpadPosition[x]);
                button.setOn((mask >> x) % 2 == 1);
                button.setBackgroundResource(button.isOn() ? R.color.NUMPAD_BUTTON_MARKED_COLOR : R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
            }

            if (selectedCell.isMarked()) {
                numpad.getChildAt(5).setBackgroundResource(R.color.MARKED_CELL_COLOR);
            } else {
                numpad.getChildAt(5).setBackgroundResource(R.color.NUMPAD_BUTTON_UNMARKED_COLOR);
            }
        }
    }

    public void onClickSubmit(View view) {
        if (!isUniqueValueGrid()) {
            return;
        }
        int[][] grid = new int[9][9];
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                grid[row][col] = cells[row][col].getNumber();
            }
        }
        if (solver.checkAcceptedGrid(grid)) {
            Toast.makeText(this, "Accepted", LENGTH_LONG).show();
            handler.removeCallbacks(runnable);
            gameState = 1;
        } else {
            Toast.makeText(this, "Wrong answer", LENGTH_LONG).show();
        }
    }

    public void onClickSolve() {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                /* remove marker */
                gridValues[row][col] &= ~1024;

                cells[row][col].setNumber(gridValues[row][col]);
                if (!cells[row][col].isLocked()) {
                    cells[row][col].setTextColor(Color.RED);
                    cells[row][col].setTextSize(Cell.CELL_DEFAULT_TEXT_SIZE);
                }
                numpad.setEnabled(false);
            }
        }
        handler.removeCallbacks(runnable);
        gameState = -1;
    }

    private void onClickReset() {
        if (gameState == -1) return;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isLocked()) {
                    cell.addNumber(0);
                }
            }
        }
    }

    public boolean isUniqueValueGrid() {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (!cells[row][col].isLocked() && Integer.bitCount(cells[row][col].getMask()) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

}
