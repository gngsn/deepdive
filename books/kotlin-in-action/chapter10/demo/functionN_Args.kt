/*
❯ kotlinc functionN_Args.kt
❯ javap FunctionN_ArgsKt.class

public final class FunctionN_ArgsKt {
  public static final void arg22(kotlin.jvm.functions.Function22<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, java.lang.Integer>);
  public static final void arg23(kotlin.jvm.functions.FunctionN<java.lang.Integer>);
  public static final void arg24(kotlin.jvm.functions.FunctionN<java.lang.Integer>);
  public static final void argArray(kotlin.jvm.functions.Function1<? super java.lang.Integer[], java.lang.Integer>);
...
}
*/

fun arg22(
    f: (
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int
    ) -> Int
) {
    println(
        f(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22
        )
    )
}


fun arg23(
    f: (
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int
    ) -> Int
) {
    println(
        f(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23
        )
    )
}

fun arg24(
    f: (
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int, Int, Int, Int, Int, Int, Int,
        Int, Int, Int, Int
    ) -> Int
) {
    println(
        f(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24
        )
    )
}

fun argArray(
    f: (Array<Int>) -> Int  // 배열을 직접 받는 타입
) {
    val intarr = arrayOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24
    )
    f(intarr)
}

fun main() {
    arg22 { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
            p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
            p21, p22 ->
        p1
    }

    arg23 { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
            p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
            p21, p22, p23 ->
        p1
    }

    arg24 { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
            p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
            p21, p22, p23, p24 ->
        p1
    }

    argArray { arr -> arr.sum() }
}