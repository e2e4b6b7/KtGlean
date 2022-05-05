package org.jetbrains.research.ktglean.angle.resolution

import org.jetbrains.research.ktglean.angle.data.*
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleParser
import org.jetbrains.research.ktglean.angle.parser.antlr.AngleParserBaseVisitor

class TypeResolver(context: TypeResolutionContext) {
    private val visitor = TypeResolvingVisitor(context)

    fun resolve(ctx: AngleParser.TypeContext): Type = visitor.visitType(ctx)

    private class TypeResolvingVisitor(val context: TypeResolutionContext) : AngleParserBaseVisitor<Type>() {
        override fun visitReference(ctx: AngleParser.ReferenceContext): Type {
            return when (val qname = ctx.text) {
                "nat" -> NatType
                "string" -> StringType
                "bool" -> BoolType
                "byte" -> ByteType
                else -> context.resolve(qname)
            }
        }

        override fun visitRecord(ctx: AngleParser.RecordContext): Type {
            val fields = ctx.fielddef().asSequence()
                .map { Pair(it.IDENT().text, visitType(it.type())) }
                .toMap()
            return RecordType(fields)
        }

        override fun visitSum(ctx: AngleParser.SumContext): Type {
            val options = ctx.fielddef().asSequence()
                .map { Pair(it.IDENT().text, visitType(it.type())) }
                .toMap()
            return SumType(options)
        }

        override fun visitArray(ctx: AngleParser.ArrayContext): Type {
            return ArrayType(visitType(ctx.type()))
        }

        override fun visitMaybe(ctx: AngleParser.MaybeContext): Type {
            return MaybeType(visitType(ctx.type()))
        }

        override fun visitEnum_(ctx: AngleParser.Enum_Context): Type {
            return EnumType(ctx.IDENT().map { it.text })
        }
    }
}
