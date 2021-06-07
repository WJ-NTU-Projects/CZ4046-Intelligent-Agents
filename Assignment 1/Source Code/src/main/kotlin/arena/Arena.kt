package arena

import Constants
import enums.Grid
import kotlin.random.Random

/**
 * Manages the arena environment, representing each state as a grid on a 2-dimensional arena map.
 *
 * It is a static class as the methods and variables are frequently used globally.
 * TO-DO: Change to a singleton.
 */
class Arena {
    companion object {
        var columns: Int = Constants.DEFAULT_ROW_COLUMN
        var rows: Int = Constants.DEFAULT_ROW_COLUMN
        var grids: Array<Array<Grid>> = Array(rows) { Array(columns) { Grid.WHITE } }
        private var start: Pair<Int, Int> = Pair(2, 3)

        /**
         * Resets the arena state to the default configuration, then fill in the green and brown spaces, walls, and
         * start point in the individual states.
         *
         * @see reset
         */
        fun setDefault() {
            reset()
            grids[3][2] = Grid.START

            grids[0][0] = Grid.GREEN
            grids[0][2] = Grid.GREEN
            grids[0][5] = Grid.GREEN
            grids[1][3] = Grid.GREEN
            grids[2][4] = Grid.GREEN
            grids[3][5] = Grid.GREEN

            grids[1][1] = Grid.BROWN
            grids[1][5] = Grid.BROWN
            grids[2][2] = Grid.BROWN
            grids[3][3] = Grid.BROWN
            grids[4][4] = Grid.BROWN

            grids[0][1] = Grid.WALL
            grids[1][4] = Grid.WALL
            grids[4][1] = Grid.WALL
            grids[4][2] = Grid.WALL
            grids[4][3] = Grid.WALL
        }

        /**
         * Resets the arena state to the default configuration.
         */
        private fun reset() {
            columns = Constants.DEFAULT_ROW_COLUMN
            rows = Constants.DEFAULT_ROW_COLUMN
            grids = Array(rows) { Array(columns) { Grid.WHITE } }
            start = Pair(2, 3)
        }

        /**
         * Generates a random square-arena of the given [size] (size x size).
         *
         * # Start point randomly assigned to a a white space.
         * # Green spaces = size (row = column).
         * # Brown spaces = size - 1.
         * # Walls = size - 1.
         */
        fun generateRandom(size: Int) {
            columns = size
            rows = size
            grids = Array(size) { Array(size) { Grid.WHITE } }
            fillGrids(size, 1, Grid.START)
            fillGrids(size, size, Grid.GREEN)
            fillGrids(size, size - 1, Grid.BROWN)
            fillGrids(size, size - 1, Grid.WALL)
        }

        /**
         * Fills [count] grids of a [size] x [size] arena with the given [grid] type.
         */
        private fun fillGrids(size: Int, count: Int, grid: Grid) {
            val random = Random(System.currentTimeMillis())

            for (i in 0 until count) {
                var column: Int
                var row: Int

                do {
                    column = random.nextInt(0, size)
                    row = random.nextInt(0, size)
                } while (grids[row][column] != Grid.WHITE)

                grids[row][column] = grid
            }
        }

        /**
         * Checks if the given [column] and [row] are valid indices for the arena (not out of bounds).
         * @return true if valid, false otherwise.
         */
        private fun checkIndices(column: Int, row: Int): Boolean = (column >= 0 && row >= 0 && column < this.columns && row < this.columns)
    }
}