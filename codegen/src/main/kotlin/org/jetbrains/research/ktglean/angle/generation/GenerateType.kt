package org.jetbrains.research.ktglean.angle.generation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import org.jetbrains.research.ktglean.angle.data.*
import org.jetbrains.research.ktglean.angle.generation.context.*


fun GenerationContext.generateType(type: Type, reservedName: String) = when (type) {
    is NatType -> NAT_TYPE
    is BoolType -> BOOL_TYPE
    is StringType -> STRING_TYPE
    is ByteType -> BYTE_TYPE
    is RecordType -> generateRecord(type, reservedName)
    is ReferenceType -> generateReferenceType(type)
    is ArrayType -> generateArrayType(type, reservedName)
    is MaybeType -> generateMaybeType(type, reservedName)
    is EnumType -> generateEnumType(type, reservedName)
    is SumType -> generateSumType(type, reservedName)
}

private fun GenerationContext.generateSumType(type: SumType, reservedName: String): TypeName {
    return innerSealed(reservedName) {
        for ((optionName, optionType) in type.options) {
            innerClass(generateTypeName(optionName)) {
                val optionClassName = generateType(optionType, generateTypeName(optionName))
                addPrimaryProperty(optionName, optionClassName)
                addSuperinterface(this@innerSealed.className)
            }
        }
    }
}

private fun GenerationContext.generateEnumType(type: EnumType, reservedName: String): TypeName {
    if (type.values.isEmpty()) return UNIT_TYPE
    return innerEnum(reservedName) {
        type.values.forEach(::addConstant)
    }
}

private fun GenerationContext.generateMaybeType(type: MaybeType, reservedName: String): TypeName {
    val originalType = generateType(type.originalType, reservedName)
    return originalType.copy(nullable = true)
}

private fun GenerationContext.generateArrayType(type: ArrayType, reservedName: String): TypeName {
    val elementType = generateType(type.elementType, reservedName + "Elem")
    return ARRAY_TYPE.parameterizedBy(elementType)
}

private fun GenerationContext.generateRecord(type: RecordType, reservedName: String): ClassName {
    if (type.fields.isEmpty()) return UNIT_TYPE
    return innerClass(reservedName) {
        for ((fieldName, fieldType) in type.fields) {
            val fieldClassName = generateType(fieldType, generateTypeName(fieldName))
            addPrimaryProperty(fieldName, fieldClassName)
        }
    }
}

private fun GenerationContext.generateReferenceType(type: ReferenceType) = resolver.classNameOf(type.id)

private fun generateTypeName(fieldName: String): String = fieldName.replaceFirstChar { it.uppercaseChar() }
