package token

class Token(
    private var type: TokenType,
    var value: String,
    private var initialPosition: TokenPosition,
    private var finalPosition: TokenPosition,
) {
    fun getType(): TokenType {
        return type
    }

    fun getValue(): String {
        return value
    }

    fun getPosition(): TokenPosition {
        return initialPosition
    }

    fun getFinalPosition(): TokenPosition {
        return finalPosition
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Token
        return type == other.type &&
                value == other.value &&
                initialPosition == other.initialPosition &&
                finalPosition == other.finalPosition
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + initialPosition.hashCode()
        result = 31 * result + finalPosition.hashCode()
        return result
    }

    override fun toString():String{
        return "Token(type = '$type', value = '$value', start = '$initialPosition', end = '$finalPosition')"

    }
}