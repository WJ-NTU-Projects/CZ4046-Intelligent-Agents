package algorithm

import arena.Arena
import enums.Action
import enums.Grid
import kotlin.math.abs

/**
 * Algorithm for value iteration, extending the abstract Iteration class.
 */
class ValueIteration: Iteration() {
    /**
     * Public accessor variables as the algorithm methods do not return anything to the caller.
     * (NOTE: Kotlin loosely respects OOP!)
     */
    var convergenceConstant: Double = Constants.CONVERGENCE_CONSTANT
    var convergenceThreshold: Double = 0.01

    /**
     * Calls the parent class' start method, and calculates the [convergenceThreshold] based on the input [constant]
     * value.
     *
     * Repeatedly calculates the best utility and optimal action for each state until convergence, as determined by
     * the [convergenceThreshold].
     *
     * Finally, exports the history of utilities calculated into a .CSV format file.
     *
     * @see iterate
     * @see exportHistory
     */
    fun start(arena: Array<Array<Grid>>, columns: Int, rows: Int, constant: Double) {
        super.start(arena, columns, rows)
        convergenceConstant = constant

        val allowableError: Double = constant * Constants.MAX_REWARD
        val convergeThreshold: Double = (allowableError * (1 - Constants.DISCOUNT)) / Constants.DISCOUNT
        this.convergenceThreshold = convergeThreshold
        var largestChange: Double

        do {
            largestChange = iterate()
            iterations++
        } while (largestChange >= convergeThreshold)

        exportHistory("value")
    }

    /**
     * Calculates the best utility and optimal action for each state, for an iteration,
     * based on the current [utilities].
     *
     * Calculates the largest absolute difference between each calculated utility and the current (previous iteration
     * calculated) utility.
     *
     * Updates the [utilities] and [policy] arrays with the calculated best utility and optimal action for each state.
     *
     * @return the largest absolute difference calculated.
     * @see calculateBestUtilityAndAction
     */
    private fun iterate(): Double {
        var largestChange = 0.0

        for ((row, r) in Arena.grids.withIndex()) {
            for ((column, grid) in r.withIndex()) {
                if (grid == Grid.WALL) continue
                val bestUtilityAndAction: Pair<Double, Action> = calculateBestUtilityAndAction(column, row)
                var bestUtility: Double = bestUtilityAndAction.first
                bestUtility *= Constants.DISCOUNT
                bestUtility += grid.reward

                val change: Double = abs(bestUtility - utilities[row][column])
                if (change > largestChange) largestChange = change
                utilities[row][column] = bestUtility
                val bestAction: Action = bestUtilityAndAction.second
                policy[row][column] = bestAction
            }
        }

        saveHistory()
        return largestChange
    }
}