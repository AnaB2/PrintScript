package factories

import ast.ASTNode
import ast.PrintNode
import token.Token
import token.TokenType

class PrintlnFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        if (tokens.size < 4) {
            throw IllegalArgumentException("Invalid token structure for println: Too few tokens")
        }

        val openParenIndex = tokens.indexOfFirst { it.getType() == TokenType.PARENTHESIS && it.value == "(" }
        val closeParenIndex = tokens.indexOfLast { it.getType() == TokenType.PARENTHESIS && it.value == ")" }

        if (openParenIndex == -1 || closeParenIndex == -1 || openParenIndex >= closeParenIndex) {
            throw IllegalArgumentException("Invalid token structure for println: Missing or misordered parentheses")
        }

        val expressionTokens = tokens.subList(openParenIndex + 1, closeParenIndex)

        if (expressionTokens.isEmpty()) {
            throw IllegalArgumentException("Cannot create AST for println from an empty expression")
        }

        val expressionNode = OperationFactory().createAST(expressionTokens)
        return PrintNode(expressionNode, tokens[0].getPosition())
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.isNotEmpty() && tokens[0].getType() == TokenType.FUNCTION && tokens[0].value == "println"
    }
}
