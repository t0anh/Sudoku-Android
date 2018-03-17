package sudoku.hfad.com.gridviewexample;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    static private GridView grid, numpad;
    static private Cell selectedCell = null;
    Cell[][] cells = new Cell[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the screen width
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        // compute cell height
        Cell.CELL_HEIGHT = (width - 120) / 9;

        // generate a sudoku grid
        SudokuSolver generator = new SudokuSolver();
        int[][] values = generator.getRandomGrid(1);

        // setup values for boxes
        ArrayList<ArrayList<Cell>> cellGroup = new ArrayList<>();
        for(int i = 0; i < 9; ++i) {
            cellGroup.add(new ArrayList<Cell>());
        }
        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                cellGroup.get(box).add(new Cell(this, values[row][col]));
            }
        }
        // setup sudoku gridview
        ArrayList<Box> boxes = new ArrayList<>();
        for(int i = 0; i < 9; ++i) {
            Box box = new Box(this);
            BoxAdapter adapter = new BoxAdapter(this, cellGroup.get(i));
            box.setAdapter(adapter);
            box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cell cell = (Cell) view;
                    if(cell.isLocked()) {
                        numpad.setVisibility(View.INVISIBLE);
                    }
                    else {
                        int values = cell.getValues();
                        numpad.setVisibility(View.VISIBLE);
                        numpad.setSelected(true);
                        for(int pos = 1; pos <= 9; ++pos) {
                            if((values >> pos) % 2 == 1) {
                                numpad.getChildAt(pos-1).setBackgroundResource(R.color.SELECTED);
                            }
                            else {
                                numpad.getChildAt(pos-1).setBackgroundResource(R.color.UNSELECTED);
                            }
                        }
                        cell.setGameActivitySelectedCell();
                    }
                }
            });
            boxes.add(box);
        }

        grid = findViewById(R.id.main_grid);
        GridAdapter adapter = new GridAdapter(this, boxes);
        grid.setAdapter(adapter);

        // setup numpad
        ArrayList<NumpadButton> buttons = new ArrayList<>();
        for(int pos = 0; pos < 10; ++pos) {
            buttons.add(new NumpadButton(this, pos));
        }
        numpad = findViewById(R.id.numpad);
        NumpadAdapter numpadAdapter = new NumpadAdapter(this, buttons);
        numpad.setAdapter(numpadAdapter);
        // extract cells from 'grid'
        String message = "";
        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                int boxID = (row / 3) * 3 + col / 3;
                GridView box = (GridView) grid.getItemAtPosition(boxID);
                cells[row][col] = (Cell) box.getItemAtPosition((row % 3) * 3 + (col % 3));
                message += String.valueOf(Cell.toIndex.get(cells[row][col].getValues()) + " ");
            }
            message += "\n";
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }



    static public void setSelectedCell (Cell cell) {
        selectedCell = cell;
    }

    static public void setValueToSelectedCell (int value) {
        if(selectedCell != null) {
            selectedCell.setValue(value);
        }
    }

    public static void updateNumpad() {
        if(selectedCell != null) {
            int values = selectedCell.getValues();
            for(int x = 1; x <= 9; ++x) {
                NumpadButton button = (NumpadButton) numpad.getChildAt(x-1);
                button.isMarked = ((values >> x) % 2 == 1);
                button.setBackgroundResource(button.isMarked ? R.color.SELECTED : R.color.UNSELECTED);
            }
        }
    }

    public void OnClickSubmit(View view) {
        int[][] grid = new int[9][9];
        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                grid[row][col] = cells[row][col].getValues();
            }
        }
        SudokuSolver solver = new SudokuSolver();
        if(solver.checkUniqueValueGrid(grid)) {
            Toast.makeText(this, "Unique Value Grid", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Multiple Value Grid", Toast.LENGTH_LONG).show();
        }

        if(solver.checkAcceptedGrid(grid)) {
            Toast.makeText(this, "Accepted", Toast.LENGTH_LONG).show();
        }
    }
}
