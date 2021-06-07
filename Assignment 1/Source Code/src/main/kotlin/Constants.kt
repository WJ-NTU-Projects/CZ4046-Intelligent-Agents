/**
 * Constant values that are accessible by multiple classes.
 *
 * Values include:
 * - DISCOUNT: Discount constant used to calculate utility.
 * - CONVERGENCE_CONSTANT: Constant c used to determine the convergence threshold.
 * - EVALUATION_ITERATION: No. of iterations N to evaluate a policy for policy iteration.
 * - MAX_REWARD: Maximum amount of reward Rmax used to determine the convergence threshold.
 * - MOVE_INTENDED: Probability that the agent moves in the intended direction.
 * - MOVE_LEFT: Probability that the agent moves left of the intended direction.
 * - MOVE_RIGHT: Probability that the agent moves right of the intended direction.
 * - DEFAULT_ROW_COLUMN: Default size of the arena (6 as given in the question).
 */
class Constants {
    companion object {
        const val DISCOUNT: Double = 0.99
        const val CONVERGENCE_CONSTANT: Double = 0.1
        const val EVALUATION_ITERATION: Int = 75
        const val MAX_REWARD: Double = 1.0
        const val MOVE_INTENDED: Double = 0.8
        const val MOVE_LEFT: Double = 0.1
        const val MOVE_RIGHT: Double = 0.1
        const val DEFAULT_ROW_COLUMN = 6
    }
}