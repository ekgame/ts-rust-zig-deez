package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.InfixOperation
import monkeylang.Token

data class InfixExpression(
    val left: Expression,
    val operator: Token,
    val right: Expression,
    val operation: InfixOperation,
) : Expression {
    override val range: DocumentRange = left.range..right.range
    override val structure by lazy { Structure(left.structure, right.structure, operation) }

    data class Structure(
        val left: StructuralExpression,
        val right: StructuralExpression,
        val operation: InfixOperation,
    ) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            left.isEquivalentTo(other.left) &&
            operation == other.operation &&
            right.isEquivalentTo(other.right)
    }
}
