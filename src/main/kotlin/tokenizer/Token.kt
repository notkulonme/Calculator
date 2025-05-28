package hu.notkulonme.tokenizer

data class Token(val value: String, val type: TokenType){
    fun isAdditive(): Boolean{
        return this.value == "+" || this.value == "-"
    }

    fun isMultiplicative(): Boolean {
        return this.value == "*" || this.value == "/" || this.value == "÷"
    }
}