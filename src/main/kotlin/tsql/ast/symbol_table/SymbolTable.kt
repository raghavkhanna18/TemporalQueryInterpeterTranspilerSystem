package tsql.ast.symbol_table

import tsql.ast.types.AbstractType

class SymbolTable(val enclosure: SymbolTableInterface) :
    SymbolTableInterface {
    override fun getTypes(): ArrayList<AbstractType> {
        return ArrayList(referenceTable.values)
    }

    // If a function is defined return a pair <return type, types of arguments>.
    // Otherwise return null.
    fun lookupScopeRef(ident: String): AbstractType? {
        return referenceTable[ident]
    }

    // Map of variable names in this scope to their types.
    private val referenceTable = HashMap<String, AbstractType>()

    // Recursively lookup variable type in this or parent scope. Null if not defined.
    override fun lookupAllRef(ident: String): AbstractType? {
        return referenceTable[ident] ?: return enclosure.lookupAllRef(ident)
    }

    // Add a reference to an identifier. Used for var declaration.
    fun addRef(ident: String, type: AbstractType) {
        referenceTable.put(ident, type)
    }

    // Lookup a function in global function table.
    override fun lookupFunction(ident: String): Pair<AbstractType, List<AbstractType>>? {
        return enclosure.lookupFunction(ident)
    }
}
