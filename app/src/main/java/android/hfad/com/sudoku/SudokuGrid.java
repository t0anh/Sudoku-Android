package android.hfad.com.sudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class SudokuGrid {
    private Context mContext;
    private GridView mGridView;
    private Cell[][] mCells = new Cell[9][9];
    private Box[] mBoxes = new Box[9];
    private Cell mSelectedCell;
    private int[][] mSolution = new int[9][9];

    public SudokuGrid(Context context, int[][] masks, int[][] solution) {
        mContext = context;
        mGridView = ((Activity) context).findViewById(R.id.grid_sudoku);

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int box = (row / 3) * 3 + col / 3;
                // copy solution
                mSolution[row][col] = solution[row][col];

                // initialize cell
                int highlightColor = (solution[row][col] <= 9) ? R.color.HIGHLIGHT_LOCKED_CELL_COLOR : R.color.HIGHLIGHT_EMPTY_CELL_COLOR;
                int defaultColor = (box % 2 == 0) ? R.color.EVEN_BOX_COLOR : R.color.ODD_BOX_COLOR;

                mCells[row][col] = new Cell(context.getApplicationContext(), row * 9 + col, highlightColor, defaultColor);
                mCells[row][col].setMask(masks[row][col]);
            }
        }

        // initialize grid
        for (int i = 0; i < 9; ++i) {
            mBoxes[i] = new Box();
            BoxAdapter boxAdapter = new BoxAdapter(i);
            mBoxes[i].setAdapter(boxAdapter);
        }

        SudokuGridAdapter gridAdapter = new SudokuGridAdapter(mBoxes);
        mGridView.setAdapter(gridAdapter);
    }

    public int[][] getCurrentMasks() {
        int[][] values = new int[9][9];
        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                values[row][col] = mCells[row][col].getMask();
            }
        }
        return values;
    }

    public int[][] getNumbers () {
        int[][] values = new int[9][9];
        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                values[row][col] = mCells[row][col].getNumber();
            }
        }
        return values;
    }

    public void fill() {
        boolean[][] rows = new boolean[9][10];
        boolean[][] cols = new boolean[9][10];
        boolean[][] boxes = new boolean[9][10];

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (mCells[row][col].isLocked()) {
                    int box = (row / 3) * 3 + col / 3;
                    int number = mCells[row][col].getNumber();
                    rows[row][number] = true;
                    cols[col][number] = true;
                    boxes[box][number] = true;
                }
            }
        }

        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (!mCells[row][col].isLocked()) {
                    int mask = 1022; // full numbers = 2^10 - 2.
                    int box = (row / 3) * 3 + col / 3;
                    for (int x = 1; x <= 9; ++x) {
                        if (rows[row][x] || cols[col][x] || boxes[box][x]) mask &= ~(1 << x);
                    }
                    mCells[row][col].setMask(mask);
                }
            }
        }
    }

    public Cell getSelectedCell() {
        return mSelectedCell;
    }

    public void setSelectedCell(int index) {
        int row = index / 9;
        int col = index - row * 9;
        mSelectedCell = mCells[row][col];
    }

    public boolean isLegalGrid() {
        for (Cell[] row : mCells) {
            for (Cell cell : row) {
                if (Integer.bitCount(cell.getMask()) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        for (Cell[] row : mCells) {
            for (Cell cell : row) {
                if (!cell.isLocked()) {
                    cell.addNumber(0);
                }
            }
        }
    }

    public Cell getCell(int row, int col) {
        return mCells[row][col];
    }

    public void highlightNeighborCells(int index) {
        int row = index / 9;
        int col = index - row * 9;
        int box = (row / 3) * 3 + col / 3;

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                int k = (i / 3) * 3 + j / 3;
                if (i == row || j == col || k == box) {
                    mCells[i][j].setHighLight();
                } else {
                    mCells[i][j].setNoHighLight();
                }
            }
        }
    }

    public void showSolution() {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                // get actually number
                int number = mSolution[row][col] & ~1024;

                Cell cell = mCells[row][col];
                cell.setNumber(number);

                // if cell is unlocked, color cell number to RED
                if (!cell.isLocked()) {
                    cell.setTextColor(Color.RED);
                    cell.setTextSize(Cell.CELL_DEFAULT_TEXT_SIZE);
                }

            }
        }
    }

    /* Grid adapter */
    public class SudokuGridAdapter extends BaseAdapter {
        Box[] mBoxes;

        public SudokuGridAdapter(Box[] boxes) {
            mBoxes = boxes;
        }
        @Override
        public int getCount() {
            return mBoxes.length;
        }

        @Override
        public Object getItem(int i) {
            return mBoxes[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return mBoxes[i];
        }
    }

    /* Box*/
    public class Box extends GridView {
        public Box() {
            super(mContext);
            setVerticalSpacing(AppConstant.BOX_LINE_SPACING);
            setHorizontalSpacing(AppConstant.BOX_LINE_SPACING);
            setNumColumns(3);
            setGravity(Gravity.CENTER);
            setBackgroundResource(R.color.GRID_BACKGROUND_COLOR);
            setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, AppConstant.BOX_HEIGHT));
        }
    }

    /* Box adapter */
    public class BoxAdapter extends BaseAdapter {
        private int mIndex;

        public BoxAdapter (int index) {
            this.mIndex = index;
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            int row = (mIndex / 3) * 3 + position / 3;
            int col = (mIndex % 3) * 3 + position % 3;
            return mCells[row][col];
        }
    }
}
