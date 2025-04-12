/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc Figure09_01.kt
 *  ❯ kotlin Figure09_01Kt.class
 * ====================================
 */

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

data class MutablePoint(var x: Int, var y: Int) {
    operator fun plus(other: Point): MutablePoint {
        return MutablePoint(x + other.x, y + other.y)
    }

    operator fun plusAssign(other: Point) {
        this.x += other.x
        this.y += other.y
    }
}

fun main() {
    val point1 = Point(10, 20)
    val point2 = MutablePoint(10, 20)

//    point1 += Point(30, 40)
//    ^^^^^^  Kotlin Compile Error
//    figure09_01.kt:30:5: error: 'val' cannot be reassigned.

    point2 += Point(30, 40)
    println(point2)         // MutablePoint(x=40, y=60)
}

