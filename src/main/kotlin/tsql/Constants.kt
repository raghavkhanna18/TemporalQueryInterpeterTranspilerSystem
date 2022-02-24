package tsql

object Constants {
    const val RETURN = 0
    const val SP = 13
    const val LR = 14
    const val PC = 15

    const val INITIAL_TARGET_REGISTER = 4
    const val WORD_SIZE = 4
    const val BYTE = 1

    const val SUCCESS_EXIT_CODE = 0
    const val SYNTAX_EXIT_CODE = 100
    const val SEMANTIC_EXIT_CODE = 200
    const val RUNTIME_ERROR_CODE = -1
    const val MAIN_F = "#main"

    const val MAX_CONSTANT = 1024
    const val MAX_POWER = 31

    const val TRUE_CONST = 1
    const val FALSE_CONST = 0
    const val PAIR_SIZE = 2
    const val SECOND_ARG_REG = 1
    const val STACK_TIME = 10
}
