package monkeylang.ast

import monkeylang.DocumentRange

data class ExpressionStatement(
    val expression: Expression,
) : Statement {
    override val range: DocumentRange = expression.range
    override val structure by lazy { Structure(expression.structure) }

    data class Structure(val expression: StructuralExpression) : StructuralStatement {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            expression.isEquivalentTo(other.expression)
    }
}
