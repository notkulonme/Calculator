package hu.notkulonme

class Tokenizer {
    private lateinit var buffer: StringBuilder
    private lateinit var tokenList: ArrayList<Token>
    private var bufferCurrentType: TokenType? = null

    fun tokenize(text: String){
        buffer = StringBuilder()
        tokenList = ArrayList()
        bufferCurrentType = getType(text[0])

        for(char in text){
            val charType = getType(char)
            if(bufferCurrentType == null){
                bufferCurrentType = charType
            }
            if (charType == bufferCurrentType){
                buffer.append(char)
            }
            else{
                if(bufferCurrentType != TokenType.NOT_TOKEN){
                    tokenList.add(Token(value = buffer.toString(), type = bufferCurrentType!!))
                }
                buffer = StringBuilder()
                bufferCurrentType = null

            }
        }
    }

    fun getType(char: Char): TokenType{
        when(char){
            '+' -> return TokenType.PLUS
            '-' -> return TokenType.MINUS
            ' ' -> return TokenType.WHITESPACE
            '/' -> return TokenType.DIVISION
            '*' -> return TokenType.MULTIPLY
            else -> {
                return if (char.isDigit())
                    TokenType.NUMBER
                else if (char.code in 97..122)
                    TokenType.VARIABLE
                else
                    TokenType.NOT_TOKEN
            }
        }
    }

}