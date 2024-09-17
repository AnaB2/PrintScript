package org.example.lexer

import token.TokenType

class TokenMapper(private val version: String) {
    private val strategyMap: MutableMap<TokenType, TokenClassifierStrategy> = mutableMapOf()

    private val reservedKeywords = setOf("if", "else", "let", "const", "println", "true", "false") // Incluye true y false

    init {
        initializeStrategies()
    }

    // Inicializa las estrategias en el mapa según la versión
    private fun initializeStrategies() {
        when (version) {
            "1.0" -> initializeVersion10Strategies()
            "1.1" -> {
                initializeVersion10Strategies() // Primero inicializa la versión 1.0
                initializeVersion11Strategies() // Luego extiende con 1.1
            }
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
    }

    // Configura las estrategias para la versión 1.0
    private fun initializeVersion10Strategies() {
        strategyMap[TokenType.KEYWORD] = RegexTokenClassifier("""\blet\b""".toRegex())
        strategyMap[TokenType.FUNCTION] = RegexTokenClassifier("""\bprintln\b""".toRegex())
        strategyMap[TokenType.PARENTHESIS] = RegexTokenClassifier("""\(|\)""".toRegex())
        strategyMap[TokenType.DECLARATOR] = RegexTokenClassifier(""":""".toRegex())
        strategyMap[TokenType.ASSIGNATION] = RegexTokenClassifier("""=""".toRegex())
        strategyMap[TokenType.DATA_TYPE] = RegexTokenClassifier("""\bstring\b|\bnumber\b""".toRegex())
        strategyMap[TokenType.OPERATOR] = RegexTokenClassifier("""[\+\-\*/%=><!&|^~]+""".toRegex())
        strategyMap[TokenType.IDENTIFIER] = RegexTokenClassifier("""\b[a-zA-Z_][a-zA-Z0-9_]*\b""".toRegex())
        strategyMap[TokenType.STRINGLITERAL] = RegexTokenClassifier("\'[^\']*\'|\"[^\"]*\"".toRegex())
        strategyMap[TokenType.NUMBERLITERAL] = RegexTokenClassifier("[0-9]+(\\.[0-9]+)?".toRegex())
        strategyMap[TokenType.PUNCTUATOR] = RegexTokenClassifier("""[()\[\],;.]""".toRegex())
    }

    private fun initializeVersion11Strategies() {
        strategyMap[TokenType.CONDITIONAL] = RegexTokenClassifier("""\bif\b|\belse\b""".toRegex())
        strategyMap[TokenType.KEYWORD] = RegexTokenClassifier("""\blet\b|\bconst\b""".toRegex())
        strategyMap[TokenType.FUNCTION] = RegexTokenClassifier("println|readInput|readEnv".toRegex())
        strategyMap[TokenType.DATA_TYPE] = RegexTokenClassifier("(string|number|boolean)".toRegex())
        strategyMap[TokenType.BOOLEANLITERAL] = RegexTokenClassifier("(true|false)".toRegex())
        strategyMap[TokenType.PUNCTUATOR] = RegexTokenClassifier("""[{}()\[\],;.]""".toRegex())
    }

    fun classify(input: String): TokenType {
        if (version == "1.0" && input == "const") {
            throw IllegalArgumentException("Const declarations are not allowed in version 1.0")
        }
        if (input.isBlank()) {
            return TokenType.UNKNOWN
        }

        // Maneja las palabras reservadas antes de aplicar las estrategias de clasificación
        if (reservedKeywords.contains(input)) {
            return when (input) {
                "if", "else" -> TokenType.CONDITIONAL
                "let", "const" -> TokenType.KEYWORD
                "println" -> TokenType.FUNCTION
                "true", "false" -> TokenType.BOOLEANLITERAL
                else -> TokenType.UNKNOWN
            }
        }

        // Verifica las estrategias para otros tipos
        for ((type, strategy) in strategyMap) {
            if (strategy.classify(input)) {
                return type
            }
        }

        return TokenType.UNKNOWN
    }

    fun getStrategyMap(): Map<TokenType, TokenClassifierStrategy> {
        return strategyMap
    }
}
