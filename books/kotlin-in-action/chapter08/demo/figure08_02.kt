/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc figure08_02.kt && javap Animal
 * ====================================
 *
 * Compiled from "figure08_02.kt"
 * public final class Animal {
 *   public final java.lang.String getName();
 *     descriptor: ()Ljava/lang/String;
 *
 *   public final int getAge-pVg5ArA();
 *     descriptor: ()I
 *
 *   public Animal(java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker);
 *     descriptor: (Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
 * }
 */
class Animal(
    val name: String,
    val age: UInt
)

/** public static final java.util.List<java.lang.Integer> getListOfInts();  */
val listOfInts = listOf(1, 2, 3)
