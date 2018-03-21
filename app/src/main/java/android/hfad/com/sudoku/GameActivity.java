package android.hfad.com.sudoku;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class GameActivity extends AppCompatActivity {
    static GridView sudoku, numpad;
    static Box[] boxes;
    static Cell[][] cells = new Cell[9][9];
    static Cell selectedCell = null;
    private int[][] gridValues;
    private int timeElapsed;
    private SudokuSolver solver = new SudokuSolver();
    private final int[] numberOfEmptyCells = {0, 20, 30, 40, 50};
    private final String[] difficultName = {"NONE", "EASY", "NORMAL", "HARD", "NIGHTMARE"};
    static int counter;

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
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_solve:
                onClickSolve();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        /* hide the status bar */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* compute cell height */
        int width = AppConstant.screenSize.x;
        Cell.CELL_HEIGHT = (width - 120) / 9;

        /* timer */
        setupTimer();

        /* difficult text */
        TextView view = findViewById(R.id.difficult_text);
        view.setText(difficultName[AppConstant.difficulty]);

         /* generate a sudoku */
        gridValues = solver.getRandomGrid(numberOfEmptyCells[AppConstant.difficulty]);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                cells[row][col] = new Cell(this, row * 9 + col, gridValues[row][col]);
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
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cell cell = (Cell) view;
                    if (cell.isLocked()) {
                        numpad.setVisibility(View.INVISIBLE);
                    } else {
                        int values = cell.getMask();
                        numpad.setVisibility(View.VISIBLE);
                        for (int x = 1; x <= 9; ++x) {
                            if ((values >> x) % 2 == 1) {
                                numpad.getChildAt(x - 1).setBackgroundResource(R.color.SELECTED);
                            } else {
                                numpad.getChildAt(x - 1).setBackgroundResource(R.color.UNSELECTED);
                            }
                        }
                    }
                    setSelectedCell(cell);
                }
            });
            boxes[i].setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, boxHeight));
        }

        sudoku = findViewById(R.id.main_grid);
        GridAdapter adapter = new GridAdapter(this);
        sudoku.setAdapter(adapter);

        /* setup numpad */
        ArrayList<NumpadButton> buttons = new ArrayList<>();
        for (int pos = 0; pos < 10; ++pos) {
            buttons.add(new NumpadButton(this, pos));
        }
        numpad = findViewById(R.id.numpad);
        NumpadAdapter numpadAdapter = new NumpadAdapter(this, buttons);
        numpad.setAdapter(numpadAdapter);
        numpad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NumpadButton button = (NumpadButton) view;
                selectedCell.addNumber(button.getNumber());
                updateNumpad();
            }
        });
    }

    private void setupTimer() {
        timeElapsed = 0;

        final TextView timer = findViewById(R.id.timer);
        timer.setTextColor(Color.BLACK);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timeElapsed += 1;
                handler.postDelayed(this, 1000);

                int seconds = timeElapsed % 60;
                int minutes = (timeElapsed / 60) % 60;
                int hours = timeElapsed / 3600;

                if (hours == 0) {
                    timer.setText(String.format("%02d:%02d", minutes, seconds));
                } else {
                    timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
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

    static public void setSelectedCell(Cell cell) {
        selectedCell = cell;
        highlightNeighborCells(cell.getPosition());
    }

    public static void updateNumpad() {
        if (selectedCell != null) {
            int values = selectedCell.getMask();
            for (int x = 1; x <= 9; ++x) {
                NumpadButton button = (NumpadButton) numpad.getChildAt(x - 1);
                button.isMarked = ((values >> x) % 2 == 1);
                button.setBackgroundResource(button.isMarked ? R.color.SELECTED : R.color.UNSELECTED);
            }
        }
    }

    public void onClickSubmit(View view) {
        if (!isUniqueValueGrid()) return;
        int[][] grid = new int[9][9];
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                grid[row][col] = cells[row][col].getValue();
            }
        }
        if (solver.checkAcceptedGrid(grid)) {
            Toast.makeText(this, "Accepted", LENGTH_LONG).show();
        }
    }

    public void onClickSolve() {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                /* remove marker */
                gridValues[row][col] &= ~1024;

                cells[row][col].setNumber(gridValues[row][col]);
                cells[row][col].setLocked();
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
