package formatOperations

import Formatter
import ast.ASTNode
import ast.LiteralNode
import token.TokenType

class FormatLiteral : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is LiteralNode
    }

    override fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String {
        if (!canHandle(node)) error("Node isn't a LiteralNode")
        val literalNode = node as LiteralNode
        return if (literalNode.type == TokenType.STRINGLITERAL) "\"${literalNode.value}\"" else literalNode.value.toString()
    }
}
