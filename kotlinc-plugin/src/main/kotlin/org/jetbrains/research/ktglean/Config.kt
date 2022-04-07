package org.jetbrains.research.ktglean

import org.jetbrains.research.ktglean.builders.GleanClassBuilder
import org.jetbrains.research.ktglean.builders.GleanFunctionBuilder
import org.jetbrains.research.ktglean.indexers.ClassDeclarationsIndexer
import org.jetbrains.research.ktglean.indexers.FunctionDeclarationsIndexer
import org.jetbrains.research.ktglean.indexers.base.Indexer
import org.jetbrains.research.ktglean.storage.FactsStorage
import org.jetbrains.research.ktglean.storage.JsonStorage
import org.koin.dsl.module

val storage = ::JsonStorage

val indexers = listOf<(FactsStorage) -> Indexer>(
    ::ClassDeclarationsIndexer,
    ::FunctionDeclarationsIndexer
)

val builders = module {
    single { GleanClassBuilder() }
    single { GleanFunctionBuilder() }
}
