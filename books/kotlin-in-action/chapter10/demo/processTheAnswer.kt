fun processTheAnswer(f: (Int) -> Int) {
    println(f(21))
}

fun main() {
    processTheAnswer {it}
}