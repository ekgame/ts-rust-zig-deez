package monkeylang

import monkeylang.ast.BooleanLiteral
import monkeylang.ast.Identifier
import monkeylang.ast.IntegerLiteral
import monkeylang.ast.LetStatement
import monkeylang.ast.Program
import monkeylang.ast.StringLiteral
import kotlin.test.Test
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun testLetStatement() {
        val code = """
            let x = 5;
            let y = "hello";
            let z = true;
        """.trimIndent()

        assertExpectedAst(code) {
            Program.Structure(
                LetStatement.Structure(
                    Identifier.Structure("x"),
                    IntegerLiteral.Structure(5),
                ),
                LetStatement.Structure(
                    Identifier.Structure("y"),
                    StringLiteral.Structure("hello"),
                ),
                LetStatement.Structure(
                    Identifier.Structure("z"),
                    BooleanLiteral.Structure(true),
                ),
            )
        }
    }

    fun assertExpectedAst(code: String, expectedBuilder: () -> Program.Structure) {
        val parsedProgram = Parser(Lexer(code)).parseProgram()
        val expectedStructure = expectedBuilder()
        assertTrue(parsedProgram.isEquivalentTo(expectedStructure))
    }
}
