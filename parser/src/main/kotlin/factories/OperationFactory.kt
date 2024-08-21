package factories

import ast.AstNode
import ast.BinaryNode
import ast.LiteralNode
import token.Token
import token.TokenType

class OperationFactory {
    fun createAST(tokens: List<Token>): AstNode {
        if (tokens.isEmpty()) {
            throw IllegalArgumentException("Cannot create AST from an empty token list")
        }

        if (tokens.size == 1) {
            return createLiteralNode(tokens[0])
        }

        for (token in tokens) {
            if (isAdditionOrSubtraction(token)) {
                return BinaryNode(
                    left = createAST(tokens.subList(0, tokens.indexOf(token))),
                    right = createAST(tokens.subList(tokens.indexOf(token) + 1, tokens.size)),
                    operator = token.getType(),
                    position = token.getPosition()
                )
            }
        }

        for (token in tokens) {
            if (isMultiplicationOrDivision(token)) {
                return BinaryNode(
                    left = createAST(tokens.subList(0, tokens.indexOf(token))),
                    right = createAST(tokens.subList(tokens.indexOf(token) + 1, tokens.size)),
                    operator = token.getType(),
                    position = token.getPosition()
                )
            }
        }

        throw Exception("Error in operation")
    }

    private fun createLiteralNode(token: Token): AstNode {
        return LiteralNode(value = token.getValue(), type = token.getType(), position = token.getPosition())
    }

    private fun isMultiplicationOrDivision(token: Token) = token.getValue() == "*" || token.getValue() == "/"

    private fun isAdditionOrSubtraction(token: Token) = token.getValue() == "+" || token.getValue() == "-"
}
