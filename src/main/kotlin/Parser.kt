package hu.notkulonme

import InputError
import OperatorError
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
        tokenList = ArrayList(tokenList.filter { it.type != TokenType.WHITESPACE })
        if (tokenList.count{ it.type == TokenType.INVALID_NUMBER } > 0)
            throw InputError(tokenList.stream().filter { it.type == TokenType.INVALID_NUMBER }.map { it.value }.toList())
        if (tokenList.count{it.type == TokenType.NOT_TOKEN } > 0)
            throw InputError(TokenType.NOT_TOKEN)

        validateOperators(getOperatorIndexList())

        if (tokenList[0].type == TokenType.OPERATOR) {
            if (tokenList[0].isMultiplicative())
                throw OperatorError("invalid operator in beginning ${tokenList[0].value}")
            else {
                val newToken = Token(addOperatorToNumber(1), TokenType.NUMBER)
                tokenList[1] = newToken
                tokenList.removeAt(0)
            }
        }
        if (tokenList[tokenList.size-1].type == TokenType.OPERATOR)
            throw OperatorError("Operator can't be at the end.")

        var i = 1
        while (i < tokenList.size){
            val prev = tokenList[i-1]
            val current = tokenList[i]
            if (prev.isMultiplicative() && current.isAdditive()){
                val newToken = Token(addOperatorToNumber(i+1), TokenType.NUMBER)
                tokenList[i+1] = newToken
                tokenList.removeAt(i)
                i--
            }
            i++
        }
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
                    val test =  Token (addOperators(prevRef.value, curRef.value), TokenType.OPERATOR)
                    tokenList[current] = test
                    tokenList.removeAt(previous)
                    operatorIndexes = getOperatorIndexList()
                    i--
                }
                else if (prevRef.isAdditive() && curRef.isMultiplicative())
                    throw OperatorError("${prevRef.value} can't be followed by ${curRef.value}")
                else if(prevRef.isMultiplicative() && curRef.isMultiplicative())
                    throw OperatorError("${prevRef.value} can't be followed by ${curRef.value}")

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

    fun addOperators(previousOperator: String, currentOperator: String): String {
        val prevNum = "${previousOperator}1".toInt()
        val curNum = "${currentOperator}1".toInt()
        if(prevNum + curNum != 0)
            return "+"
        return "-"
    }

    fun addOperatorToNumber(numberIndex:Int): String {
        val operatorIndex = numberIndex-1
        return tokenList[operatorIndex].value + tokenList[numberIndex].value
    }

}