package org.jetbrains.research.ktglean.angle.resolution

import org.jetbrains.research.ktglean.angle.data.Type

class TypeResolutionContext(
    private val unqualifiedTypes: Map<String, Type>,
    private val qualifiedTypes: Map<String, Map<String, Type>>
) {
    fun resolve(qname: String): Type {
        val lastDot = qname.lastIndexOf('.')
        if (lastDot == -1) return resolveUnqualified(qname)
        return resolveQualified(qname.substring(0, lastDot), qname.substring(lastDot + 1))
    }

    private fun resolveUnqualified(name: String): Type {
        return unqualifiedTypes[name] ?: error("Unresolved type $name")
    }

    private fun resolveQualified(qualifier: String, name: String): Type {
        return qualifiedTypes[qualifier]?.get(name) ?: error("Unresolved type $qualifier.$name")
    }
}
