package hu.notkulonme

class Tokenizer {
    private lateinit var buffer: StringBuilder
    private lateinit var tokenList: ArrayList<Token>
    private lateinit var bufferCurrentType: TokenType

    fun tokenize(text: String): ArrayList<Token>{
        initTokenizer(text)

        for(char in text){

            val charType = getType(char)

            if (charType == bufferCurrentType){
                buffer.append(char)
            }
            else{
                addValidBufferToList()
                buffer = StringBuilder()
                buffer.append(char)
                bufferCurrentType = charType

            }

        }
        addValidBufferToList()
        return tokenList
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

    fun initTokenizer(text: String){
        buffer = StringBuilder()
        tokenList = ArrayList()
        bufferCurrentType = getType(text[0])
    }

    fun addValidBufferToList(){
        if(bufferCurrentType != TokenType.NOT_TOKEN){
            tokenList.add(Token(value = buffer.toString(), type = bufferCurrentType))
        }
    }

}