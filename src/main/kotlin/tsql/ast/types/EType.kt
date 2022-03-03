package tsql.ast.types

// All types in wacc language.
enum class EType {
    INT, BOOL, STRING, DATE,
    UNKNOWN, NULL, STATEMENT, ERROR,
    NUMBER, BLOB, TIMESTAMP, DATETIME;

    fun isSimpleValueType(): Boolean {
        return when (this) {
            INT, BOOL, DATE, STRING -> true
            else -> false
        }
    }
}
