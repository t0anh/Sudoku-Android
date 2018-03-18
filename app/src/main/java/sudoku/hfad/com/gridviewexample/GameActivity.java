package sudoku.hfad.com.gridviewexample;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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
    static private GridView grid, numpad;
    static private Cell selectedCell = null;
    static private Cell[][] cells = new Cell[9][9];
    private TextView timer;
    private int timeElapsed;
    private Handler handler;
    private Runnable runnable;

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
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide the status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // get the screen width
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        // compute cell height
        int width = screenSize.x;
        Cell.CELL_HEIGHT = (width - 120) / 9;

        // generate a sudoku grid
        SudokuSolver generator = new SudokuSolver();
        int[][] values = generator.getRandomGrid(40);

        // setup values for boxes
        ArrayList<ArrayList<Cell>> cellGroups = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            cellGroups.add(new ArrayList<Cell>());
        }
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                cellGroups.get(box).add(new Cell(this, row * 9 + col, values[row][col]));
            }
        }

        // extract cells from 'grid' to cells[][]
        for(ArrayList<Cell> group : cellGroups) {
            for(Cell cell : group) {
                int index = cell.getIndex();
                int row = index / 9;
                int col = index - row * 9;
                cells[row][col] = cell;
            }
        }

        // setup sudoku gridview
        ArrayList<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            Box box = new Box(this);
            BoxAdapter adapter = new BoxAdapter(this, cellGroups.get(i));
            box.setAdapter(adapter);
            box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cell cell = (Cell) view;

                    if (cell.isLocked()) {
                        numpad.setVisibility(View.INVISIBLE);
                    } else {
                        int values = cell.getValues();
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
            boxes.add(box);
        }


        grid = findViewById(R.id.main_grid);
        GridAdapter adapter = new GridAdapter(this, boxes);
        grid.setAdapter(adapter);

        // setup numpad
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
                setValueToSelectedCell(button.getIndex());
                updateNumpad();
            }
        });

        setupTimer();
    }

    private void setupTimer() {
        timeElapsed = 0;
        timer = findViewById(R.id.timer);
        timer.setTextColor(Color.BLACK);
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
                    timer.setText(String.format("%02d:%02d", minutes, seconds));
                } else {
                    timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }
            }
        };
        runnable.run();
    }

    public void highlightNeighborCells() {
        int pos = selectedCell.getIndex();
        int row = pos / 9;
        int col = pos - row * 9;
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

    public void setSelectedCell(Cell cell) {
        selectedCell = cell;
        highlightNeighborCells();
    }

    static public void setValueToSelectedCell(int value) {
        if (selectedCell != null) {
            selectedCell.setValue(value);
        }
    }

    public static void updateNumpad() {
        if (selectedCell != null) {
            int values = selectedCell.getValues();
            for (int x = 1; x <= 9; ++x) {
                NumpadButton button = (NumpadButton) numpad.getChildAt(x - 1);
                button.isMarked = ((values >> x) % 2 == 1);
                button.setBackgroundResource(button.isMarked ? R.color.SELECTED : R.color.UNSELECTED);
            }
        }
    }

    public void OnClickSubmit(View view) {
        int[][] grid = new int[9][9];
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                grid[row][col] = cells[row][col].getValues();
            }
        }
        SudokuSolver solver = new SudokuSolver();

        if (solver.checkUniqueValueGrid(grid)) {
            Toast.makeText(this, "Unique Value Grid", LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Multiple Value Grid", LENGTH_LONG).show();
        }

        if (solver.checkAcceptedGrid(grid)) {
            Toast.makeText(this, "Accepted", LENGTH_LONG).show();
        }
    }
}
