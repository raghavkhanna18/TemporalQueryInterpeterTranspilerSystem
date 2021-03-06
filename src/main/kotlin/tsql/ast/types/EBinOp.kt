package tsql.ast.types


// All binary operators.
enum class EBinOp(val repr: String) {
    AND("AND"),
    EQUAL("="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),
    NOT_EQUAL("!="),
    OR("OR");

    override fun toString(): String {
        return when(this){
            AND -> " AND "
            EQUAL -> "="
            GREATER -> ">"
            GREATER_EQUAL -> ">="
            LESS -> "<"
            LESS_EQUAL -> "<="
            NOT_EQUAL -> "!="
            OR -> " OR "
        }
    }


}
