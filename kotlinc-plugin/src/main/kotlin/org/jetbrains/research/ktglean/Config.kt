package org.jetbrains.research.ktglean

import org.jetbrains.research.ktglean.indexers.ClassNamesIndexer
import org.jetbrains.research.ktglean.storage.JsonStorage

val storage = ::JsonStorage

val indexers = listOf(
    ::ClassNamesIndexer
)
