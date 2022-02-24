package tsql.ast.symbol_table

import tsql.ast.types.AbstractType

interface SymbolTableInterface {
    fun lookupAllRef(ident: String): AbstractType?
    fun lookupFunction(ident: String): Pair<AbstractType, List<AbstractType>>?
    fun getTypes(): ArrayList<AbstractType>
}
