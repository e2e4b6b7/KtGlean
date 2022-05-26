@file:Suppress(
    "RedundantVisibilityModifier",
    "unused",
)

package org.jetbrains.research.ktglean.predicates.kotlin.v1

import org.jetbrains.research.ktglean.serialization.Fact

public typealias Unit = kotlin.Unit

public data class Loc(
    public val `file`: File,
    public val offset: Int,
)

public enum class Visibility {
    Private,
    Protected,
    Public,
    Internal,
}

public enum class Variance {
    InVariance,
    OutVariance,
    Invariant,
}

public enum class Modifier {
    Data,
    Value,
    Sealed,
    Interface,
    Open,
}

public enum class Nullability {
    NotNull,
    Nullable,
    Unknown,
}

public data class File(
    public override val key: String,
) : Fact {
    public override val name: String
        get() = "kotlin.File.1"
}

public data class Package(
    public override val key: String,
) : Fact {
    public override val name: String
        get() = "kotlin.Package.1"
}

public data class QName(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.QName.1"

    public constructor(`package`: Package, name: String) : this(Key(`package`, name))

    public data class Key(
        public val `package`: Package,
        public val name: String,
    )
}

public data class TypeRef(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.TypeRef.1"

    public sealed interface Key {
        public data class ExplicitRef(
            public val explicitRef: ExplicitRef,
        ) : Key {
            public data class ExplicitRef(
                public val `class`: QName,
                public val typeParameters: List<Type>,
            )
        }

        public data class ParameterRef(
            public val parameterRef: String,
        ) : Key

        public data class Star(
            public val star: Unit,
        ) : Key
    }
}

public data class Type(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.Type.1"

    public constructor(ref: TypeRef, nullability: Nullability) : this(Key(ref, nullability))

    public data class Key(
        public val ref: TypeRef,
        public val nullability: Nullability,
    )
}

public data class TypeParameter(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.TypeParameter.1"

    public constructor(
        name: String,
        isReified: Boolean,
        bound: List<Type>,
        variance: Variance,
    ) : this(Key(name, isReified, bound, variance))

    public data class Key(
        public val name: String,
        public val isReified: Boolean,
        public val bound: List<Type>,
        public val variance: Variance,
    )
}

public data class ClassDeclaration(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.ClassDeclaration.1"

    public constructor(
        name: QName,
        supertypes: List<TypeRef>,
        typeParams: List<TypeParameter>,
        modifiers: List<Modifier>,
        loc: Loc,
    ) : this(Key(name, supertypes, typeParams, modifiers, loc))

    public data class Key(
        public val name: QName,
        public val supertypes: List<TypeRef>,
        public val typeParams: List<TypeParameter>,
        public val modifiers: List<Modifier>,
        public val loc: Loc,
    )
}

public data class FunctionDeclaration(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.FunctionDeclaration.1"

    public constructor(
        name: QName,
        argTypes: List<Type>,
        typeParams: List<TypeParameter>,
        returnType: Type,
        modifiers: List<Modifier>,
        loc: Loc,
    ) : this(Key(name, argTypes, typeParams, returnType, modifiers, loc))

    public data class Key(
        public val name: QName,
        public val argTypes: List<Type>,
        public val typeParams: List<TypeParameter>,
        public val returnType: Type,
        public val modifiers: List<Modifier>,
        public val loc: Loc,
    )
}

public data class PropertyDeclaration(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.PropertyDeclaration.1"

    public constructor(
        name: QName,
        typeParams: List<TypeParameter>,
        type: Type,
        mutable: Boolean,
        modifiers: List<Modifier>,
        loc: Loc,
    ) : this(Key(name, typeParams, type, mutable, modifiers, loc))

    public data class Key(
        public val name: QName,
        public val typeParams: List<TypeParameter>,
        public val type: Type,
        public val mutable: Boolean,
        public val modifiers: List<Modifier>,
        public val loc: Loc,
    )
}

public data class XRefTarget(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.XRefTarget.1"

    public sealed interface Key {
        public data class Class_(
            public val class_: ClassDeclaration,
        ) : Key

        public data class Function_(
            public val function_: FunctionDeclaration,
        ) : Key

        public data class Property_(
            public val property_: PropertyDeclaration,
        ) : Key
    }
}

public data class XRef(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.XRef.1"

    public constructor(target: XRefTarget, loc: Loc) : this(Key(target, loc))

    public data class Key(
        public val target: XRefTarget,
        public val loc: Loc,
    )
}

public data class FunctionCall(
    public override val key: Key,
) : Fact {
    public override val name: String
        get() = "kotlin.FunctionCall.1"

    public constructor(function: FunctionDeclaration, loc: Loc) : this(Key(function, loc))

    public data class Key(
        public val function: FunctionDeclaration,
        public val loc: Loc,
    )
}
