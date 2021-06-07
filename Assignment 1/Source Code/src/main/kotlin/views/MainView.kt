package views

import algorithm.PolicyIteration
import algorithm.ValueIteration
import javafx.scene.Parent
import javafx.scene.layout.VBox
import tornadofx.*

/**
 * Main view containing all other views (master view tying up all other views into one window / stage).
 */
class MainView: View("CZ4046 Assignment 1") {
    companion object {
        val policyIteration = PolicyIteration()
        val valueIteration = ValueIteration()
    }

    private var rootBox: VBox by singleAssign()
    private val bonusConfigView: BonusConfigView by inject()
    private val arenaView: ArenaView by inject()
    private val configView: ConfigView by inject()
    private val bonusResultsView: BonusResultsView by inject()

    override val root: Parent = borderpane {
        style = "-fx-font-family: 'Verdana'; -fx-background-color: #FFFFFF"

        center {
            rootBox = vbox {
                add(configView)
                add(arenaView)
            }
        }

        right {
            vbox {
                add(bonusConfigView)
                add(bonusResultsView)
            }
        }
    }

    init {
        currentStage?.isResizable = false
        currentStage?.sizeToScene()

        runLater {
            rootBox.requestFocus()
        }
    }
}