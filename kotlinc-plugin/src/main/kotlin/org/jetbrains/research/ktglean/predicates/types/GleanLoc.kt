package org.jetbrains.research.ktglean.predicates.types

import org.jetbrains.research.ktglean.predicates.GleanFile

data class GleanLoc(val file: GleanFile, val offset: Int) {
    companion object {
        val UNRESOLVED = GleanLoc(GleanFile(""), 0)
    }
}
