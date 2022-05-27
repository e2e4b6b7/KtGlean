package org.jetbrains.research.ktglean.factories

import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.getModifierList
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirQualifiedAccess
import org.jetbrains.kotlin.fir.resolve.firClassLike
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.resolve.toSymbol
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.research.ktglean.predicates.kotlin.v1.*
import org.jetbrains.research.ktglean.predicates.kotlin.v1.Variance as GleanVariance

fun FirTypeRef.toFirClass(context: CheckerContext): FirClass? =
    firClassLike(context.session) as? FirClass

@OptIn(SymbolInternals::class)
fun ClassId.toFirClass(context: CheckerContext) =
    context.session.symbolProvider.getClassLikeSymbolByClassId(this)?.fir as? FirClass

@OptIn(SymbolInternals::class)
fun ConeKotlinType.toFirClass(context: CheckerContext) =
    (this as? ConeClassLikeType)?.lookupTag?.toSymbol(context.session)?.fir as? FirClass

val FirElement.loc: Loc
    get() {
        val psi = psi ?: return unresolved()
        val fileName = psi.containingFile.name
        return Loc(File(fileName), psi.startOffset)
    }

@OptIn(SymbolInternals::class)
val FirFunctionCall.callee: FirFunction?
    get() = calleeReference.resolvedSymbol?.fir as? FirFunction

@OptIn(SymbolInternals::class)
val FirQualifiedAccess.callee: FirDeclaration?
    get() = calleeReference.resolvedSymbol?.fir

val FqName.gleanPackage get() = Package(asString())

val ClassId.qname get() = QName(packageFqName.gleanPackage, shortClassName.asString())

val CallableId.qname get() = QName((classId?.asSingleFqName() ?: packageName).gleanPackage, callableName.asString())

val FirElement.modifiersList
    get() = source.getModifierList()?.modifiers?.mapNotNull {
        when (it.token) {
            KtTokens.DATA_KEYWORD -> Modifier.Data
            KtTokens.VALUE_KEYWORD -> Modifier.Value
            KtTokens.SEALED_KEYWORD -> Modifier.Sealed
            KtTokens.ANNOTATION_KEYWORD -> Modifier.Annotation
            KtTokens.ENUM_KEYWORD -> Modifier.Enum

            KtTokens.PUBLIC_KEYWORD -> Modifier.Public
            KtTokens.INTERNAL_KEYWORD -> Modifier.Internal
            KtTokens.PROTECTED_KEYWORD -> Modifier.Protected
            KtTokens.PRIVATE_KEYWORD -> Modifier.Private

            KtTokens.INTERFACE_KEYWORD -> Modifier.Interface
            KtTokens.ABSTRACT_KEYWORD -> Modifier.Abstract
            KtTokens.OPEN_KEYWORD -> Modifier.Open
            KtTokens.FINAL_KEYWORD -> Modifier.Final

            KtTokens.EXPECT_KEYWORD -> Modifier.Expect
            KtTokens.ACTUAL_KEYWORD -> Modifier.Actual

            KtTokens.INNER_KEYWORD -> Modifier.Inner
            else -> null
        }
    } ?: emptyList()

val Variance.gleanVariance
    get() = when (this) {
        Variance.INVARIANT -> GleanVariance.Invariant
        Variance.IN_VARIANCE -> GleanVariance.InVariance
        Variance.OUT_VARIANCE -> GleanVariance.OutVariance
    }

val ConeNullability.gleanNullability
    get() = when (this) {
        ConeNullability.NULLABLE -> Nullability.Nullable
        ConeNullability.UNKNOWN -> Nullability.Unknown
        ConeNullability.NOT_NULL -> Nullability.NotNull
    }
