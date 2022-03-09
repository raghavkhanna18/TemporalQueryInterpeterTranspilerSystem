package tsql.ast

import antlr.TSQLLexer
import antlr.TSQLParser
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.Trees
import tsql.ast.nodes.ProgramAST
import tsql.ast.nodes.ProgramConstructor
import tsql.ast.symbol_table.TopLevelSymbolTable
import tsql.error.ErrorAccumulator
import tsql.error.SemanticErrorListener
import tsql.error.SyntaxErrorListener

typealias TSQLParseTree = TSQLParser.ProgramContext

fun buildCST(parser: TSQLParser): TSQLParseTree {
    val root: ParseTree = parser.program()
    println(printSyntaxTree(parser, root))

    return root as TSQLParseTree
}

fun buildAST(parseTree: TSQLParseTree, syntaxErrorListener: SyntaxErrorListener): ProgramAST {
    return parseTree.accept(ProgramConstructor(syntaxErrorListener)) as ProgramAST
}

// Create a TSQLParser parse tree for an input string
fun constructParser(syntaxErrorAccumulator: ErrorAccumulator, inputString: String): TSQLParser {
    val syntaxErrorListener = SyntaxErrorListener(syntaxErrorAccumulator)
    val charStream = CharStreams.fromString(inputString)
    return constructParser(syntaxErrorListener, charStream)
}

fun constructParser(syntaxErrorListener: SyntaxErrorListener, charStream: CharStream): TSQLParser {
    // Create the lexer and token stream
    val lexer = TSQLLexer(charStream)
    lexer.removeErrorListeners()
    lexer.addErrorListener(syntaxErrorListener)
    val tokStream = BufferedTokenStream(lexer)

    // Build the parse tree for the input
    val parser = TSQLParser(tokStream)
    parser.removeErrorListeners()
    parser.addErrorListener(syntaxErrorListener)
    return parser
}

fun constructAndCreateAST(syntaxErrorAccumulator: ErrorAccumulator, semanticErrorAccumulator: ErrorAccumulator, input: String): ProgramAST {
    val charStream = CharStreams.fromString(input)
    return createAST(syntaxErrorAccumulator, semanticErrorAccumulator, charStream)
}

fun constructAndCreateASTFromContents(syntaxErrorAccumulator: ErrorAccumulator, semanticErrorAccumulator: ErrorAccumulator, fileContents: String): ProgramAST {
    val charStream = CharStreams.fromString(fileContents)
    return createAST(syntaxErrorAccumulator, semanticErrorAccumulator, charStream)
}

private fun createAST(
    syntaxErrorAccumulator: ErrorAccumulator,
    semanticErrorAccumulator: ErrorAccumulator,
    charStream: CharStream
): ProgramAST {
    val syntaxErrorListener = SyntaxErrorListener(syntaxErrorAccumulator)
    val semanticErrorListener = SemanticErrorListener(semanticErrorAccumulator)

    // Create parser and construct parse tree
    val parser: TSQLParser =
        constructParser(syntaxErrorListener, charStream)
    val parseTree = buildCST(parser)

    // Convert to abstract syntax tree
    val absSynTree = buildAST(parseTree, syntaxErrorListener)

    // Perform semantic analysis
    val topLevelSymbolTable = TopLevelSymbolTable()
    // absSynTree.checkNode(syntaxErrorListener, semanticErrorListener, topLevelSymbolTable)
        return absSynTree
    }

fun printSyntaxTree(parser: Parser, root: ParseTree): String? {
    val buf = StringBuilder()
    recursive(root, buf, 0, parser.getRuleNames().asList())
    return buf.toString()
}

private fun recursive(aRoot: ParseTree, buf: StringBuilder, offset: Int, ruleNames: List<String>) {
    for (i in 0 until offset) {
        buf.append("  ")
    }
    buf.append(Trees.getNodeText(aRoot, ruleNames)).append("\n")
    if (aRoot is ParserRuleContext) {
        val prc = aRoot as ParserRuleContext
        if (prc.children != null) {
            for (child in prc.children) {
                recursive(child, buf, offset + 1, ruleNames)
            }
        }
    }
}
