package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.jetbrains.research.ktglean.angle.data.*
import org.jetbrains.research.ktglean.angle.generation.context.*

fun GenerationContext.generateType(type: Type, reservedName: String) = when (type) {
    is NatType -> NAT_CLASS
    is BoolType -> BOOL_CLASS
    is StringType -> STRING_CLASS
    is ByteType -> BYTE_CLASS
    is RecordType -> generateRecord(type, reservedName)
    is ReferenceType -> generateReferenceType(type)
    is ArrayType -> generateArrayType(type, reservedName)
    is MaybeType -> generateMaybeType(type, reservedName)
    is EnumType -> generateEnumType(type, reservedName)
    is SumType -> generateSumType(type, reservedName)
}

private fun GenerationContext.generateSumType(type: SumType, reservedName: String): KotlinClass {
    return innerSealed(reservedName) {
        for ((optionName, optionType) in type.options) {
            innerClass(generateTypeName(optionName)) {
                val optionClass = generateType(optionType, generateTypeName(optionName))
                addPrimaryProperty(optionName, optionClass.name)
                addSuperinterface(this@innerSealed.className)
            }
        }
    }
}

private fun GenerationContext.generateEnumType(type: EnumType, reservedName: String): KotlinClass {
    if (type.values.isEmpty()) return UNIT_CLASS
    return innerEnum(reservedName) {
        type.values.forEach(::addConstant)
    }
}

private fun GenerationContext.generateMaybeType(type: MaybeType, reservedName: String): KotlinClass {
    val originalClass = generateType(type.originalType, reservedName)
    return KotlinClass(originalClass.name.copy(nullable = true))
}

private fun GenerationContext.generateArrayType(type: ArrayType, reservedName: String): KotlinClass {
    val elementClass = generateType(type.elementType, reservedName + "Elem")
    return KotlinClass(ARRAY_TYPE.parameterizedBy(elementClass.name))
}

private fun GenerationContext.generateRecord(type: RecordType, reservedName: String): KotlinClass {
    if (type.fields.isEmpty()) return UNIT_CLASS
    return innerClass(reservedName) {
        for ((fieldName, fieldType) in type.fields) {
            val fieldClass = generateType(fieldType, generateTypeName(fieldName))
            addPrimaryProperty(fieldName, fieldClass.name)
        }
    }
}

private fun GenerationContext.generateReferenceType(type: ReferenceType) = KotlinClass(resolver.classNameOf(type.id))

private fun generateTypeName(fieldName: String): String = fieldName.replaceFirstChar { it.uppercaseChar() }
