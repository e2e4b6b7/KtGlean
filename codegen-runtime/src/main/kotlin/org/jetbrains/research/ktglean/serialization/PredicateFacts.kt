package org.jetbrains.research.ktglean.serialization

import com.google.gson.GsonBuilder
import com.google.gson.annotations.JsonAdapter

class PredicateFacts(
    val predicate: String,
    @JsonAdapter(FactsSerializer::class)
    val facts: MutableSet<Fact> = HashSet()
) {
    companion object {
        fun writeBatch(facts: Collection<PredicateFacts>, output: Appendable) {
            val gson = GsonBuilder().apply {
                registerTypeHierarchyAdapter(Fact::class.java, BackReferenceSerializer())
            }.create()

            gson.toJson(facts, output)
        }
    }
}
