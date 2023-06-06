package monkeylang

import monkeylang.ast.BooleanLiteral
import monkeylang.ast.CallExpression
import monkeylang.ast.Expression
import monkeylang.ast.ExpressionStatement
import monkeylang.ast.GroupedExpression
import monkeylang.ast.Identifier
import monkeylang.ast.InfixExpression
import monkeylang.ast.IntegerLiteral
import monkeylang.ast.LetStatement
import monkeylang.ast.PrefixExpression
import monkeylang.ast.Program
import monkeylang.ast.ReturnStatement
import monkeylang.ast.Statement
import monkeylang.ast.StringLiteral

class Parser(tokens: Iterator<Token>) {
    private val iterator = PeekingIterator(tokens)

    fun parseProgram(): Program {
        val statements = mutableListOf<Statement>()
        while (iterator.peek()?.type != TokenType.EndOfFile) {
            statements.add(parseStatement())
        }
        return Program(statements)
    }

    fun expectStatementTerminator() {
        if (iterator.peek()?.type != TokenType.Semicolon) {
            error("Expected semicolon")
        }
        iterator.next()
    }

    fun parseStatement(): Statement {
        val statement = when (iterator.peek()?.type) {
            TokenType.Let -> parseLetStatement()
            TokenType.Return -> parseReturnStatement()
            else -> parseExpressionStatement()
        }
        expectStatementTerminator()
        return statement
    }

    fun parseLetStatement(): LetStatement {
        val prefixToken = iterator.next()
        require(prefixToken.type == TokenType.Let)
        val identifier = parseIdentifier()
        val assignToken = iterator.next()
        val value = parseExpression()
        return LetStatement(prefixToken, assignToken, identifier, value)
    }

    fun parseReturnStatement(): ReturnStatement {
        val prefixToken = iterator.next()
        require(prefixToken.type == TokenType.Return)
        val value = parseExpression()
        return ReturnStatement(prefixToken, value)
    }

    fun parseExpressionStatement(): ExpressionStatement {
        val expression = parseExpression()
        return ExpressionStatement(expression)
    }

    fun parseIdentifier(): Identifier {
        val token = iterator.next()
        require(token.type == TokenType.Identifier)
        return Identifier(token)
    }

    fun parseExpression(): Expression {
        return parseInfixExpression()
    }

    fun parseInfixExpression(): Expression {
        var expression = parsePrefixExpression()
        while (true) {
            val operation = when (iterator.peek()?.type) {
                TokenType.Plus -> InfixOperation.Add
                TokenType.Minus -> InfixOperation.Subtract
                TokenType.Asterisk -> InfixOperation.Multiply
                TokenType.Slash -> InfixOperation.Divide
                TokenType.Equals -> InfixOperation.Equals
                TokenType.NotEquals -> InfixOperation.NotEquals
                TokenType.LessThan -> InfixOperation.LessThan
                TokenType.GreaterThan -> InfixOperation.GreaterThan
                else -> break
            }
            val operatorToken = iterator.next()
            val right = parsePrefixExpression()
            expression = InfixExpression(expression, operatorToken, right, operation)
        }
        return expression
    }

    fun parsePrefixExpression(): Expression {
        val operation = when (iterator.peek()?.type) {
            TokenType.Bang -> PrefixOperation.Invert
            TokenType.Minus -> PrefixOperation.Negate
            TokenType.Plus -> PrefixOperation.Plus
            else -> return parseCallExpression()
        }
        val operatorToken = iterator.next()
        val right = parsePrefixExpression()
        return PrefixExpression(operatorToken, right, operation)
    }

    fun parseCallExpression(): Expression {
        var expression = parsePrimaryExpression()
        while (true) {
            val token = iterator.peek()
            if (token?.type != TokenType.LeftParen) {
                break
            }
            val leftParen = iterator.next()
            val arguments = parseCallArguments()
            val rightParen = iterator.next()
            expression = CallExpression(expression, leftParen, arguments, rightParen)
        }
        return expression
    }

    fun parseCallArguments(): List<Expression> {
        val arguments = mutableListOf<Expression>()
        while (true) {
            val token = iterator.peek()
            if (token?.type == TokenType.RightParen) {
                break
            }
            arguments.add(parseExpression())
            if (iterator.peek()?.type != TokenType.Comma) {
                break
            }
            iterator.next()
        }
        return arguments
    }

    fun parsePrimaryExpression(): Expression {
        val token = iterator.peek()
        return when (token?.type) {
            TokenType.True, TokenType.False -> BooleanLiteral(iterator.next())
            TokenType.Integer -> IntegerLiteral(iterator.next())
            TokenType.StringLiteral -> StringLiteral(iterator.next())
            TokenType.Identifier -> parseIdentifier()
            TokenType.LeftParen -> parseGroupedExpression()
            else -> error("Expected expression")
        }
    }

    fun parseGroupedExpression(): GroupedExpression {
        val leftParen = iterator.next()
        val expression = parseExpression()
        val rightParen = iterator.next()
        return GroupedExpression(leftParen, expression, rightParen)
    }
}
