package views

import arena.Arena
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TextField
import tornadofx.*
import kotlin.random.Random

/**
 * Configuration and "launcher" for the bonus question, namely the random generation of an arena of an input size,
 * and running the iteration algorithms on the generated arena.
 *
 * Passes the results to the [BonusResultsView] for display.
 * @see BonusResultsView
 */
class BonusConfigView: View() {
    private var sizeField: TextField by singleAssign()

    override val root: Parent = vbox {
        padding = Insets(16.0)

        hbox {
            label("Size:")
            region { prefWidth = 4.0 }
            sizeField = textfield { maxWidth = 120.0; filterInput { it.controlNewText.isInt() } }
            alignment = Pos.CENTER_LEFT
        }

        region { prefHeight = 16.0 }

        button("Randomly Generate Arena") {
            isFocusTraversable = false
            useMaxWidth = true

            action {
                val size: Int = sizeField.text.toIntOrNull() ?: Random(System.currentTimeMillis()).nextInt(4, 64)

                if (size <= 2) {
                    error("Minimum size is 3.")
                    return@action
                }

                var valueLowest: Int = Int.MAX_VALUE
                var valueHighest = 0
                var policyLowest: Int = Int.MAX_VALUE
                var policyHighest = 0

                for (i in 0 until 10) {
                    Arena.generateRandom(size)
                    val constant: Double = MainView.valueIteration.convergenceConstant
                    MainView.valueIteration.start(Arena.grids, Arena.columns, Arena.rows, constant)
                    val valueIterations: Int = MainView.valueIteration.iterations
                    if (valueIterations > valueHighest) valueHighest = valueIterations
                    if (valueIterations < valueLowest) valueLowest = valueIterations

                    val iterations: Int = MainView.policyIteration.evaluationIteration
                    MainView.policyIteration.start(Arena.grids, Arena.columns, Arena.rows, iterations)
                    val policyIterations: Int = MainView.policyIteration.iterations
                    if (policyIterations > policyHighest) policyHighest = policyIterations
                    if (policyIterations < policyLowest) policyLowest = policyIterations
                    println("V: $valueIterations")
                    println("P: $policyIterations")
                }

                BonusResultsView.showResults(size, Pair(valueLowest, valueHighest), Pair(policyLowest, policyHighest))
            }
        }
    }
}