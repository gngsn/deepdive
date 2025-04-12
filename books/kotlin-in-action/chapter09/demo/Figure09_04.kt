/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc Figure09_03.kt -d Figure09_03.jar
 *  ❯ kotlinc Figure09_04.kt -classpath Figure09_03.jar
 * ====================================
 */

class ObservableProperty(
    val propName: String,
    var propValue:
    Int,
    val observable: Observable
) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        observable.notifyObservers(propName, oldValue, newValue)
    }
}

class Person(val name: String, age: Int, salary: Int): Observable() {
    val _age = ObservableProperty("age", age, this)
    var age: Int
        get() = _age.getValue()
        set(newValue) {
            _age.setValue(newValue)
        }

    val _salary = ObservableProperty("salary", salary, this)
    var salary: Int
        get() = _salary.getValue()
        set(newValue) {
            _salary.setValue(newValue)
        }
}