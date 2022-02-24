package tsql.error

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class SyntaxErrorListener(private val errorAccumulator: ErrorAccumulator) : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        if (msg != null) {
            syntaxError(
                line,
                charPositionInLine,
                msg.replace("INT_LITERAL", "'int'")
                    .replace("CHAR_LITERAL", "'chr'")
                    .replace("STRING_LITERAL", "'string'")
                    .replace("IDENT", "'Identifier'")
            )
        } else {
            syntaxError(
                line,
                charPositionInLine,
                ""
            )
        }
    }

    fun syntaxError(
        line: Int,
        charPositionInLine: Int,
        msg: String?
    ) {
        errorAccumulator.addError(SyntaxError(line, charPositionInLine, msg.orEmpty()))
    }
}
