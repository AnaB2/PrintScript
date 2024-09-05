package ast

import token.Token
import token.TokenPosition
import token.TokenType

sealed class ASTNode {
    abstract val position: TokenPosition
}

data class LiteralNode(
    val value: String,
    val type: TokenType,
    override val position: TokenPosition,
) : ASTNode()

data class BinaryNode(
    val left: ASTNode,
    val right: ASTNode,
    val operator: Token,
    override val position: TokenPosition,
) : ASTNode()

data class PrintNode(
    val expression: ASTNode,
    override val position: TokenPosition,
) : ASTNode()

data class DeclarationNode(
    val declType: TokenType,
    val id: String,
    val valType: TokenType,
    val expr: ASTNode,
    override val position: TokenPosition,
) : ASTNode()

data class AssignationNode(
    val id: String,
    val expression: ASTNode,
    val valType: TokenType,
    override val position: TokenPosition,
) : ASTNode()

data class BlockNode(
    val nodes: List<ASTNode>,
    override val position: TokenPosition,
) : ASTNode()

data class ConditionalNode(
    val condition: LiteralNode,
    val thenBlock: ASTNode,
    val elseBlock: ASTNode,
    override val position: TokenPosition,
) : ASTNode()

data class FunctionNode(
    val function: TokenType,
    val expression: ASTNode,
    override val position: TokenPosition,
) : ASTNode()

data object NilNode : ASTNode() {
    override val position: TokenPosition
        get() = TokenPosition(0, 0)
}
