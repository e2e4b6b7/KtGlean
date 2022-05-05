package org.jetbrains.research.ktglean.angle.generation.context

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import org.jetbrains.research.ktglean.angle.generation.SUPPRESS_TYPE

fun FileSpec.Builder.suppressWarnings(vararg types: String) {
    addAnnotation(AnnotationSpec.builder(SUPPRESS_TYPE).apply {
        types.forEach { addMember("%S", it) }
    }.build())
}
