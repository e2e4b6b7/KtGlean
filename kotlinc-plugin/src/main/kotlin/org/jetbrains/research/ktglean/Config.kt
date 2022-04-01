package org.jetbrains.research.ktglean

import org.jetbrains.research.ktglean.indexers.ClassNamesIndexer
import org.jetbrains.research.ktglean.storage.LoggingStorage

val storage = ::LoggingStorage

val indexers = listOf(
    ::ClassNamesIndexer
)
