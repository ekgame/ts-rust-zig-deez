package monkeylang.ast

import monkeylang.DocumentRange

sealed interface AstNode {
    val structure: StructuralNode
    val range: DocumentRange
    fun isEquivalentTo(other: AstNode): Boolean = structure.isEquivalentTo(other.structure)
    fun isEquivalentTo(other: StructuralNode): Boolean = structure.isEquivalentTo(other)
}

sealed interface StructuralNode {
    fun isEquivalentTo(other: StructuralNode): Boolean
}

sealed interface Statement : AstNode {
    override val structure: StructuralStatement
}

sealed interface StructuralStatement : StructuralNode

sealed interface Expression : AstNode {
    override val structure: StructuralExpression
}

sealed interface StructuralExpression : StructuralNode
