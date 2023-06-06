package monkeylang

class PeekingIterator<T>(private val iterator: Iterator<T>) : Iterator<T> {
    private val peekStack = ArrayDeque<T>()

    override fun next(): T {
        if (peekStack.isNotEmpty()) {
            return peekStack.removeFirst()
        }
        return iterator.next()
    }

    override fun hasNext(): Boolean {
        return peekStack.isNotEmpty() || iterator.hasNext()
    }

    fun peek(amount: Int = 1): T? {
        while (peekStack.size < amount && iterator.hasNext()) {
            peekStack.addLast(iterator.next())
        }
        return peekStack[amount - 1]
    }
}
