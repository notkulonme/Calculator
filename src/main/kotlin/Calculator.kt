package hu.notkulonme

import Tree
import hu.notkulonme.tokenizer.Token
import hu.notkulonme.tokenizer.TokenType
import hu.notkulonme.tokenizer.Tokenizer

class Calculator {
    fun calculate(input: String): Number {
        val tokenizer = Tokenizer()
        val tokenList = tokenizer.tokenize(input)
        if (tokenList.size == 1)
            return tokenList[0].toNumber()
        val parser = Parser(tokenList)
        val root = parser.parseIntoTree()
        //root.printTree()
        return recursiveCalculator(root)
    }

    fun recursiveCalculator(root: Tree): Number {
        val leafLevel = root.leafLevel
        if (leafLevel[0] != root) {
            for (node in leafLevel) {
                if (node.exists()) {
                    val parent = node.parent
                    val operation = parent.value.getOperation()
                    parent.value = Token(
                        operation(
                            parent.leftChild.value.toDouble(),
                            parent.rightChild.value.toDouble()
                        ).toString(), TokenType.NUMBER
                    )
                    parent.deleteChildren()
                }
            }
            return recursiveCalculator(root)
        }
        return root.value.toNumber()
    }
}