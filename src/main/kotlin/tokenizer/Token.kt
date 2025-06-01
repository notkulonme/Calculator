package hu.notkulonme.tokenizer

data class Token(val value: String, val type: TokenType) {
    fun isAdditive(): Boolean {
        return this.value == "+" || this.value == "-"
    }

    fun isMultiplicative(): Boolean {
        return this.value == "*" || this.value == "/" || this.value == "÷"
    }

    fun getOperation(): (Double, Double) -> Double {
        return when (value) {
            "*" -> { left, right -> left * right }
            "/" -> { left, right -> left / right }
            "+" -> { left, right -> left + right }
            "-" -> { left, right -> left - right }
            else -> { _, _ -> 0.0 }
        }
    }

    fun toDouble(): Double {
        return value.toDouble()
    }

    fun toNumber(): Number {
        return if (value.toDouble() % 1.0 == 0.0)
            value.toDouble().toInt()
        else
            value.toDouble()
    }
}