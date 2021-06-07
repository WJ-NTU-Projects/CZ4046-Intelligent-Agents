package enums

/**
 * Enumerator for all possible actions of a state, with a [print] parameter defined for display purposes.
 */
enum class Action(val print: String) {
    UP("UP"),
    DOWN("DOWN"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    NONE("--"),
}