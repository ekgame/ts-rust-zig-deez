package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.PrefixOperation
import monkeylang.Token

data class PrefixExpression(
    val operator: Token,
    val expression: Expression,
    val operation: PrefixOperation,
) : Expression {
    override val range: DocumentRange = operator.range..expression.range
    override val structure by lazy { Structure(expression.structure, operation) }

    data class Structure(
        val expression: StructuralExpression,
        val operation: PrefixOperation,
    ) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            operation == other.operation &&
            expression.isEquivalentTo(other.expression)
    }
}
