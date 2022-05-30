package tsql.ast.types

// All types in TSQL language.
enum class EType(s: String) {
    INT("int8"),
    DECIMAL("decimal"),
    BOOL("bool"),
    STRING("string"),
    DATE("date"),
    DOUBLE("double"),
    FLOAT("float"),
    UNKNOWN("unknown"),
    NULL("null"),
    ERROR("error"),
    NUM("number"),
    BLOB("int8"),
    TIMESTAMP("timestamp"),
    DATETIME("datetime"),
    BIGINT("bigint");

    fun isNumeric(): Boolean {
        return when (this) {
            BIGINT, NUM, DECIMAL, FLOAT, DOUBLE -> true
            else -> false
        }
    }
}
