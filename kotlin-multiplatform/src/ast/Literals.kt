package monkeylang.ast

import monkeylang.DocumentRange
import monkeylang.Token
import monkeylang.TokenType

data class BooleanLiteral(val token: Token) : Expression {
    override val range: DocumentRange = token.range
    override val structure by lazy { Structure(token.type == TokenType.True) }

    class Structure(val value: Boolean) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            value == other.value
    }
}

data class StringLiteral(val token: Token) : Expression {
    override val range: DocumentRange = token.range
    override val structure by lazy { Structure(token.literal.removeSurrounding("\"")) }

    class Structure(val value: String) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            value == other.value
    }
}

data class IntegerLiteral(val token: Token) : Expression {
    override val range: DocumentRange = token.range
    override val structure by lazy { Structure(token.literal.toInt()) }

    class Structure(val value: Int) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            value == other.value
    }
}

data class Identifier(val token: Token) : Expression {
    override val range: DocumentRange = token.range
    override val structure by lazy { Structure(token.literal) }

    class Structure(val value: String) : StructuralExpression {
        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            value == other.value
    }
}
