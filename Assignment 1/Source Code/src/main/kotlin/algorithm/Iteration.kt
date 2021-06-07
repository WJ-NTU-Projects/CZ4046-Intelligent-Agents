package algorithm

import enums.Action
import enums.Grid
import java.nio.channels.FileChannel
import java.nio.file.*

/**
 * Abstract implementation of the iteration algorithms.
 * Contains common methods and variables used by both algorithms.
 */
abstract class Iteration {
    val history: ArrayList<Array<Array<Double>>> = arrayListOf()
    lateinit var arena: Array<Array<Grid>>
    lateinit var policy: Array<Array<Action>>
    lateinit var utilities: Array<Array<Double>>
    var iterations: Int = 0

    /**
     * Re-initialises the [utilities] and [policy] (action) arrays at the start of each algorithm run as a single
     * object is used through the application runtime.
     *
     * Assigns the updated [arena] to the object, as [arena] is modifiable for the bonus question.
     */
    open fun start(arena: Array<Array<Grid>>, columns: Int, rows: Int) {
        this.arena = arena
        policy = Array(rows) { Array(columns) { Action.NONE } }
        utilities = Array(rows) { Array(columns) { 0.0 } }
        iterations = 0
        history.clear()
    }

    /**
     * Calculates the raw utility of a given [action] of a state, defined by the [column] and [row] of the arena.
     *
     * @return the raw utility obtained from the utilities array, based on the given [action].
     */
    fun calculateUtility(action: Action, column: Int, row: Int): Double {
        val coordinates: Pair<Int, Int> = when (action) {
            Action.UP -> Pair(column, row - 1)
            Action.DOWN -> Pair(column, row + 1)
            Action.LEFT -> Pair(column - 1, row)
            Action.RIGHT -> Pair(column + 1, row)
            else -> Pair(column, row)
        }

        val newColumn: Int = coordinates.first
        val newRow: Int = coordinates.second
        val grid: Grid? = arena.getOrNull(newRow)?.getOrNull(newColumn)
        if (grid == null || grid == Grid.WALL) return utilities[row][column]
        return utilities[newRow][newColumn]
    }

    /**
     * Calculates the best utility and optimal action for every available action for a given state, as defined
     * by the [column] and [row] of the arena.
     *
     * @return a pair of the best utility out of all available actions and the associated optimal action.
     *
     * @see calculateUtility
     */
    fun calculateBestUtilityAndAction(column: Int, row: Int): Pair<Double, Action> {
        var bestUtility = Double.NEGATIVE_INFINITY
        var bestAction: Action = Action.NONE

        for (action in Action.values()) {
            if (action == Action.NONE) continue
            var utility = Constants.MOVE_INTENDED * calculateUtility(action, column, row)
            var newAction: Action = getLeftAction(action)
            utility += Constants.MOVE_LEFT * calculateUtility(newAction, column, row)
            newAction = getRightAction(action)
            utility += Constants.MOVE_RIGHT * calculateUtility(newAction, column, row)

            if (utility > bestUtility) {
                bestUtility = utility
                bestAction = action
            }
        }

        return Pair(bestUtility, bestAction)
    }

    /**
     * Get the left-90-degree action of the intended [action]
     * @return the left-90-degree action of the intended [action].
     */
    fun getLeftAction(action: Action): Action {
        return when (action) {
            Action.UP -> Action.LEFT
            Action.DOWN -> Action.RIGHT
            Action.LEFT -> Action.DOWN
            Action.RIGHT -> Action.UP
            else -> Action.NONE
        }
    }

    /**
     * Get the right-90-degree action of the intended [action]
     * @return the right-90-degree action of the intended [action].
     */
    fun getRightAction(action: Action): Action {
        return when (action) {
            Action.UP -> Action.RIGHT
            Action.DOWN -> Action.LEFT
            Action.LEFT -> Action.UP
            Action.RIGHT -> Action.DOWN
            else -> Action.NONE
        }
    }

    /**
     * Saves a copy of the current [utilities] array to a history list.
     *
     * @see copy
     */
    fun saveHistory() {
        history.add(utilities.copy())
    }

    /**
     * Exports the history list into a .CSV format file. The file name is the timestamp of the current system time,
     * and the [type] parameter is used to distinguish the files between value iteration and policy iteration.
     *
     * The file is saved in the "output" folder under the root directory.
     */
    fun exportHistory(type: String) {
        val fileName: String = System.currentTimeMillis().toString()
        val path: Path = Paths.get("output/${fileName}_$type.csv")
        if (!Files.exists(Paths.get("output"), LinkOption.NOFOLLOW_LINKS)) Files.createDirectory(Paths.get("output"))
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) Files.createFile(path)
        val fileChannel: FileChannel = FileChannel.open(path, StandardOpenOption.APPEND)

        for (array in history) {
            var text = ""

            for ((row, r) in array.withIndex()) {
                for ((column, data) in r.withIndex()) {
                    if (data == 0.0) continue
                    if (row > 0 || column > 0) text += ","
                    text += data
                }
            }

            text += "\n"
            Files.write(path, text.toByteArray(), StandardOpenOption.APPEND)
        }

        fileChannel.close()
    }

    /**
     * Copy functionality for a 2-dimensional DoubleArray.
     */
    private fun Array<Array<Double>>.copy() = Array(size) { get(it).clone() }
}