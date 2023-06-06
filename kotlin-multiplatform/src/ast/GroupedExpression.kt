package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.Token

data class GroupedExpression(
    val leftParen: Token,
    val expression: Expression,
    val rightParen: Token,
) : Expression {
    override val range: DocumentRange = leftParen.range..rightParen.range
    override val structure by lazy { Structure(expression.structure) }

    data class Structure(val expression: StructuralExpression) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            expression.isEquivalentTo(other.expression)
    }
}
