package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.Token

data class CallExpression(
    val expression: Expression,
    val leftParenToken: Token,
    val arguments: List<Expression>,
    val rightParenToken: Token,
) : Expression {
    override val range: DocumentRange = expression.range..rightParenToken.range
    override val structure: StructuralExpression by lazy {
        Structure(expression.structure, arguments.map { it.structure })
    }

    data class Structure(
        val expression: StructuralExpression,
        val arguments: List<StructuralExpression>,
    ) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            expression.isEquivalentTo(other.expression) &&
            arguments.size == other.arguments.size &&
            arguments.zip(other.arguments).all { (a, b) -> a.isEquivalentTo(b) }
    }
}
