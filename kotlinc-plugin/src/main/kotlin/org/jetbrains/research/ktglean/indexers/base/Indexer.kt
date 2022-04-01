package org.jetbrains.research.ktglean.indexers.base

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

interface Indexer {
    fun singletonExtension(): (FirSession) -> FirAdditionalCheckersExtension
}
