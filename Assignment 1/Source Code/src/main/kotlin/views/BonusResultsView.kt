package views

import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.stage.Stage
import tornadofx.*

/**
 * Displays information pertaining to the bonus question:
 * - Arena size.
 * - No. of green and brown spaces, and walls.
 * - Minimum and maximum observed convergence iterations across 10 test runs for both value and policy iterations.
 */
class BonusResultsView: View() {
    companion object {
        private var currentStage: Stage? = null
        private var sizeLabel: Label by singleAssign()
        private var greenLabel: Label by singleAssign()
        private var brownLabel: Label by singleAssign()
        private var wallLabel: Label by singleAssign()
        private var valueIterationLabel: Label by singleAssign()
        private var policyIterationLabel: Label by singleAssign()

        fun showResults(size: Int, valueRange: Pair<Int, Int>, policyRange: Pair<Int, Int>) {
            val size2: Int = size - 1
            sizeLabel.text = "Size: $size x $size"
            greenLabel.text = "Green spaces: $size"
            brownLabel.text = "Brown spaces: $size2"
            wallLabel.text = "Walls: $size2"
            valueIterationLabel.text = "[Value] Iterations: ${valueRange.first} - ${valueRange.second}"
            policyIterationLabel.text = "[Policy] Iterations: ${policyRange.first} - ${policyRange.second}"
            currentStage?.sizeToScene()
        }
    }

    override val root: Parent = vbox {
        padding = Insets(16.0)
        sizeLabel = label("Size: ? x ?")
        region { prefHeight = 4.0 }
        greenLabel = label("Green spaces: ?")
        region { prefHeight = 4.0 }
        brownLabel = label("Brown spaces: ?")
        region { prefHeight = 4.0 }
        wallLabel = label("Walls: ?")
        region { prefHeight = 16.0 }
        valueIterationLabel = label("[Value] Iterations: ?")
        region { prefHeight = 4.0 }
        policyIterationLabel = label("[Policy] Iterations: ?")
    }

    init {
        Companion.currentStage = this.currentStage
    }
}