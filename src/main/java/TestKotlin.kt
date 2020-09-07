import java.lang.Integer.*

class TestKotlin {

    fun test(x: Any, y: Any): Unit {
        println("x:$x y:$y " + y.hashCode())
    }

    companion object {
        public var Instance = TestKotlin()
        var Instance2 = TestJava.Instance

        @JvmStatic
        fun static_method(s: String): Int? {
            return s.toIntOrNull()
            //return parseInt(s)
        }
    }

    //no static
//    @JvmStatic
//    fun  testStatic(){
//
//    }

    fun parseAndInc(number: String?): Int {
//        return number.let { Integer.parseInt(it) }
//                .let { it -> it + 1 } ?: 0 //number=null时会有java.lang.NumberFormatException: null
        return number?.let { Integer.parseInt(it) }
                ?.let { it -> it + 1 } ?: 0
    }

    operator fun plus(Te: TestKotlin): TestKotlin {
        return Te
    }

    operator fun minus(testKotlin: TestKotlin): TestKotlin {
        return this
    }

    inner class innerclass{ //类似于java的内部类

    }
    class 默认是嵌套类{ //类似于java的static inner 和外部类没关系

    }

    //中缀表达式 infix
    infix fun sayTo(name:String){
        println("sayto $name")
    }

}