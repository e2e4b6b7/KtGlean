package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.koin.core.component.KoinComponent

interface Indexer : KoinComponent {
    fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension
}
