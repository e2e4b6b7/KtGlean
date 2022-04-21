package org.jetbrains.research.ktglean

import org.jetbrains.research.ktglean.factories.*
import org.jetbrains.research.ktglean.indexers.*
import org.jetbrains.research.ktglean.indexers.base.Indexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.jetbrains.research.ktglean.storage.JsonStorage
import org.koin.dsl.module

val storage = ::JsonStorage

val indexers = listOf<(FactsStorage) -> Indexer>(
    ::ClassDeclarationsIndexer,
    ::FunctionDeclarationsIndexer,
    ::FloatLiteralInCallIndexer,
    ::RangeCallIndexer
)

val factories = module {
    single { GleanClassFactory() }
    single { GleanFunctionFactory() }
    single { GleanFunctionCallFactory() }
    single { GleanFloatLiteralUsageFactory() }
    single { GleanRangeCallFactory() }
}
