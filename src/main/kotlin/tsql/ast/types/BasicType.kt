package tsql.ast.types

// All basic types will be of this type.
class BasicType(val valueType: EType) : AbstractType {
    override fun getType(): EType {
        return valueType
    }

    override fun isCompatibleWith(type: AbstractType): Boolean {
        // Anything is compatible with UNKNOWN type.
        if (valueType == EType.UNKNOWN) {
            return true
        }

        return when (type.getType()) {
            EType.UNKNOWN, valueType -> true
            else -> false
        }
    }

    override fun toString(): String {
        return "$valueType"
    }
}
