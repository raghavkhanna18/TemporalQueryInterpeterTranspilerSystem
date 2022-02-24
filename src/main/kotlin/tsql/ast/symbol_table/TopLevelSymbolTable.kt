package tsql.ast.symbol_table

import tsql.ast.types.AbstractType

// Symbol table in global scope.
class TopLevelSymbolTable : SymbolTableInterface {
    override fun getTypes(): ArrayList<AbstractType> {
        return ArrayList()
    }

    // Map of function identifier -> (return type, parameters).
    val functionTable = HashMap<String, Pair<AbstractType, List<AbstractType>>>()

    // Map of variable names -> variable type.
    val referenceTable = HashMap<String, AbstractType>()

    override fun lookupFunction(ident: String): Pair<AbstractType, List<AbstractType>>? {
        return functionTable[ident]
    }
    fun addFunction(ident: String, signature: Pair<AbstractType, List<AbstractType>>) {
        functionTable.put(ident, signature)
    }

    override fun lookupAllRef(ident: String): AbstractType? {
        return referenceTable[ident]
    }
}
