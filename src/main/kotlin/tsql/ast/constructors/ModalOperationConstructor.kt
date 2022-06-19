package tsql.ast.constructors

import antlr.TSQLParser
import antlr.TSQLParserBaseVisitor
import tsql.ast.types.EModalOperation
import tsql.ast.nodes.ModalOperationAST
import tsql.error.SyntaxErrorListener

class ModalOperationConstructor(syntaxErrorListener: SyntaxErrorListener) : TSQLParserBaseVisitor<ModalOperationAST>() {
    override fun visitModal_always_future(ctx: TSQLParser.Modal_always_futureContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.ALWAYS_FUTURE)
    }

    override fun visitModal_always_past(ctx: TSQLParser.Modal_always_pastContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.ALWAYS_PAST)
    }

    override fun visitModal_future(ctx: TSQLParser.Modal_futureContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.FUTURE)
    }

    override fun visitModal_next(ctx: TSQLParser.Modal_nextContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.NEXT)
    }

    override fun visitModal_past(ctx: TSQLParser.Modal_pastContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.PAST)
    }

    override fun visitModal_previous(ctx: TSQLParser.Modal_previousContext?): ModalOperationAST {
        return ModalOperationAST(EModalOperation.PREVIOUS)
    }
}
