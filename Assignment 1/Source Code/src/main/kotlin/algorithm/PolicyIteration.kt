package algorithm

import Constants
import enums.Action
import enums.Grid
import kotlin.random.Random

/**
 * Algorithm for policy iteration, extending the abstract Iteration class.
 */
class PolicyIteration: Iteration() {
    /**
     * Public accessor variable as the algorithm methods do not return anything to the caller.
     * (NOTE: Kotlin loosely respects OOP!)
     */
    var evaluationIteration: Int = Constants.EVALUATION_ITERATION

    /**
     * Calls the parent class' start method, and sets the actions in the [policy] (action) array to random actions for
     * non-wall states.
     *
     * Runs the evaluation and improvement algorithms in a loop until there is no change in policy.
     *
     * Finally, exports the history of utilities calculated into a .CSV format file.
     *
     * @see evaluatePolicy
     * @see improvePolicy
     * @see exportHistory
     */
    fun start(arena: Array<Array<Grid>>, columns: Int, rows: Int, iterations: Int) {
        super.start(arena, columns, rows)
        evaluationIteration = iterations
        val random = Random(System.currentTimeMillis())

        for ((row, r) in policy.withIndex()) {
            for (column in r.indices) {
                if (arena[row][column] == Grid.WALL) continue
                policy[row][column] = getAction(random.nextInt(0, 4))
            }
        }

        var unchanged: Boolean

        do {
            evaluatePolicy()
            unchanged = improvePolicy()
            this.iterations++
        } while (!unchanged)

        exportHistory("policy")
    }

    /**
     * Evaluates the current policy stored in the [policy] array over N iterations as defined in [evaluationIteration],
     * using the simplified Bellman update formula.
     *
     * Updates the [utilities] array with the calculated utilities for each state every iteration.
     *
     * @see calculateUtility
     */
    private fun evaluatePolicy() {
        for (i in 0 until evaluationIteration) {
            for ((row, r) in policy.withIndex()) {
                for ((column, action) in r.withIndex()) {
                    if (arena[row][column] == Grid.WALL) continue
                    var utility = Constants.MOVE_INTENDED * calculateUtility(action, column, row)
                    var newAction: Action = getLeftAction(action)
                    utility += Constants.MOVE_LEFT * calculateUtility(newAction, column, row)
                    newAction = getRightAction(action)
                    utility += Constants.MOVE_RIGHT * calculateUtility(newAction, column, row)
                    utility *= Constants.DISCOUNT
                    utility += arena[row][column].reward
                    utilities[row][column] = utility
                }
            }

            saveHistory()
        }
    }

    /**
     * Improves the current policy stored in the [policy] array using utilities stored in the [utilities] array.
     * Calculates the best utility and optimal action for each state using the Bellman update formula, but does not
     * update the [utilities] array (done in the evaluation stage).
     *
     * Updates only the [policy] array with the calculated optimal action for each state.
     *
     * @see calculateBestUtilityAndAction
     */
    private fun improvePolicy(): Boolean {
        var unchanged = true

        for ((row, r) in arena.withIndex()) {
            for ((column, grid) in r.withIndex()) {
                if (grid == Grid.WALL) continue
                val bestUtilityAndAction: Pair<Double, Action> = calculateBestUtilityAndAction(column, row)
                val bestAction: Action = bestUtilityAndAction.second
                if (bestAction == policy[row][column]) continue
                policy[row][column] = bestAction
                unchanged = false
            }
        }

        return unchanged
    }

    /**
     * Converts an integer [number] within the range of 0 (inclusive) to 4 (exclusive) into an [Action] enum variable.
     * @return an [Action] enum variable associated with the input [number].
     */
    private fun getAction(number: Int): Action {
        return when (number) {
            3 -> Action.RIGHT
            2 -> Action.LEFT
            1 -> Action.DOWN
            else -> Action.UP
        }
    }
}