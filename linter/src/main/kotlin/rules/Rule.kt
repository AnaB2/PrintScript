package rules

import BrokenRule
import token.Token

interface Rule {
    fun applyRule(tokens: List<List<Token>>): List<BrokenRule>

    fun getRuleName(): String

    fun getRuleDescription(): String
}
