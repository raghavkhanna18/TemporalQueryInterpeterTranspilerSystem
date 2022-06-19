package tsql.ast.nodes

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.error.SyntaxErrorListener

class ProgramConstructor(val syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ProgramAST>() {
    // Construct the program ast
    override fun visitProgram(ctx: TSQLParser.ProgramContext): ProgramAST {
        lateinit var program: ProgramAST
        program = ProgramAST()
        println(ctx.toStringTree())
        program.statementList.add(
            ctx.union_statement().accept(UnionStatementConstructor(syntaxErrorListener))
        )
        return program
    }
}
