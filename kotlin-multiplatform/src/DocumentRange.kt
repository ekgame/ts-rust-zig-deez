package monkeylang

import monkeylang.ast.AstNode

data class DocumentPosition(val line: Int, val column: Int) : Comparable<DocumentPosition> {
    init {
        require(line >= 1) { "DocumentPosition line must be >= 1" }
        require(column >= 0) { "DocumentPosition column must be >= 0" }
    }

    infix operator fun rangeTo(other: DocumentPosition): DocumentRange {
        return DocumentRange(this, other)
    }

    override fun toString(): String = "$line:$column"

    override fun compareTo(other: DocumentPosition): Int {
        return when {
            line < other.line -> -1
            line > other.line -> 1
            column < other.column -> -1
            column > other.column -> 1
            else -> 0
        }
    }
}

data class DocumentRange(val start: DocumentPosition, val end: DocumentPosition) {
    init {
        require(start <= end) { "DocumentRange start can not be after the end" }
    }

    infix operator fun rangeTo(other: DocumentRange): DocumentRange {
        return this.start..other.end
    }

    override fun toString(): String = "$start-$end"

    companion object {
        val EMPTY = fromPositions(1, 0, 1, 0)

        fun fromPositions(
            startLine: Int,
            startColumn: Int,
            endLine: Int,
            endColumn: Int,
        ): DocumentRange {
            return DocumentPosition(startLine, startColumn)..DocumentPosition(endLine, endColumn)
        }

        fun fromNodeList(nodes: List<AstNode>): DocumentRange {
            val first = nodes.firstOrNull()?.range ?: return EMPTY
            val last = nodes.lastOrNull()?.range ?: return EMPTY
            return first..last
        }
    }
}
