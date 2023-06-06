package monkeylang.ast

import monkeylang.DocumentRange

data class Program(val statements: List<Statement>) : AstNode {
    override val range: DocumentRange = DocumentRange.fromNodeList(statements)
    override val structure by lazy { Structure(statements.map { it.structure }) }

    data class Structure(val statements: List<StructuralStatement>) : StructuralNode {
        constructor(vararg statements: StructuralStatement) : this(statements.toList())

        override fun isEquivalentTo(other: StructuralNode): Boolean = other is Structure &&
            statements.size == other.statements.size &&
            statements.zip(other.statements).all { (a, b) -> a.isEquivalentTo(b) }
    }
}
