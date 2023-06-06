package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.Token

data class LetStatement(
    val prefixToken: Token,
    val assignToken: Token,
    val identifier: Identifier,
    val value: Expression,
) : Statement {
    override val range: DocumentRange = prefixToken.range..value.range
    override val structure by lazy { Structure(identifier.structure, value.structure) }

    data class Structure(
        val identifier: Identifier.Structure,
        val value: StructuralExpression,
    ) : StructuralStatement {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            identifier.isEquivalentTo(other.identifier) &&
            value.isEquivalentTo(other.value)
    }
}
