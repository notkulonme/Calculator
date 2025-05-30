package hu.notkulonme.tokenizer

import hu.notkulonme.tokenizer.TokenType
import kotlin.text.iterator

class Tokenizer {
    private lateinit var buffer: StringBuilder
    private lateinit var tokenList: ArrayList<Token>
    private lateinit var bufferCurrentType: TokenType

    fun tokenize(text: String): ArrayList<Token> {
        initTokenizer(text)

        for (char in text) {

            val charType = getType(char)

            if (charType == bufferCurrentType && bufferCurrentType != TokenType.OPERATOR) {
                buffer.append(char)
            } else {
                addValidBufferToList()
                buffer = StringBuilder()
                buffer.append(char)
                bufferCurrentType = charType

            }

        }
        addValidBufferToList()
        return tokenList
    }

    fun getType(char: Char): TokenType =
        when (char) {
            '+' -> TokenType.OPERATOR
            '-' -> TokenType.OPERATOR
            ' ' -> TokenType.WHITESPACE
            '/' -> TokenType.OPERATOR
            '*' -> TokenType.OPERATOR
            '.' -> TokenType.NUMBER
            else -> {
                if (char.isDigit())
                    TokenType.NUMBER
                else if (char.code in 97..122)
                    TokenType.VARIABLE
                else
                    TokenType.NOT_TOKEN
            }
        }


    fun initTokenizer(text: String) {
        buffer = StringBuilder()
        tokenList = ArrayList()
        bufferCurrentType = getType(text[0])
    }

    fun addValidBufferToList() {
        if (isInvalidNumber())
            tokenList.add(Token(value = buffer.toString(), type = TokenType.INVALID_NUMBER))
        else if (!buffer.isEmpty())
            tokenList.add(Token(value = buffer.toString(), type = bufferCurrentType))
    }

    fun isInvalidNumber(): Boolean {
        if (bufferCurrentType != TokenType.NUMBER) {
            return false
        } else {
            val commaCount = buffer.count { it == '.' }
            if (buffer[0] == '.' || buffer[buffer.length - 1] == '.' || commaCount > 1)
                return true
            else
                return false
        }
        return true
    }
}