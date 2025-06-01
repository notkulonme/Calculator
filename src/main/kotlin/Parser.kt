package hu.notkulonme

import InputError
import OperatorError
import Tree
import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType
import java.util.EmptyStackException
import java.util.LinkedList

class Parser {
    var tokenList: ArrayList<Token>

    constructor(tokenList: ArrayList<Token>) {
        this.tokenList = tokenList
        validateTokenList()
    }

    fun parseIntoTree(): Tree {
        val rootIndex = nextOperatorIndex(tokenList)
        val root = Tree(null,tokenList[rootIndex])
        root.builderLeftChild = ArrayList(tokenList.subList(0, rootIndex))
        root.builderRightChild = ArrayList(tokenList.subList(rootIndex+1, tokenList.size))

        while (root.leafLevelHasBuilder()){
            val leafLevel = root.leafLevel
            for (leafNode in leafLevel){
                if (leafNode.builderLeftChild != null) {
                    if (leafNode.builderLeftChild.size == 1 && leafNode.builderLeftChild[0].type == TokenType.NUMBER)
                        leafNode.leftChild = Tree(leafNode, leafNode.builderLeftChild[0])


                    if (leafNode.builderLeftChild.size != 1) {
                        val nextLeftOperator = nextOperatorIndex(leafNode.builderLeftChild)
                        val leftChild = Tree(leafNode, leafNode.builderLeftChild[nextLeftOperator])
                        leftChild.builderLeftChild = ArrayList(leafNode.builderLeftChild.subList(0, nextLeftOperator))
                        leftChild.builderRightChild = ArrayList(
                            leafNode.builderLeftChild.subList(
                                nextLeftOperator + 1,
                                leafNode.builderLeftChild.size
                            )
                        )
                        leafNode.leftChild = leftChild
                    }
                }
                if (leafNode.builderRightChild != null) {
                    if (leafNode.builderRightChild.size == 1 && leafNode.builderRightChild[0].type == TokenType.NUMBER)
                        leafNode.rightChild = Tree(leafNode, leafNode.builderRightChild[0])

                    if (leafNode.builderRightChild.size != 1) {
                        val nextRightOperator = nextOperatorIndex(leafNode.builderRightChild)
                        val rightChild = Tree(leafNode, leafNode.builderRightChild[nextRightOperator])
                        rightChild.builderLeftChild =
                            ArrayList(leafNode.builderRightChild.subList(0, nextRightOperator))
                        rightChild.builderRightChild = ArrayList(
                            leafNode.builderRightChild.subList(
                                nextRightOperator + 1,
                                leafNode.builderRightChild.size
                            )
                        )
                        leafNode.rightChild = rightChild
                    }
                }
            }
        }
        return root
    }

    fun nextOperatorIndex(builderList: ArrayList<Token>): Int {
        if (builderList.count{ it.type == TokenType.PARENTHESIS }  == 0) {

            var isSearched: (Token) -> Boolean
            if (builderList.any { it.isAdditive() })
                isSearched = { it.isAdditive() }
            else if (builderList.any { it.isMultiplicative() })
                isSearched = { it.isMultiplicative() }
            else
                return -1

            for (i in builderList.size - 1 downTo 0) {
                if (isSearched(builderList[i]))
                    return i
            }
            return -1
        }else{
            if (builderList.first().type == TokenType.PARENTHESIS && builderList.last().type == TokenType.PARENTHESIS && builderList.count { it.type == TokenType.PARENTHESIS } == 2){
                builderList.removeFirst()
                builderList.removeLast()
                return nextOperatorIndex(builderList)
            }else{
                val builderBuffer = ArrayList(builderList)

                for ((index, token) in builderBuffer.withIndex()){
                    if (token.value == ")")
                        nullInsideParenthesis(builderBuffer, index)
                }
                return nextOperatorIndex(builderBuffer)
            }

        }

    }

    fun nullInsideParenthesis(buffer: ArrayList<Token>, index: Int){
        var i = index
        while (buffer[i].value != "("){
            buffer[i] = Token("", TokenType.NOT_TOKEN)
            i--
        }
        buffer[i] = Token("", TokenType.NOT_TOKEN)

    }

