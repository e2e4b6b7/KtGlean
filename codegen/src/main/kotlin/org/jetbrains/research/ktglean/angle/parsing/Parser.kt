package org.jetbrains.research.ktglean.angle.parsing

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleLexer
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleParser

fun parseSchemas(code: String): AngleParser.SchemasContext {
    val charStream = CharStreams.fromString(code)
    val lexer = AngleLexer(charStream)
    lexer.addErrorListener(ThrowingErrorListener)
    val tokensStream = CommonTokenStream(lexer)
    val parser = AngleParser(tokensStream)
    parser.addErrorListener(ThrowingErrorListener)
    return parser.schemas()
}
