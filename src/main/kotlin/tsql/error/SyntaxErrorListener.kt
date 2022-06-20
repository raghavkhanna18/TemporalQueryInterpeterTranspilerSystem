package tsql.error

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class SyntaxErrorListener : BaseErrorListener() {
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
                msg
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
        println("line: $line:$charPositionInLine, $msg")
    }
}
