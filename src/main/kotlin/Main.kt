package hu.notkulonme

fun main() {
    val calculator = Calculator()
    var userInput = ""
    println("type stop to stop the program")
    userInput = readln()
    while (userInput != "stop" && !userInput.isEmpty()) {
        try {
            println(calculator.calculate(userInput))
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        userInput = readln()
    }


}