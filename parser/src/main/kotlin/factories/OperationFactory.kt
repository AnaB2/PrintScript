package factories

import ast.ASTNode
import ast.BinaryNode
import ast.LiteralNode
import token.Token

class OperationFactory {
    fun createAST(tokens: List<Token>): ASTNode {
        if (tokens.size == 1) {
            return createLiteralNode(tokens[0])
        }
        if (tokens.first().value == "(" && tokens.last().value == ")") {
            return createAST(tokens.subList(1, tokens.size - 1))
        }
        for (token in tokens) {
            if (isMultiplicationOrDivision(token)) {
                return BinaryNode(
                    left = createAST(tokens.subList(0, tokens.indexOf(token))),
                    right = createAST(tokens.subList(tokens.indexOf(token) + 1, tokens.size)),
                    operator = token,
                    position = token.getPosition(),
                )
            }
        }
        for (token in tokens) {
            if (isAdditionOrSubtraction(token)) {
                return BinaryNode(
                    left = createAST(tokens.subList(0, tokens.indexOf(token))),
                    right = createAST(tokens.subList(tokens.indexOf(token) + 1, tokens.size)),
                    operator = token,
                    position = token.getPosition(),
                )
            }
        }
        throw Exception("Error in operation")
    }

    private fun createLiteralNode(token: Token): ASTNode {
        return LiteralNode(value = token.value, type = token.getType(), position = token.getPosition())
    }

    private fun isMultiplicationOrDivision(token: Token) = token.value == "*" || token.value == "/"

    private fun isAdditionOrSubtraction(token: Token) = token.value == "+" || token.value == "-"
}
