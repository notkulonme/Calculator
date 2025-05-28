package hu.notkulonme

import InputError
import Tree
import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType

class Parser {
    lateinit var tokenList: ArrayList<Token>


    fun parseIntoTree(tokenList: ArrayList<Token>):Tree{
        this.tokenList = tokenList
        validateTokenList()
        return Tree(null, null)
    }

    fun validateTokenList(){
        if (tokenList.count{ it.type == TokenType.INVALID_NUMBER } > 0)
            throw InputError(tokenList.stream().filter { it.type == TokenType.INVALID_NUMBER }.map { it.value }.toList())
        if (tokenList.count{it.type == TokenType.NOT_TOKEN } > 0)
            throw InputError(TokenType.NOT_TOKEN)


    }

    private fun validateOperators(operatorIndexList: ArrayList<Int>): Boolean{
        var operatorIndexes = operatorIndexList
        var i = 1
        while (i < operatorIndexes.size){
            val previous = operatorIndexes[i-1]
            val current = operatorIndexes[i]
            if (previous+1 == current){
                val prevRef = tokenList[previous]
                val curRef = tokenList[current]
                if (prevRef.isAdditive() && curRef.isAdditive()){
                    tokenList[current] = Token (addOperators(prevRef.value, curRef.value), TokenType.OPERATOR)
                    tokenList.removeAt(previous)
                    operatorIndexes = getOperatorIndexList()
                    i--
                }
            }

            i++
        }
        return false
    }

    private fun getOperatorIndexList(): ArrayList<Int> {
        val operatorIndexList = ArrayList<Int>()
        for ((index, token) in tokenList.withIndex()){
            if (token.type == TokenType.OPERATOR)
                operatorIndexList.add(index)
        }
        return operatorIndexList
    }

    fun addOperators(prev: String, cur: String): String {
        val prevNum = "${prev}1".toInt()
        val curNum = "${cur}1".toInt()
        println(prevNum+curNum)
        if(prevNum + curNum != 0)
            return "+"
        return "-"
    }

}
