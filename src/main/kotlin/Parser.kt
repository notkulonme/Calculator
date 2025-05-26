package hu.notkulonme

import InputError
import Tree
import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType

class Parser {
    @Throws(InputError::class)
    fun parseIntoTree(tokenList: List<Token>):Tree{
        if (tokenList.count{ it.type == TokenType.INVALID_NUMBER } > 0)
            throw InputError(tokenList.stream().filter { it.type == TokenType.INVALID_NUMBER }.map { it.value }.toList())
        return Tree(null, null)
    }


}