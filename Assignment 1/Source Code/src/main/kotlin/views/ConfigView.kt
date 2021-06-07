package views

import Constants
import arena.Arena
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.text.FontWeight
import tornadofx.*

/**
 * Configuration for the value and policy iteration algorithms (applies to both parts of the assignment).
 */
class ConfigView: View() {
    private var constantField: TextField by singleAssign()
    private var iterationField: TextField by singleAssign()

    override val root: Parent = vbox {
        padding = Insets(16.0)
        label("Convergence Constant:") { style { fontWeight = FontWeight.BOLD } }
        region { prefHeight = 4.0 }
        constantField = textfield { maxWidth = 120.0 }

        region { prefHeight = 8.0 }

        label("Policy Evaluation Iterations:") { style { fontWeight = FontWeight.BOLD } }
        region { prefHeight = 4.0 }
        iterationField = textfield { maxWidth = 120.0; filterInput { it.controlNewText.isInt() } }

        region { prefHeight = 16.0 }

        hbox {
            button("Value Iterate") {
                isFocusTraversable = false
                prefWidth = 120.0

                action {
                    val default: Int = Constants.DEFAULT_ROW_COLUMN
                    if (Arena.columns != default || Arena.rows != default) Arena.setDefault()
                    val constant: Double = constantField.text.toDoubleOrNull() ?: Constants.CONVERGENCE_CONSTANT
                    MainView.valueIteration.start(Arena.grids, Arena.columns, Arena.rows, constant)

                    ArenaView.showResults(
                        MainView.valueIteration.iterations,
                        MainView.valueIteration.utilities,
                        MainView.valueIteration.policy,
                        threshold = MainView.valueIteration.convergenceThreshold
                    )
                }
            }

            region { prefWidth = 8.0 }

            button("Policy Iterate") {
                isFocusTraversable = false
                prefWidth = 120.0

                action {
                    val default: Int = Constants.DEFAULT_ROW_COLUMN
                    if (Arena.columns != default || Arena.rows != default) Arena.setDefault()
                    val iterations: Int = iterationField.text.toIntOrNull() ?: Constants.EVALUATION_ITERATION
                    MainView.policyIteration.start(Arena.grids, Arena.columns, Arena.rows, iterations)

                    ArenaView.showResults(
                        MainView.policyIteration.iterations,
                        MainView.policyIteration.utilities,
                        MainView.policyIteration.policy
                    )
                }
            }
        }
    }
}