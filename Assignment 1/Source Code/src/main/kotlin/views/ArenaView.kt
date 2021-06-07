package views

import arena.Arena
import enums.Action
import enums.Grid
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.GridPane
import javafx.scene.shape.Rectangle
import javafx.scene.text.FontWeight
import tornadofx.*

/**
 * Contains the arena map view, along with the iterations and convergence threshold labels.
 *
 * Displays the optimal policy and utilities for all states after a successful value iteration or policy iteration run.
 *
 * Provides the functionality to copy the calculated final utilities and policy as a LaTEX formatted table on to
 * the clipboard (report purpose).
 */
class ArenaView: View() {
    companion object {
        private const val GRID_SIZE = 60.0
        private const val MARGIN_SIZE = 1.0
        private val rectangles: Array<Array<Rectangle>> = Array(Arena.rows) { Array(Arena.columns) { Rectangle() } }
        private val policyLabels: Array<Array<Label>> = Array(Arena.rows) { Array(Arena.columns) { Label() } }
        private val utilityLabels: Array<Array<Label>> = Array(Arena.rows) { Array(Arena.columns) { Label() } }

        private var policyCopyButton: Button by singleAssign()
        private var utilityCopyButton: Button by singleAssign()
        private var iterationLabel: Label by singleAssign()
        private var thresholdLabel: Label by singleAssign()
        private var policyLatex: String = ""
        private var utilityLatex: String = ""

        fun showResults(iterations: Int, utilities: Array<Array<Double>>, policy: Array<Array<Action>>, threshold: Double? = null) {
            iterationLabel.text = "Iterations: $iterations"
            thresholdLabel.text = "Convergence Threshold: ${threshold ?: "-"}"
            policyLatex = ""
            utilityLatex = ""

            for (row in policy.indices) {
                for (column in policy[row].indices) {
                    val policyText: String = policy[row][column].print
                    val utilityText: String = String.format("%.3f", utilities[row][column])

                    if (Arena.grids[row][column] != Grid.WALL) {
                        policyLabels[row][column].text = policyText
                        utilityLabels[row][column].text = utilityText
                    } else {
                        policyLabels[row][column].text = ""
                        utilityLabels[row][column].text = ""
                    }

                    if (column > 0) {
                        policyLatex += " & "
                        utilityLatex += " & "
                    }

                    policyLatex += policyText
                    utilityLatex += utilityText

                    rectangles[row][column].fill = Arena.grids[row][column].color
                }

                policyLatex += " \\\\\n"
                policyLatex += "\\hline\n"
                utilityLatex += " \\\\\n"
                utilityLatex += "\\hline\n"
            }

            if (policyLatex.isNotEmpty()) policyCopyButton.isDisable = false
            if (utilityLatex.isNotEmpty()) utilityCopyButton.isDisable = false
        }
    }

    private var gridPane: GridPane by singleAssign()

    override val root: Parent = vbox {
        padding = Insets(16.0)

        policyCopyButton = button("[Policy] Copy LaTEX table string") {
            action { Clipboard.getSystemClipboard().setContent(ClipboardContent().apply { putString(policyLatex) }) }
        }

        region { prefHeight = 8.0 }

        utilityCopyButton = button("[Utility] Copy LaTEX table string") {
            action { Clipboard.getSystemClipboard().setContent(ClipboardContent().apply { putString(utilityLatex) }) }
        }

        region { prefHeight = 16.0 }
        iterationLabel = label("Iterations: ?") { style { fontWeight = FontWeight.BOLD } }
        region { prefHeight = 4.0 }
        thresholdLabel = label("Convergence Threshold: -") { style { fontWeight = FontWeight.BOLD } }
        region { prefHeight = 16.0 }

        gridPane = gridpane {
            for ((row, r) in Arena.grids.withIndex()) {
                row {
                    for ((column, c) in r.withIndex()) {
                        stackpane {
                            rectangles[row][column] = rectangle(width = GRID_SIZE, height = GRID_SIZE) { fill = c.color }

                            vbox {
                                policyLabels[row][column] = label("") { style { fontWeight = FontWeight.BOLD } }
                                region { prefHeight = 4.0 }
                                utilityLabels[row][column] = label("")
                                alignment = Pos.CENTER
                            }

                            gridpaneConstraints { margin = Insets(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE) }
                        }
                    }
                }
            }
        }
    }

    init {
        Arena.setDefault()

        runLater {
            for ((row, r) in Arena.grids.withIndex()) {
                for ((column, c) in r.withIndex()) rectangles[row][column].fill = c.color
            }
        }
    }
}