    fun validateTokenList() {
        tokenList = ArrayList(tokenList.filter { it.type != TokenType.WHITESPACE })
        if (tokenList.count { it.type == TokenType.INVALID_NUMBER } > 0)
            throw InputError(tokenList.stream().filter { it.type == TokenType.INVALID_NUMBER }.map { it.value }
                .toList())
        if (tokenList.count { it.type == TokenType.NOT_TOKEN } > 0)
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
        if (tokenList[tokenList.size - 1].type == TokenType.OPERATOR)
            throw OperatorError("Operator can't be at the end.")

        var i = 1
        while (i < tokenList.size) {
            val prev = tokenList[i - 1]
            val current = tokenList[i]
            if (prev.isMultiplicative() && current.isAdditive()) {
                val newToken = Token(addOperatorToNumber(i + 1), TokenType.NUMBER)
                tokenList[i + 1] = newToken
                tokenList.removeAt(i)
                i--
            }
            i++
        }
        val parenthesisStack = LinkedList<Token>()
        for(token in tokenList){
            parenthesisStack.push(token)
            if (token.value == ")"){
                popValidContent(parenthesisStack)
            }

        }
        if (parenthesisStack.any{ it.type == TokenType.PARENTHESIS })
            throw InputError(TokenType.PARENTHESIS)
        var index = 0
        while (index < tokenList.size){
            val token = tokenList[index]
            try {
                if (token.type == TokenType.OPERATOR){
                    val next = tokenList[index+1]
                    val nextNext = tokenList[index+2]
                    val prev = tokenList[index-1]
                    if (next.type == TokenType.NUMBER && prev.value == "(" && nextNext.value == ")") {
                        tokenList[index -1] = Token(addOperatorToNumber(index+1), TokenType.NUMBER)
                        tokenList.remove(next)
                        tokenList.remove(nextNext)
                        tokenList.remove(token)
                    }
                }

            }catch (e: IndexOutOfBoundsException){
                continue
            }finally {
                index++
            }

        }
    }

    fun popValidContent(stack: LinkedList<Token>){
        if (stack.isEmpty())
            throw EmptyStackException()
        try {
            while (stack.peek().value != "(")
                stack.pop()
            stack.pop()
        }catch (e: EmptyStackException){
            throw InputError(TokenType.PARENTHESIS)
        }


    }

    private fun validateOperators(operatorIndexList: ArrayList<Int>) {
        var operatorIndexes = operatorIndexList
        var i = 1
        while (i < operatorIndexes.size) {
            val previous = operatorIndexes[i - 1]
            val current = operatorIndexes[i]
            val prevRef = tokenList[previous]
            val curRef = tokenList[current]

            if (previous + 1 == current) {
                if (prevRef.isAdditive() && curRef.isAdditive()) {
                    val test = Token(addOperators(prevRef.value, curRef.value), TokenType.OPERATOR)
                    tokenList[current] = test
                    tokenList.removeAt(previous)
                    operatorIndexes = getOperatorIndexList()
                    i--
                } else if (prevRef.isAdditive() && curRef.isMultiplicative())
                    throw OperatorError("${prevRef.value} can't be followed by ${curRef.value}")
                else if (prevRef.isMultiplicative() && curRef.isMultiplicative())
                    throw OperatorError("${prevRef.value} can't be followed by ${curRef.value}")

            }

            val nextRef = tokenList[current + 1]
            if (nextRef.value == ")")
                throw OperatorError("${curRef.value} can not be followed by )")
            if(nextRef.value == "(" && curRef.isAdditive())
                throw OperatorError("${curRef.value} can not be followed by (")

            i++
        }
    }

    private fun getOperatorIndexList(): ArrayList<Int> {
        val operatorIndexList = ArrayList<Int>()
        for ((index, token) in tokenList.withIndex()) {
            if (token.type == TokenType.OPERATOR)
                operatorIndexList.add(index)
        }
        return operatorIndexList
    }

    fun addOperators(previousOperator: String, currentOperator: String): String {
        val prevNum = "${previousOperator}1".toInt()
        val curNum = "${currentOperator}1".toInt()
        if (prevNum + curNum != 0)
            return "+"
        return "-"
    }

    fun addOperatorToNumber(numberIndex: Int): String {
        val operatorIndex = numberIndex - 1
        return tokenList[operatorIndex].value + tokenList[numberIndex].value
    }

}