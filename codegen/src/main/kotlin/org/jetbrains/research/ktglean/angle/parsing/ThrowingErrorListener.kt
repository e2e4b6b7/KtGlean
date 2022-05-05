package org.jetbrains.research.ktglean.angle.parsing

import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.ParseCancellationException


object ThrowingErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) = throw ParseCancellationException("line $line:$charPositionInLine $msg")
}
