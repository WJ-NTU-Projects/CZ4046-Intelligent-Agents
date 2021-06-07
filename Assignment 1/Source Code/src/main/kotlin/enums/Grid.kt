package enums

import javafx.scene.paint.Color

/**
 * Enumerator for all possible grid types. Contains additional defined parameters:
 * - [reward]: Reward for that grid type.
 * - [color]: Color for display for that grid type.
 */
enum class Grid(val reward: Double, val color: Color) {
    WHITE(-0.04, Color.rgb(240, 240, 240)),
    BROWN(-1.0, Color.rgb(255, 198, 162)),
    GREEN(1.0, Color.rgb(198, 255, 198)),
    WALL(0.0, Color.rgb(100, 100, 100)),
    START(-0.04, Color.rgb(255, 198, 198))
}