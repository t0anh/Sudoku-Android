# Sudoku-Android
Game sudoku trên android

# Demo 
## API
  - `SudokuSolver`: Quản lý một game sudoku về mặt dữ liệu. Một số phương thức chính:
      - `int[][] getRandomGrid (int numberOfEmptyCells)`: Sinh một lưới sudoku ngẫu nhiênvới `numberOfEmptyCells` ô rỗng
      - `boolean checkAcceptedGrid(int[][] grid)`: Kiểm tra `grid` có đúng hay không
## Giao diện
  - Vẽ bảng sudoku và numpad điều khiển
