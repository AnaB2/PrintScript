import ast.AssignationNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode
import ast.DeclarationNode
import ast.FunctionNode
import ast.LiteralNode
import ast.PrintNode
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import token.Token
import token.TokenPosition
import token.TokenType
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InterpreterTests {
    private val position = TokenPosition(1, 1)

    @Test
    fun `test number literal evaluation`() {
        val node = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals(42, result)
    }

    @Test
    fun `test string literal evaluation`() {
        val node = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals("Hello, world!", result)
    }

    @Test
    fun `test boolean literal evaluation`() {
        val trueNode = LiteralNode("true", TokenType.BOOLEAN, position)
        val falseNode = LiteralNode("false", TokenType.BOOLEAN, position)
        val interpreter = Interpreter()
        assertEquals(true, interpreter.evaluate(trueNode))
        assertEquals(false, interpreter.evaluate(falseNode))
    }

    @Test
    fun `test addition of integers`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals(15, result)
    }

    @Test
    fun `test string concatenation`() {
        val left = LiteralNode("Hello", TokenType.STRINGLITERAL, position)
        val right = LiteralNode("World", TokenType.STRINGLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals("HelloWorld", result)
    }

    @Test
    fun `test subtraction of integers`() {
        val left = LiteralNode("15", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "-", position, position) // Create a token for "-"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals(10, result)
    }

    @Test
    fun `test multiplication of integers`() {
        val left = LiteralNode("3", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("4", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "*", position, position) // Create a token for "*"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals(12, result)
    }

    @Test
    fun `test division of integers`() {
        val left = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("4", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "/", position, position) // Create a token for "/"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.evaluate(node)
        assertEquals(5, result)
    }

    @Test
    fun `test division by zero`() {
        val left = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("0", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "/", position, position) // Create a token for "/"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        assertThrows(RuntimeException::class.java) {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test variable assignment`() {
        val expression = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val node = AssignationNode("x", expression, TokenType.ASSIGNATION, position)
        val interpreter = Interpreter()
        interpreter.evaluate(node)
        assertEquals(42, interpreter.variables["x"])
    }

    @Test
    fun `test print node`() {
        val expression = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val node = PrintNode(expression, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.evaluate(node)
        assertEquals("Hello, world!", outputStream.toString().trim())
    }

    @Test
    fun `test block execution`() {
        val expr1 = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val expr2 = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val assignment = AssignationNode("y", expr2, TokenType.ASSIGNATION, position)
        val print = PrintNode(expr1, position)
        val block = BlockNode(listOf(assignment, print), position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.evaluate(block)
        assertEquals("10", outputStream.toString().trim())
    }

    @Test
    fun `test conditional node true`() {
        val condition = LiteralNode("true", TokenType.BOOLEAN, position)
        val thenBlock = PrintNode(LiteralNode("Condition is true", TokenType.STRINGLITERAL, position), position)
        val elseBlock = PrintNode(LiteralNode("Condition is false", TokenType.STRINGLITERAL, position), position)
        val node = ConditionalNode(condition, thenBlock, elseBlock, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.evaluate(node)
        assertEquals("Condition is true", outputStream.toString().trim())
    }

    @Test
    fun `test conditional node false`() {
        val condition = LiteralNode("false", TokenType.BOOLEAN, position)
        val thenBlock = PrintNode(LiteralNode("Condition is true", TokenType.STRINGLITERAL, position), position)
        val elseBlock = PrintNode(LiteralNode("Condition is false", TokenType.STRINGLITERAL, position), position)
        val node = ConditionalNode(condition, thenBlock, elseBlock, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.evaluate(node)
        assertEquals("Condition is false", outputStream.toString().trim())
    }

    @Test
    fun `test undefined variable exception`() {
        val node = LiteralNode("undefinedVariable", TokenType.IDENTIFIER, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test greater than operator`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, ">", position, position) // Create a token for ">"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.evaluate(node)

        // Assuming the result is a Boolean, we expect "10 > 5" to be true
        assertEquals(true, result)
    }

    @Test
    fun `test unsupported operator exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "^", position, position) // Use an unsupported operator
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test less than operator`() {
        val left = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "<", position, position) // Create a token for "<"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.evaluate(node)

        // Assuming the result is a Boolean, we expect "5 < 10" to be true
        assertEquals(true, result)
    }

    @Test
    fun `test supported operator exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Use a supported operator
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertDoesNotThrow {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test greater than exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("true", TokenType.BOOLEAN, position)
        val operatorToken = Token(TokenType.OPERATOR, ">", position, position) // Create a token for ">"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test less than exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("true", TokenType.BOOLEAN, position)
        val operatorToken = Token(TokenType.OPERATOR, "<", position, position) // Create a token for "<"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.evaluate(node)
        }
    }

    @Test
    fun `test function node`() {
        val expression = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val node = FunctionNode(TokenType.FUNCTION, expression, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.evaluate(node)
        assertEquals("Hello, world!", outputStream.toString().trim())
    }

    @Test
    fun `test declaration node`() {
        val expression = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val node = DeclarationNode(TokenType.KEYWORD, "x", TokenType.DATA_TYPE, expression, position)
        val interpreter = Interpreter()
        interpreter.evaluate(node)
        assertEquals(42, interpreter.variables["x"])
    }
}
