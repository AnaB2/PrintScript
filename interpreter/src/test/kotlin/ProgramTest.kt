import org.example.Lexer
import org.example.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ProgramTest {

    @Test
    fun `test lexer, parser, and interpreter`() {
        val input = """
        let x:number = 42;
        let y:number = 10;
        println(x + y);
    """.trimIndent()

        // Step 1: Lexer
        val tokenMapper = TokenMapper("1.0")
        val lexer = Lexer(tokenMapper)
        val tokens = lexer.execute(input)

        // Step 2: Parser
        val parser = Parser()
        val astNodes = parser.execute(tokens.filterNotNull())  // Filter out null tokens if any

        // Step 3: Interpreter
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Evaluate each AST node
        for (node in astNodes) {
            interpreter.evaluate(node)
        }

        // Check the output
        assertEquals("52", outputStream.toString().trim())
    }
}
