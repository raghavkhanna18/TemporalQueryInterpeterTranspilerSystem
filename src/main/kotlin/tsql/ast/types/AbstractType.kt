package tsql.ast.types

// Abstract type that a variable can be.
interface AbstractType {
    fun getType(): EType

    // True if this type is compatible with the other type. Not commutative!
    // ( e.g. char[].isCompatibleWith(string) but !(string.isCompatibleWith(char[])) )
    fun isCompatibleWith(type: AbstractType): Boolean
}
