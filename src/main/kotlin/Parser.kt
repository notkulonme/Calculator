package hu.notkulonme

import InputError
import Tree
import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType

class Parser {
    lateinit var tokenList: List<Token>
    fun parseIntoTree(tokenList: List<Token>):Tree{
        this.tokenList = tokenList
        validateTokenList()
        return Tree(null, null)
    }

    fun validateTokenList(){
        if (tokenList.count{ it.type == TokenType.INVALID_NUMBER } > 0)
            throw InputError(tokenList.stream().filter { it.type == TokenType.INVALID_NUMBER }.map { it.value }.toList())
        if (tokenList.count{it.type == TokenType.NOT_TOKEN } > 0)
            throw InputError(TokenType.NOT_TOKEN)
        val operatorIndexList = mutableListOf<Int>()
        for ((index, token) in tokenList.withIndex()){
            if (token.type == TokenType.OPERATOR)
                operatorIndexList.add(index)
        }

    }
}
