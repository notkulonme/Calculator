import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType
import hu.notkulonme.tokenizer.Tokenizer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TokenizerTest {
    val tokenizer = Tokenizer()

    @Test
    fun testParenthesis(){
        val result = tokenizer.tokenize("((3)/)")
        val expected = mutableListOf<Token>(Token("(", TokenType.PARENTHESIS),Token("(", TokenType.PARENTHESIS),Token("3", TokenType.NUMBER),Token(")", TokenType.PARENTHESIS),Token("/", TokenType.OPERATOR),Token(")", TokenType.PARENTHESIS))
        assertIterableEquals(expected, result)
    }

    @Test
    fun  testNumbers(){
        val result = tokenizer.tokenize("32.5  32..5" )
        val expected = mutableListOf<Token>(Token("32.5", TokenType.NUMBER), Token("  ", TokenType.WHITESPACE),Token("32..5", TokenType.INVALID_NUMBER))
        assertIterableEquals(expected, result)
    }

    @Test
    fun  testOperators(){
        val result = tokenizer.tokenize("+-/+3*" )
        val expected = mutableListOf<Token>(Token("+", TokenType.OPERATOR), Token("-", TokenType.OPERATOR),Token("/", TokenType.OPERATOR),Token("+", TokenType.OPERATOR),Token("3", TokenType.NUMBER),Token("*", TokenType.OPERATOR))
        assertIterableEquals(expected, result)
    }
}