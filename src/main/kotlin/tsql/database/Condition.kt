package tsql.database

import tsql.ast.types.EBinOp
import tsql.ast.types.EType

data class Condition(
    val lhs: String,
    val lhsType: EType = EType.STRING,
    val lhsIsLiteral: Boolean = false,
    val comparator: EBinOp,
    val rhs: String,
    val rhsType: EType = EType.STRING,
    val rhsIsLiteral: Boolean = false
) {
    fun flip(): Condition {
        return when (comparator) {
            EBinOp.EQUAL, EBinOp.NOT_EQUAL, EBinOp.AND, EBinOp.OR -> Condition(
                rhs,
                rhsType,
                rhsIsLiteral,
                comparator,
                lhs,
                lhsType,
                lhsIsLiteral
            )
            EBinOp.GREATER -> Condition(rhs, rhsType, rhsIsLiteral, EBinOp.LESS, lhs, lhsType, lhsIsLiteral)
            EBinOp.GREATER_EQUAL -> Condition(rhs, rhsType, rhsIsLiteral, EBinOp.LESS_EQUAL, lhs, lhsType, lhsIsLiteral)
            EBinOp.LESS -> Condition(rhs, rhsType, rhsIsLiteral, EBinOp.GREATER, lhs, lhsType, lhsIsLiteral)
            EBinOp.LESS_EQUAL -> Condition(rhs, rhsType, rhsIsLiteral, EBinOp.GREATER_EQUAL, lhs, lhsType, lhsIsLiteral)
        }
    }
}
