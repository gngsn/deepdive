import kotlinx.html.stream.createHTML
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr

fun createTable() = createHTML().table {
    val numbers = mapOf(1 to "one", 2 to "two")
    for ((num, string) in numbers) {
        tr {
            td { +"$num" }
            td { +string }
        }
    }
}

fun createSimpleTable() = createHTML().table {
    this@table.tr {           // this@table 타입 = TABLE
        this@tr.td {          // this@tr 타입 = TR
            +"cell"           // 암시적 수신 객체로 this@td 을 사용할 수 있고 그 타입은 TD
        }
    }
}


fun main() {

}