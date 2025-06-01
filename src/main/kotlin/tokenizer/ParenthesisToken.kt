package hu.notkulonme.tokenizer

data class ParenthesisToken(val tokenList: ArrayList<TokenInterface>): TokenInterface{
    override fun getValueList(): List<TokenInterface> {
        return tokenList
    }
}
